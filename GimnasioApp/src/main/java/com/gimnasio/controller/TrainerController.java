package com.gimnasio.controller;

import com.gimnasio.model.ChatMessage;
import com.gimnasio.model.Membresia;
import com.gimnasio.model.User;
import com.gimnasio.service.MembresiaService;
import com.gimnasio.service.TrainerService;
import com.gimnasio.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/trainer")
public class TrainerController {

    private final TrainerService trainerService;
    private final UserService userService;
    private final MembresiaService membresiaService;

    public TrainerController(TrainerService trainerService, UserService userService, MembresiaService membresiaService) {
        this.trainerService = trainerService;
        this.userService = userService;
        this.membresiaService = membresiaService;
    }

    @GetMapping
    public String trainerPage(@AuthenticationPrincipal UserDetails userDetails,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        Membresia membresia = membresiaService.findByUserId(user.getId()).orElse(null);

        if (!membresiaService.tieneAccesoA(membresia, Membresia.MembresiaType.PREMIUM)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "El entrenador virtual requiere membresia Premium");
            return "redirect:/membresia";
        }

        List<ChatMessage> messages = trainerService.getConversation(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("membresia", membresia);
        model.addAttribute("messages", messages);
        model.addAttribute("messageCount", trainerService.getMessageCount(user.getId()));
        return "trainer";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String contenido,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        Membresia membresia = membresiaService.findByUserId(user.getId()).orElse(null);

        if (!membresiaService.tieneAccesoA(membresia, Membresia.MembresiaType.PREMIUM)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Requiere membresia Premium");
            return "redirect:/membresia";
        }

        trainerService.sendMessage(user.getId(), user, contenido);
        return "redirect:/trainer";
    }
}
