package healthblog.controllers;

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

import java.util.Date;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model) {
        model.addAttribute("view", "article/create");

        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleBindingModel articleBindingModel) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        Article article = new Article(
                articleBindingModel.getCategory(),
                articleBindingModel.getTitle(),
                articleBindingModel.getContent(),
                userEntity,
                new Date()
        );

        article.setImage(articleBindingModel.getImage());

        this.articleRepository.saveAndFlush(article);

        return "redirect:/";
    }

    @GetMapping("article/{id}")
    public String details(Model model, @PathVariable Integer id) {
        Article article = this.articleRepository.findOne(id);

        if (article == null) return "redirect:/";

        model.addAttribute("article", article);
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
