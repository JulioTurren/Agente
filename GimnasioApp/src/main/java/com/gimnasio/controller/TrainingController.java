package com.gimnasio.controller;

import com.gimnasio.model.Exercise;
import com.gimnasio.model.Training;
import com.gimnasio.model.User;
import com.gimnasio.service.TrainingService;
import com.gimnasio.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class TrainingController {

    private final TrainingService trainingService;
    private final UserService userService;

    public TrainingController(TrainingService trainingService, UserService userService) {
        this.trainingService = trainingService;
        this.userService = userService;
    }

    @GetMapping
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Training> trainings = trainingService.getUserTrainings(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("trainings", trainings);
        model.addAttribute("totalTrainings", trainings.size());
        model.addAttribute("categories", Training.TrainingCategory.values());
        model.addAttribute("newTraining", new Training());
        return "dashboard";
    }

    @GetMapping("/training/{id}")
    public String trainingDetail(@PathVariable Long id,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        Training training = trainingService.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado"));

        List<Exercise> exercises = trainingService.getTrainingExercises(id);

        model.addAttribute("user", user);
        model.addAttribute("training", training);
        model.addAttribute("exercises", exercises);
        model.addAttribute("newExercise", new Exercise());
        return "training-detail";
    }

    @PostMapping("/create")
    public String createTraining(@Valid @ModelAttribute("training") Training training,
                                 BindingResult result,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "dashboard";
        }

        User user = userService.findByEmail(userDetails.getUsername());
        training.setUser(user);
        trainingService.createTraining(training);
        redirectAttributes.addFlashAttribute("successMessage", "Entrenamiento creado exitosamente");
        return "redirect:/dashboard";
    }

    @PostMapping("/training/{id}/add-exercise")
    public String addExercise(@PathVariable Long id,
                              @Valid @ModelAttribute("newExercise") Exercise exercise,
                              BindingResult result,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "redirect:/dashboard/training/" + id;
        }

        User user = userService.findByEmail(userDetails.getUsername());
        trainingService.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado"));

        trainingService.addExerciseToTraining(id, exercise);
        redirectAttributes.addFlashAttribute("successMessage", "Ejercicio agregado");
        return "redirect:/dashboard/training/" + id;
    }

    @PostMapping("/training/{trainingId}/delete-exercise/{exerciseId}")
    public String deleteExercise(@PathVariable Long trainingId,
                                 @PathVariable Long exerciseId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        trainingService.findById(trainingId)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado"));

        trainingService.deleteExercise(exerciseId);
        redirectAttributes.addFlashAttribute("successMessage", "Ejercicio eliminado");
        return "redirect:/dashboard/training/" + trainingId;
    }

    @PostMapping("/training/{id}/delete")
    public String deleteTraining(@PathVariable Long id,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        Training training = trainingService.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado"));

        trainingService.deleteTraining(id);
        redirectAttributes.addFlashAttribute("successMessage", "Entrenamiento eliminado");
        return "redirect:/dashboard";
    }

    @GetMapping("/filter/{category}")
    public String filterByCategory(@PathVariable Training.TrainingCategory category,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Training> trainings = trainingService.getUserTrainingsByCategory(user.getId(), category);

        model.addAttribute("user", user);
        model.addAttribute("trainings", trainings);
        model.addAttribute("totalTrainings", trainings.size());
        model.addAttribute("categories", Training.TrainingCategory.values());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("newTraining", new Training());
        return "dashboard";
    }
}
