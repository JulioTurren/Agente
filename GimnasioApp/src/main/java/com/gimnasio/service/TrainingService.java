package com.gimnasio.service;

import com.gimnasio.model.Exercise;
import com.gimnasio.model.Training;
import com.gimnasio.model.User;
import com.gimnasio.repository.ExerciseRepository;
import com.gimnasio.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final ExerciseRepository exerciseRepository;

    public TrainingService(TrainingRepository trainingRepository, ExerciseRepository exerciseRepository) {
        this.trainingRepository = trainingRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public List<Training> getUserTrainings(Long userId) {
        return trainingRepository.findByUserIdOrderByDateCreatedDesc(userId);
    }

    public List<Training> getUserTrainingsByCategory(Long userId, Training.TrainingCategory category) {
        return trainingRepository.findByUserIdAndCategoryOrderByDateCreatedDesc(userId, category);
    }

    public Optional<Training> findById(Long id) {
        return trainingRepository.findById(id);
    }

    public List<Exercise> getTrainingExercises(Long trainingId) {
        return exerciseRepository.findByTrainingIdOrderByOrderAsc(trainingId);
    }

    @Transactional
    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Transactional
    public Training updateTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Transactional
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }

    @Transactional
    public Exercise addExerciseToTraining(Long trainingId, Exercise exercise) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado"));
        exercise.setTraining(training);

        List<Exercise> existing = exerciseRepository.findByTrainingIdOrderByOrderAsc(trainingId);
        exercise.setOrder(existing.size());

        Exercise saved = exerciseRepository.save(exercise);

        updateTrainingStats(trainingId);
        return saved;
    }

    @Transactional
    public void deleteExercise(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));
        Long trainingId = exercise.getTraining().getId();
        exerciseRepository.deleteById(exerciseId);
        updateTrainingStats(trainingId);
    }

    private void updateTrainingStats(Long trainingId) {
        Training training = trainingRepository.findById(trainingId).orElse(null);
        if (training != null) {
            List<Exercise> exercises = exerciseRepository.findByTrainingIdOrderByOrderAsc(trainingId);
            int totalSets = exercises.stream()
                    .mapToInt(e -> e.getSets() != null ? e.getSets() : 0)
                    .sum();
            training.setTotalSets(totalSets);
            training.setEstimatedCalories(totalSets * 5);
            trainingRepository.save(training);
        }
    }

    public Long getUserTrainingCount(Long userId) {
        return trainingRepository.countByUserId(userId);
    }
}
