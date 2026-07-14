package com.gimnasio.controller;

import com.gimnasio.model.FoodEntry;
import com.gimnasio.model.Membresia;
import com.gimnasio.model.User;
import com.gimnasio.service.FoodService;
import com.gimnasio.service.MembresiaService;
import com.gimnasio.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/nutrition")
public class FoodController {

    private final FoodService foodService;
    private final UserService userService;
    private final MembresiaService membresiaService;

    public FoodController(FoodService foodService, UserService userService, MembresiaService membresiaService) {
        this.foodService = foodService;
        this.userService = userService;
        this.membresiaService = membresiaService;
    }

    @GetMapping
    public String nutritionPage(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(required = false) String fecha,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        Membresia membresia = membresiaService.findByUserId(user.getId()).orElse(null);

        if (!membresiaService.tieneAccesoA(membresia, Membresia.MembresiaType.PRO)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Esta funcion requiere membresia Pro o superior");
            return "redirect:/membresia";
        }

        LocalDate fechaActual = (fecha != null) ? LocalDate.parse(fecha) : LocalDate.now();
        model.addAttribute("user", user);
        model.addAttribute("membresia", membresia);
        model.addAttribute("entries", foodService.getEntriesByDate(user.getId(), fechaActual));
        model.addAttribute("summary", foodService.getSummary(user.getId()));
        model.addAttribute("fechaActual", fechaActual);
        model.addAttribute("caloriasQuemadas", getCaloriasQuemadas(user.getId()));
        model.addAttribute("newFoodEntry", new FoodEntry());
        model.addAttribute("categorias", FoodEntry.FoodCategoria.values());
        return "food-tracker";
    }

    @PostMapping("/add")
    public String addFoodEntry(@ModelAttribute FoodEntry entry,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        Membresia membresia = membresiaService.findByUserId(user.getId()).orElse(null);

        if (!membresiaService.tieneAccesoA(membresia, Membresia.MembresiaType.PRO)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Requiere membresia Pro");
            return "redirect:/membresia";
        }

        entry.setUser(user);
        foodService.addEntry(entry);
        redirectAttributes.addFlashAttribute("successMessage", "Comida registrada exitosamente");
        return "redirect:/nutrition";
    }

    @PostMapping("/delete/{id}")
    public String deleteFoodEntry(@PathVariable Long id,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        foodService.deleteEntry(id);
        redirectAttributes.addFlashAttribute("successMessage", "Registro eliminado");
        return "redirect:/nutrition";
    }

    private Integer getCaloriasQuemadas(Long userId) {
        Integer total = 0;
        var trainings = userService.findById(userId).getTrainings();
        for (var t : trainings) {
            if (t.getEstimatedCalories() != null) {
                total += t.getEstimatedCalories();
            }
        }
        return total / Math.max(trainings.size(), 1);
    }
}
