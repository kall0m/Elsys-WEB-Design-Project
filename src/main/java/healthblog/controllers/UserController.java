package healthblog.controllers;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import healthblog.bindingModels.UserBindingModel;
import healthblog.models.Article;
import healthblog.models.Role;
import healthblog.models.User;
import healthblog.repositories.ArticleRepository;
import healthblog.repositories.RoleRepository;
import healthblog.repositories.UserRepository;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

//    @Autowired
//    RoleRepository roleRepository;
//    @Autowired
//    UserRepository userRepository;

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserController(ArticleRepository articleRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("view", "user/register");

        return "base-layout";
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel){

        if(!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
            return "redirect:/register";
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(
                userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword())
        );

        Role userRole = this.roleRepository.findByName("ROLE_USER");

        user.addRole(userRole);

        this.userRepository.saveAndFlush(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("view", "user/login");

        return "base-layout";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());

        //Set<Article> articles = user.getArticles();

//        TreeSet sortedArticles = new TreeSet();
//
//        sortedArticles.addAll(articles);

        model.addAttribute("user", user);
        //model.addAttribute("articles", articles);
        model.addAttribute("view", "user/profile");

        return "base-layout";
    }

    @GetMapping("profile/article/{id}")
    @PreAuthorize("isAuthenticated()")
    public String articleDetails(Model model, @PathVariable Integer id){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());

        Article article = this.articleRepository.findOne(id);

        if(article == null) return "redirect:/";

        model.addAttribute("user", user);
        model.addAttribute("article", article);
        model.addAttribute("view", "user/articleDetails");

        return "base-layout";
    }
}
