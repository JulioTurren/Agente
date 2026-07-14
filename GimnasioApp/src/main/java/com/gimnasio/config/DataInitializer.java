package com.gimnasio.config;

import com.gimnasio.model.Exercise;
import com.gimnasio.model.Membresia;
import com.gimnasio.model.Training;
import com.gimnasio.model.User;
import com.gimnasio.repository.ExerciseRepository;
import com.gimnasio.repository.MembresiaRepository;
import com.gimnasio.repository.TrainingRepository;
import com.gimnasio.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;
    private final ExerciseRepository exerciseRepository;
    private final MembresiaRepository membresiaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           TrainingRepository trainingRepository,
                           ExerciseRepository exerciseRepository,
                           MembresiaRepository membresiaRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
        this.exerciseRepository = exerciseRepository;
        this.membresiaRepository = membresiaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User demo = new User("Carlos Fitness", "demo@gym.com", passwordEncoder.encode("123456"));
        userRepository.save(demo);

        Membresia freeMembresia = new Membresia(Membresia.MembresiaType.FREE, demo);
        membresiaRepository.save(freeMembresia);
        demo.setMembresia(freeMembresia);
        userRepository.save(demo);

        Training pecho = new Training("Pecho y Triceps", "Rutina de fuerza para tren superior", Training.TrainingCategory.PECHO, demo);
        pecho.setTotalSets(15);
        pecho.setEstimatedCalories(350);
        trainingRepository.save(pecho);

        exerciseRepository.save(new Exercise("Press de Banca", "Pecho", 4, 10, 60.0, "kg", pecho));
        exerciseRepository.save(new Exercise("Press Inclinado con Mancuernas", "Pecho superior", 3, 12, 22.5, "kg", pecho));
        exerciseRepository.save(new Exercise("Aperturas en Maquina", "Pecho", 3, 12, 35.0, "kg", pecho));
        exerciseRepository.save(new Exercise("Fondos en Paralelas", "Triceps/Pecho", 3, 10, 0.0, "bodyweight", pecho));
        exerciseRepository.save(new Exercise("Extension de Triceps en Polea", "Triceps", 3, 12, 20.0, "kg", pecho));

        Training piernas = new Training("Piernas y Gluteos", "Dia de piernas intenso", Training.TrainingCategory.PIERNAS, demo);
        piernas.setTotalSets(18);
        piernas.setEstimatedCalories(450);
        trainingRepository.save(piernas);

        exerciseRepository.save(new Exercise("Sentadilla con Barra", "Cuadriceps", 4, 8, 80.0, "kg", piernas));
        exerciseRepository.save(new Exercise("Prensa de Piernas", "Cuadriceps", 3, 12, 120.0, "kg", piernas));
        exerciseRepository.save(new Exercise("Curl Femoral", "Isquiotibiales", 3, 12, 30.0, "kg", piernas));
        exerciseRepository.save(new Exercise("Elevacion de Talones", "Pantorrillas", 4, 15, 40.0, "kg", piernas));
        exerciseRepository.save(new Exercise("Hip Thrust", "Gluteos", 3, 10, 60.0, "kg", piernas));

        Training espalda = new Training("Espalda y Biceps", "Rutina de tirón", Training.TrainingCategory.ESPALDA, demo);
        espalda.setTotalSets(14);
        espalda.setEstimatedCalories(320);
        trainingRepository.save(espalda);

        exerciseRepository.save(new Exercise("Jalón al Pecho", "Dorsales", 4, 10, 45.0, "kg", espalda));
        exerciseRepository.save(new Exercise("Remo con Barra", "Espalda media", 4, 8, 60.0, "kg", espalda));
        exerciseRepository.save(new Exercise("Remo con Mancuerna", "Dorsales", 3, 12, 20.0, "kg", espalda));
        exerciseRepository.save(new Exercise("Curl de Bíceps con Barra", "Biceps", 3, 12, 25.0, "kg", espalda));
        exerciseRepository.save(new Exercise("Curl Martillo", "Biceps/Brazos", 3, 12, 12.0, "kg", espalda));

        Training cardio = new Training("Cardio HIIT", "Quemar grasa rapido", Training.TrainingCategory.CARDIO, demo);
        cardio.setTotalSets(0);
        cardio.setEstimatedCalories(500);
        trainingRepository.save(cardio);

        exerciseRepository.save(new Exercise("Burpees", "Full body", 3, 15, 0.0, "bodyweight", cardio));
        exerciseRepository.save(new Exercise("Mountain Climbers", "Core/Cardio", 3, 20, 0.0, "bodyweight", cardio));
        exerciseRepository.save(new Exercise("Saltos de Tijera", "Full body", 3, 30, 0.0, "bodyweight", cardio));
        exerciseRepository.save(new Exercise("High Knees", "Cardio", 3, 30, 0.0, "bodyweight", cardio));

        System.out.println("=== Datos demo cargados ===");
        System.out.println("Email: demo@gym.com");
        System.out.println("Password: 123456");
    }
}
