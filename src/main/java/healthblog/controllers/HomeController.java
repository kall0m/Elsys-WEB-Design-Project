package healthblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import healthblog.models.Article;
import healthblog.repositories.ArticleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final ArticleRepository articleRepository;

    @Autowired
    public HomeController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = this.articleRepository.findAll();

        model.addAttribute("articles", articles);
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

