package healthblog.controllers;

import healthblog.models.Tag;
import healthblog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import healthblog.models.Article;
import healthblog.repositories.ArticleRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final ArticleRepository articleRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    public HomeController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String search(@RequestParam(value = "s", required = false) String s, Model model) {
        List<Article> articles = this.articleRepository.findAll();

        if(s != null) {
            List<String> tagsNames = Arrays.stream(s.split("[,.!? |/]+")).collect(Collectors.toList());

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
            }

            model.addAttribute("articles", resultArticles);
        } else {
            model.addAttribute("articles", articles);
        }

        model.addAttribute("view", "home/articles");

        return "base-layout";

    }

    @GetMapping("/fitness")
    public String fitness(Model model) {
        List<Article> articles = this.articleRepository.findAll().stream().filter(a -> a.getCategory().toLowerCase().equals("fitness")).collect(Collectors.toList());

        model.addAttribute("articles", articles);
        model.addAttribute("view", "home/articles");

        return "base-layout";
    }

    @GetMapping("/fitness/challenges")
    public String challenges(Model model) {
        model.addAttribute("view", "home/fitness-challenges");

        return "base-layout";
    }

    @GetMapping("/fitness/exercises")
    public String exercises(Model model) {
        model.addAttribute("view", "home/fitness-exercises");

        return "base-layout";
    }

    @GetMapping("/food")
    public String food(Model model) {
        List<Article> articles = this.articleRepository.findAll().stream().filter(a -> a.getCategory().toLowerCase().equals("food")).collect(Collectors.toList());

        model.addAttribute("articles", articles);
        model.addAttribute("view", "home/articles");

        return "base-layout";
    }

    @GetMapping("/food/healthy")
    public String healthy(Model model) {
        model.addAttribute("view", "home/food-healthy");

        return "base-layout";
    }

    @GetMapping("/food/desserts")
    public String desserts(Model model) {
        model.addAttribute("view", "home/food-desserts");

        return "base-layout";
    }

    @GetMapping("/lifestyle")
    public String lifestyle(Model model) {
        List<Article> articles = this.articleRepository.findAll().stream().filter(a -> a.getCategory().toLowerCase().equals("lifestyle")).collect(Collectors.toList());

        model.addAttribute("articles", articles);
        model.addAttribute("view", "home/articles");

        return "base-layout";
    }

    @GetMapping("/lifestyle/wear-it-better")
    public String wearItBetter(Model model) {
        model.addAttribute("view", "home/lifestyle-wear-it-better");

        return "base-layout";
    }

    @GetMapping("/lifestyle/motivation")
    public String motivation(Model model) {
        model.addAttribute("view", "home/lifestyle-motivation");

        return "base-layout";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("view", "home/about");

        return "base-layout";
    }

    @GetMapping("/about/my-transformation")
    public String myTransformation(Model model) {
        model.addAttribute("view", "home/about-my-transformation");

        return "base-layout";
    }

    @GetMapping("/about/me-myself-and-i")
    public String meMyselfAndI(Model model) {
        model.addAttribute("view", "home/about-me-myself-and-i");

        return "base-layout";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("view", "home/contact");

        return "base-layout";
    }
}

