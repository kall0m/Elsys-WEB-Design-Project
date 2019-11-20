package healthblog.controllers;

import healthblog.models.*;
import healthblog.services.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import healthblog.bindingModels.ArticleBindingModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ArticleController {
    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ArticleService articleService;

    private void addTagsToArticle(String[] tagsNames, Article article) {
        for (String tagName : tagsNames) {
            Tag tag = this.tagService.findTag(tagName);

            if(tag == null) {
                tag = new Tag(tagName);

                this.tagService.saveTag(tag);
            }

            article.addTag(tag);
        }
    }

    private void saveFile(Path path, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();

        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String constructFileName(String directory, int articleId, MultipartFile file, List<MultipartFile> files) {
        String fileNumber = String.valueOf(files.indexOf(file) + 1);

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        return directory + articleId + "_" + fileNumber + "." + extension;
    }

    private void saveImagesAndSetToArticle(List<MultipartFile> images, Article article) throws IOException {
        for(MultipartFile imageFile : images) {
            String fileName = constructFileName(Image.IMAGES_DIR, article.getId(), imageFile, images);

            Path imagePath = Paths.get(fileName);

            saveFile(imagePath, imageFile);

            Image image = this.imageService.findImage(imagePath.toString());

            if(image == null) {
                image = new Image(imagePath.toString(), article);

                this.imageService.saveImage(image);
            }

            article.addImage(image);
        }
    }

    private void saveDocumentsAndSetToArticle(List<MultipartFile> documents, Article article) throws IOException {
        for(MultipartFile documentFile : documents) {
            String fileName = constructFileName(Document.DOCUMENTS_DIR, article.getId(), documentFile, documents);

            Path documentPath = Paths.get(fileName);

            saveFile(documentPath, documentFile);

            Document document = this.documentService.findDocument(documentPath.toString());

            if(document == null) {
                document = new Document(documentPath.toString(), article);

                this.documentService.saveDocument(document);
            }

            article.addDocument(document);
        }
    }

    public static List<Article> subListArticles(List<Article> articles) {
        int articlesFromIndex = 0;
        int articlesToIndex = 10;

        List<Article> subListedArticles = new ArrayList<>();

        while(true) {
            try {
                subListedArticles = articles.subList(articlesFromIndex, articlesToIndex);
                break;
            } catch (IndexOutOfBoundsException exception) {
                articlesToIndex--;
            }
        }

        return subListedArticles;
    }

    public static List<String> getArticlesFirstImages(List<Article> articles) {
        List<String> base64images = new ArrayList<>();

        for(Article article : articles) {
            try {
                base64images.add(Base64.encodeBase64String(Files.readAllBytes(Paths.get(article.getImages().get(0).getPath()))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return base64images;
    }

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model) {
        model.addAttribute("view", "article/create");

        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleBindingModel articleBindingModel) throws IOException {
        for (MultipartFile file : articleBindingModel.getDocuments()) {
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            if (!"pdf".equals(extension)) {
                return "redirect:/article/create";
            }
        }

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userEntity = this.userService.findByEmail(user.getUsername());

        Article article = new Article(
                articleBindingModel.getCategory().toLowerCase(),
                articleBindingModel.getTitle(),
                articleBindingModel.getContent(),
                userEntity,
                new Date()
        );

        if(!articleBindingModel.getTags().isEmpty()) {
            addTagsToArticle(articleBindingModel.getTags().trim().split("\\s*,\\s*"), article);
        }

        this.articleService.saveArticle(article);

        saveImagesAndSetToArticle(articleBindingModel.getImages(), article);
        saveDocumentsAndSetToArticle(articleBindingModel.getDocuments(), article);

        this.articleService.saveArticle(article);

        return "redirect:/article/" + article.getId();
    }

    @GetMapping("article/{id}")
    public String details(Model model, @PathVariable Integer id) {
        Article article = this.articleService.findArticle(id);

        if (article == null) return "redirect:/";

        List<Article> similar = new ArrayList<>();

        List<Article> articles = this.articleService.getAllArticles();

        int count = 0;

        for(Tag t : article.getTags()) {
            for(Article a : articles) {
                if(a.getTags().contains(t)) {
                    if(!a.equals(article) && !similar.contains(a)) {
                        similar.add(a);

                        count++;

                        if(count == 10) {
                            break;
                        }
                    }
                }
            }
            if(count == 10) {
                break;
            }
        }

        List<String> base64images = new ArrayList<>();

        for(Image image : article.getImages()) {
            try {
                base64images.add(Base64.encodeBase64String(Files.readAllBytes(Paths.get(image.getPath()))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> base64documents = new ArrayList<>();

        for(Document document : article.getDocuments()) {
            try {
                base64documents.add(Base64.encodeBase64String(Files.readAllBytes(Paths.get(document.getPath()))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("article", article);
        model.addAttribute("articleImages", base64images);
        model.addAttribute("articleDocuments", base64documents);
        model.addAttribute("articleTags", article.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        model.addAttribute("similarArticles", subListArticles(similar));
        model.addAttribute("similarArticlesImages", getArticlesFirstImages(similar));
        model.addAttribute("view", "article/details");

        return "base-layout";
    }

    @GetMapping("article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(Model model, @PathVariable Integer id) {
        Article article = this.articleService.findArticle(id);

        if (article == null) return "redirect:/";

        StringBuilder tagsNames = new StringBuilder();

        for(Tag tag : article.getTags()) {
            tagsNames.append(tag.getName());
            if(article.getTags().size() > 1 && !tag.equals(article.getTags().get(article.getTags().size() - 1))) {
                tagsNames.append(", ");
            }
        }

        String articleTags = tagsNames.toString();

        model.addAttribute("article", article);
        model.addAttribute("articleTags", articleTags);
        model.addAttribute("view", "article/edit");

        return "base-layout";
    }

    @PostMapping("article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(ArticleBindingModel articleBindingModel, @PathVariable Integer id) throws IOException {
        Article article = this.articleService.findArticle(id);

        if (article == null) return "redirect:/";

        article.setCategory(articleBindingModel.getCategory().toLowerCase());
        article.setTitle(articleBindingModel.getTitle());
        article.setContent(articleBindingModel.getContent());

        article.setTags(new ArrayList<>());

        if(articleBindingModel.getTags() != null) {
            addTagsToArticle(articleBindingModel.getTags().trim().split("\\s*,\\s*"), article);
        }

        if(!articleBindingModel.getImages().get(0).getOriginalFilename().equals("")) {
            for(Image image : article.getImages()) {
                Files.delete(Paths.get(image.getPath()));
                image.setArticle(null);
                this.imageService.deleteImage(image);
            }

            article.setImages(new ArrayList<>());

            saveImagesAndSetToArticle(articleBindingModel.getImages(), article);
            saveDocumentsAndSetToArticle(articleBindingModel.getDocuments(), article);
        }

        this.articleService.saveArticle(article);

        return "redirect:/article/" + article.getId();
    }

    @GetMapping("article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id) {
        Article article = this.articleService.findArticle(id);

        if(article == null) return "redirect:/";

        String articleImageBase64 = "";

        try {
            articleImageBase64 = Base64.encodeBase64String(Files.readAllBytes(Paths.get(article.getImages().get(0).getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("article", article);
        model.addAttribute("articleImage", articleImageBase64);
        model.addAttribute("view", "article/delete");

        return "base-layout";
    }

    @PostMapping("article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(Model model, @PathVariable Integer id){
        Article article = this.articleService.findArticle(id);

        if(article == null) return "redirect:/";

        for(Tag tag : article.getTags()) {
            tag.getArticles().remove(article);
        }

        for(Image image : article.getImages()) {
            image.setArticle(null);
            this.imageService.deleteImage(image);
        }

        this.articleService.deleteArticle(article);

        return "redirect:/";
    }
}
