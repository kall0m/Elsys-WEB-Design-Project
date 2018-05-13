package healthblog.controllers;

import healthblog.models.Image;
import healthblog.models.Tag;
import healthblog.repositories.ImageRepository;
import healthblog.services.ImageService;
import healthblog.services.TagService;
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
import healthblog.models.Article;
import healthblog.models.User;
import healthblog.repositories.ArticleRepository;
import healthblog.repositories.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private ImageService imageService;

    private static List<Article> subListArticles(List<Article> articles) {
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

    @Autowired
    public ArticleController(ArticleRepository articleRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
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
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        Article article = new Article(
                articleBindingModel.getCategory().toLowerCase(),
                articleBindingModel.getTitle(),
                articleBindingModel.getContent(),
                userEntity,
                new Date()
        );

        if(!articleBindingModel.getTags().isEmpty()) {
            for (String tagName : articleBindingModel.getTags().trim().split("\\s*,\\s*")) {
                Tag tag = this.tagService.findTag(tagName);

                if(tag == null) {
                    tag = new Tag(tagName);

                    this.tagService.saveTag(tag);
                }

                article.addTag(tag);
            }
        }

        article.setImage(articleBindingModel.getImage());

        List<Image> imagesBytes = new ArrayList<>();

        for(MultipartFile imageFile : articleBindingModel.getImages()) {
            byte[] imageBytes = imageFile.getBytes();

            try {
                Files.write(Paths.get("retrieve-dir/" + "img_original" + "." + "JPG"), imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] decodedImage = Base64.decodeBase64(imageBytes);

            try {
                Files.write(Paths.get("retrieve-dir/" + "img_decoded" + "." + "JPG"), decodedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String base64 = Base64.encodeBase64URLSafeString(imageBytes);

            Image image = imageRepository.findByBase64(base64);

            if(image == null) {
                image = new Image(imageBytes);

                image.setBase64(Base64.encodeBase64String(imageBytes));

                this.imageService.saveImage(image);
            }

            imagesBytes.add(image);
        }

        article.setImages(imagesBytes);

        this.articleRepository.saveAndFlush(article);

        return "redirect:/";
    }

    @GetMapping("article/{id}")
    public String details(Model model, @PathVariable Integer id) {
        Article article = this.articleRepository.findOne(id);

        if (article == null) return "redirect:/";

        List<Article> similar = new ArrayList<>();

        List<Article> articles = this.articleRepository.findAll();

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

        model.addAttribute("article", article);
        model.addAttribute("similarArticles", subListArticles(similar));
        model.addAttribute("view", "article/details");

        return "base-layout";
    }

    @GetMapping("article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(Model model, @PathVariable Integer id) {
        Article article = this.articleRepository.findOne(id);

        if (article == null) return "redirect:/";

        model.addAttribute("article", article);
        model.addAttribute("view", "article/edit");

        return "base-layout";
    }

    @PostMapping("article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(ArticleBindingModel articleBindingModel, @PathVariable Integer id) {
        Article article = this.articleRepository.findOne(id);

        if (article == null) return "redirect:/";

        article.setCategory(articleBindingModel.getCategory());
        article.setTitle(articleBindingModel.getTitle());
        article.setContent(articleBindingModel.getContent());
        article.setImage(articleBindingModel.getImage());

        this.articleRepository.saveAndFlush(article);

        return "redirect:/article/" + article.getId();
    }

    @GetMapping("article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id) {
        Article article = this.articleRepository.findOne(id);

        if(article == null) return "redirect:/";

        model.addAttribute("article", article);
        model.addAttribute("view", "article/delete");

        return "base-layout";
    }

    @PostMapping("article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(Model model, @PathVariable Integer id){
        Article article = this.articleRepository.findOne(id);

        if(article == null) return "redirect:/";

        this.articleRepository.delete(article);

        return "redirect:/";
    }
}
