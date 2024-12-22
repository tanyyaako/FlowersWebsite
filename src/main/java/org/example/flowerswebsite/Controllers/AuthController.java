package org.example.flowerswebsite.Controllers;

import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.flowerswebsite.DTO.UserRegistrationDto;
import org.example.flowerswebsite.Entities.UserEntity;
import org.example.flowerswebsite.services.Impl.AuthService;
import org.example.viewModel.Base.BaseView;
import org.example.viewModel.User.UserProfileView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class AuthController {

    private AuthService authService;
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ModelAttribute("userRegistrationDto")
    public UserRegistrationDto initForm() {
        return new UserRegistrationDto();
    }
    public BaseView createBaseViewModel(String logo, String aboutUsInformation) {
        return new BaseView(logo, aboutUsInformation);
    }

    @GetMapping("/register")
    public String register() {
        LOG.log(Level.INFO,"User on register page");
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid UserRegistrationDto userRegistrationDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationDto", userRegistrationDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto", bindingResult);

            return "redirect:/users/register";
        }

        LOG.log(Level.INFO,"User registered");
        this.authService.register(userRegistrationDto);

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login() {
        LOG.log(Level.INFO,"User on login page");
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {

        LOG.log(Level.INFO,"User: "+username+ " failed in login");
        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("badCredentials", true);

        return "redirect:/users/login";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        LOG.log(Level.INFO,"User: "+principal.getName()+ " on profile page");
        String logoPath = "/images/logo.jpg";
        BaseView baseView = new BaseView(logoPath, "Мы продаём цветы 5 лет");
        model.addAttribute("baseView", baseView);
        String username = principal.getName();
        UserEntity user = authService.getUser(username);

        UserProfileView userProfileView = new UserProfileView(
                username,
                user.getPhoneNumber(),
                user.getEmail()
        );
        model.addAttribute("user", userProfileView);
        return "profile.html";
    }
}
