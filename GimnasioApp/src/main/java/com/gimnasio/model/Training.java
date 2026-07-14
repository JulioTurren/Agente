package com.gimnasio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del entrenamiento es obligatorio")
    private String name;

    private String description;

    @NotNull(message = "La categoria es obligatoria")
    @Enumerated(EnumType.STRING)
    private TrainingCategory category;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    private Duration duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderColumn(name = "exercise_order")
    private List<Exercise> exercises = new ArrayList<>();

    @Column(name = "total_sets")
    private Integer totalSets;

    @Column(name = "estimated_calories")
    private Integer estimatedCalories;

    public enum TrainingCategory {
        PECHO("Pecho", "fa-dumbbell", "#e74c3c"),
        ESPALDA("Espalda", "fa-arrows-up-down", "#3498db"),
        PIERNAS("Piernas", "fa-person-walking", "#2ecc71"),
        HOMBROS("Hombros", "fa-hand-fist", "#f39c12"),
        BRAZOS("Brazos", "fa-hand-back-fist", "#9b59b6"),
        ABDOMEN("Abdomen", "fa-ring", "#1abc9c"),
        CARDIO("Cardio", "fa-heart-pulse", "#e67e22"),
        FULL_BODY("Full Body", "fa-person", "#e91e63");

        private final String displayName;
        private final String icon;
        private final String color;

        TrainingCategory(String displayName, String icon, String color) {
            this.displayName = displayName;
            this.icon = icon;
            this.color = color;
        }

        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }
        public String getColor() { return color; }
    }

    @PrePersist
    protected void onCreate() {
        dateCreated = LocalDate.now();
    }

    public Training() {}

    public Training(String name, String description, TrainingCategory category, User user) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TrainingCategory getCategory() { return category; }
    public void setCategory(TrainingCategory category) { this.category = category; }

    public LocalDate getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDate dateCreated) { this.dateCreated = dateCreated; }

    public Duration getDuration() { return duration; }
    public void setDuration(Duration duration) { this.duration = duration; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Exercise> getExercises() { return exercises; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }

    public Integer getTotalSets() { return totalSets; }
    public void setTotalSets(Integer totalSets) { this.totalSets = totalSets; }

    public Integer getEstimatedCalories() { return estimatedCalories; }
    public void setEstimatedCalories(Integer estimatedCalories) { this.estimatedCalories = estimatedCalories; }
}
