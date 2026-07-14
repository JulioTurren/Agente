package com.gimnasio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    private String name;

    private String muscleGroup;

    @Min(value = 1, message = "Las series deben ser al menos 1")
    private Integer sets;

    @Min(value = 1, message = "Las repeticiones deben ser al menos 1")
    private Integer reps;

    private Double weight;

    private String unit;

    private String notes;

    @Column(name = "exercise_order")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    public Exercise() {}

    public Exercise(String name, String muscleGroup, Integer sets, Integer reps, Double weight, String unit, Training training) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.unit = unit;
        this.training = training;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }

    public Integer getSets() { return sets; }
    public void setSets(Integer sets) { this.sets = sets; }

    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }

    public Training getTraining() { return training; }
    public void setTraining(Training training) { this.training = training; }
}
