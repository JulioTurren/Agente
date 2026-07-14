package com.gimnasio.controller;

import com.gimnasio.model.User;
import com.gimnasio.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerNewUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Cuenta creada exitosamente. Inicia sesion.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }
}
