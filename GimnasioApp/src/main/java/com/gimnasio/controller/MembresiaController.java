package com.gimnasio.controller;

import com.gimnasio.model.Membresia;
import com.gimnasio.model.User;
import com.gimnasio.service.MembresiaService;
import com.gimnasio.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/membresia")
public class MembresiaController {

    private final MembresiaService membresiaService;
    private final UserService userService;

    public MembresiaController(MembresiaService membresiaService, UserService userService) {
        this.membresiaService = membresiaService;
        this.userService = userService;
    }
/* 
    @GetMapping
    public String showMembresiaPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        Membresia membresia = membresiaService.findByUserId(user.getId()).orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("membresia", membresia);
        model.addAttribute("tipos", Membresia.MembresiaType.values());
        return "membership";
    }
*/
@GetMapping
public String showMembresiaPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {

    System.out.println("Entró al controlador");

    System.out.println("UserDetails: " + userDetails);

    User user = userService.findByEmail(userDetails.getUsername());

    System.out.println("User: " + user);

    Membresia membresia = membresiaService.findByUserId(user.getId()).orElse(null);

    System.out.println("Membresia: " + membresia);

    model.addAttribute("user", user);
    model.addAttribute("membresia", membresia);
    model.addAttribute("tipos", Membresia.MembresiaType.values());

    return "membership";
}
    @PostMapping("/suscribir")
    public String suscribir(@RequestParam Membresia.MembresiaType tipo,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        Membresia nueva = membresiaService.suscribir(user.getId(), user, tipo);

        user.setMembresia(nueva);
        userService.updateUser(user);

        redirectAttributes.addFlashAttribute("successMessage",
                "Te has suscrito a " + tipo.getDisplayName() + " exitosamente!");
        return "redirect:/dashboard";
    }
}
