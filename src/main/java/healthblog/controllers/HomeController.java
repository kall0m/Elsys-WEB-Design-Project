package healthblog.controllers;

import healthblog.models.Tag;
import healthblog.services.ArticleService;
import healthblog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import healthblog.models.Article;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    private ArticleService articleService;

    /*@Autowired
    private TagService tagService;*/

    private static final int ARTICLES_PER_PAGE_COUNT = 8;

    private static List<Article> pageArticles(Integer pageNum, List<Article> articles) {
        int articlesFromIndex = ARTICLES_PER_PAGE_COUNT * (pageNum - 1);
        int articlesToIndex = ARTICLES_PER_PAGE_COUNT * pageNum;

        List<Article> articlesPerPage = new ArrayList<>();

        while(true) {
            try {
                articlesPerPage = articles.subList(articlesFromIndex, articlesToIndex);
                break;
            } catch (IndexOutOfBoundsException exception) {
                articlesToIndex--;
            }
        }

        return articlesPerPage;
    }

    private static List<Article> searchArticles(String searchWord, List<Article> articles) {
        List<String> keywords = Arrays.stream(searchWord.split("[,.!? |/]+")).collect(Collectors.toList());

        List<Article> searchedArticles = new ArrayList<>();

        for(String k : keywords) {
            for(Article a : articles) {
                if (a.getTitle().toLowerCase().contains(k.toLowerCase()) || a.getContent().toLowerCase().contains(k.toLowerCase())) {
                    searchedArticles.add(a);
                }
            }
        }

        return searchedArticles;
    }

    @Autowired
    public HomeController(ArticleService ArticleService) {
        this.articleService = articleService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String search(@RequestParam(value = "s", required = false) String s, Model model) {
        List<Article> articles = new ArrayList<>();

        try {
            articles = pageArticles(1, this.articleService.getAllArticles());
        } catch (IllegalArgumentException exception) {
            model.addAttribute("view", "error/page-not-found");

            return "base-layout";
        }

        if(s != null) {
            if(s.length() < 3) {
                model.addAttribute("view", "error/search-not-found");

                return "base-layout";
            }

            List<Article> searchedArticles = new ArrayList<>();

            try {
                searchedArticles = pageArticles(1, searchArticles(s, this.articleService.getAllArticles()));
            } catch (IllegalArgumentException exception) {
                model.addAttribute("view", "error/page-not-found");

                return "base-layout";
            }

            if(searchedArticles.isEmpty()) {
                model.addAttribute("view", "error/search-not-found");

                return "base-layout";
            }

            model.addAttribute("articles", searchedArticles);
        } else {
            model.addAttribute("articles", articles);
        }

        model.addAttribute("pageNum", 1);
        model.addAttribute("category", "index");
        model.addAttribute("allArticlesCount", this.articleService.getAllArticles().size());
        model.addAttribute("articlesPerPageCount", ARTICLES_PER_PAGE_COUNT);
        model.addAttribute("s", s);
        model.addAttribute("view", "home/articles");

        return "base-layout";

    }

    @RequestMapping(value = "/page/{pageNum}", method = RequestMethod.GET)
    public String paging(@RequestParam(value = "s", required = false) String s, Model model, @PathVariable Integer pageNum) {
        List<Article> articlesPerPage = new ArrayList<>();

        List<Article> searchedArticles = this.articleService.getAllArticles();

        if(s != null) {
            if(s.length() < 3) {
                model.addAttribute("view", "error/search-not-found");

                return "base-layout";
            }

            searchedArticles = searchArticles(s, this.articleService.getAllArticles());

            if(searchedArticles.isEmpty()) {
                model.addAttribute("view", "error/search-not-found");

                return "base-layout";
            }
        } else {
            if(pageNum <= 1) {
                return "redirect:/";
            }
        }

        try {
            articlesPerPage = pageArticles(pageNum, searchedArticles);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("view", "error/page-not-found");

            return "base-layout";
        }

        model.addAttribute("articles", articlesPerPage);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("category", "index");
        model.addAttribute("allArticlesCount", searchedArticles.size());
        model.addAttribute("articlesPerPageCount", ARTICLES_PER_PAGE_COUNT);
        model.addAttribute("s", s);
        model.addAttribute("view", "home/articles");

        return "base-layout";
    }

    @GetMapping("/fitness")
    public String fitness(Model model) {
        List<Article> articles = new ArrayList<>();

        try {
            articles = pageArticles(1, this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("fitness")).collect(Collectors.toList()));
        } catch (IllegalArgumentException exception) {
            model.addAttribute("view", "error/page-not-found");

            return "base-layout";
        }

        model.addAttribute("articles", articles);
        model.addAttribute("pageNum", 1);
        model.addAttribute("category", "fitness");
        model.addAttribute("allArticlesCount", this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("fitness")).collect(Collectors.toList()).size());
        model.addAttribute("articlesPerPageCount", ARTICLES_PER_PAGE_COUNT);
        model.addAttribute("view", "home/articles");

        return "base-layout";
    }

    @GetMapping("/fitness/page/{pageNum}")
    public String fitnessPaging(Model model, @PathVariable Integer pageNum) {
        if(pageNum <= 1) {
            return "redirect:/fitness";
        }

        List<Article> articlesPerPage = new ArrayList<>();

        try {
            articlesPerPage = pageArticles(pageNum, this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("fitness")).collect(Collectors.toList()));
        } catch (IllegalArgumentException exception) {
            model.addAttribute("view", "error/page-not-found");

            return "base-layout";
        }

        model.addAttribute("articles", articlesPerPage);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("category", "fitness");
        model.addAttribute("allArticlesCount", this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("fitness")).collect(Collectors.toList()).size());
        model.addAttribute("articlesPerPageCount", ARTICLES_PER_PAGE_COUNT);
        model.addAttribute("view", "home/articles");

        return "base-layout";
    }

    @GetMapping("/food")
    public String food(Model model) {
        List<Article> articles = new ArrayList<>();

        try {
            articles = pageArticles(1, this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("food")).collect(Collectors.toList()));
        } catch (IllegalArgumentException exception) {
            model.addAttribute("view", "error/page-not-found");

            return "base-layout";
        }

        model.addAttribute("articles", articles);
        model.addAttribute("pageNum", 1);
        model.addAttribute("category", "food");
        model.addAttribute("allArticlesCount", this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("food")).collect(Collectors.toList()).size());
        model.addAttribute("articlesPerPageCount", ARTICLES_PER_PAGE_COUNT);
        model.addAttribute("view", "home/articles");

        return "base-layout";
    }

    @GetMapping("/food/page/{pageNum}")
    public String foodPaging(Model model, @PathVariable Integer pageNum) {
        if(pageNum <= 1) {
            return "redirect:/food";
        }

        List<Article> articlesPerPage = new ArrayList<>();

        try {
            articlesPerPage = pageArticles(pageNum, this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("food")).collect(Collectors.toList()));
        } catch (IllegalArgumentException exception) {
            model.addAttribute("view", "error/page-not-found");

            return "base-layout";
        }

        model.addAttribute("articles", articlesPerPage);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("category", "food");
        model.addAttribute("allArticlesCount", this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("food")).collect(Collectors.toList()).size());
        model.addAttribute("articlesPerPageCount", ARTICLES_PER_PAGE_COUNT);
        model.addAttribute("view", "home/articles");

        return "base-layout";
    }

    @GetMapping("/lifestyle")
    public String lifestyle(Model model) {
        List<Article> articles = new ArrayList<>();

        try {
            articles = pageArticles(1, this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("lifestyle")).collect(Collectors.toList()));
        } catch (IllegalArgumentException exception) {
            model.addAttribute("view", "error/page-not-found");

            return "base-layout";
        }

        model.addAttribute("articles", articles);
        model.addAttribute("pageNum", 1);
        model.addAttribute("category", "lifestyle");
        model.addAttribute("allArticlesCount", this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("lifestyle")).collect(Collectors.toList()).size());
        model.addAttribute("articlesPerPageCount", ARTICLES_PER_PAGE_COUNT);
        model.addAttribute("view", "home/articles");

        return "base-layout";
    }

    @GetMapping("/lifestyle/page/{pageNum}")
    public String lifestylePaging(Model model, @PathVariable Integer pageNum) {
        if(pageNum <= 1) {
            return "redirect:/lifestyle";
        }

        List<Article> articlesPerPage = new ArrayList<>();

        try {
            articlesPerPage = pageArticles(pageNum, this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("lifestyle")).collect(Collectors.toList()));
        } catch (IllegalArgumentException exception) {
            model.addAttribute("view", "error/page-not-found");

            return "base-layout";
        }

        model.addAttribute("articles", articlesPerPage);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("category", "lifestyle");
        model.addAttribute("allArticlesCount", this.articleService.getAllArticles().stream().filter(a -> a.getCategory().equals("lifestyle")).collect(Collectors.toList()).size());
        model.addAttribute("articlesPerPageCount", ARTICLES_PER_PAGE_COUNT);
        model.addAttribute("view", "home/articles");

        return "base-layout";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("view", "home/about");

        return "base-layout";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("view", "home/contact");

        return "base-layout";
    }
}

/* SEARCH BY TAGS */
            /*List<String> tagsNames = Arrays.stream(s.split("[,.!? |/]+")).collect(Collectors.toList());

            List<Tag> resultTags = new ArrayList<>();

            for(String t : tagsNames) {
                Tag tag = this.tagService.findTag(t);

                if(tag != null) {
                    resultTags.add(tag);
                }
            }

            List<Article> resultArticles = new ArrayList<>();

            for(Tag t : resultTags) {
                for(Article a : articles) {
                    if(a.getTags().contains(t)) {
                        if(!resultArticles.contains(a)) {
                            resultArticles.add(a);
                        }
                    }
                }
            }

            if(resultArticles.isEmpty()) {
                model.addAttribute("view", "error/search-not-found");

                return "base-layout";
            }*/