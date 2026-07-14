package com.gimnasio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "food_entries")
public class FoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la comida es obligatorio")
    private String nombre;

    private String categoria;

    @Min(value = 1, message = "Las calorias deben ser al menos 1")
    private Integer calorias;

    private Double proteinas;

    private Double carbohidratos;

    private Double grasas;

    private Double gramos;

    private LocalDate fecha;

    private LocalTime hora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum FoodCategoria {
        DESAYUNO("Desayuno", "fa-sun", "#f39c12"),
        ALMUERZO("Almuerzo", "fa-bowl-food", "#27ae60"),
        CENA("Cena", "fa-moon", "#3498db"),
        SNACK("Snack", "fa-cookie", "#e67e22"),
        PRE_ENTreno("Pre-Entreno", "fa-bolt", "#e74c3c"),
        POST_ENTRENO("Post-Entreno", "fa-fire", "#9b59b6");

        private final String displayName;
        private final String icon;
        private final String color;

        FoodCategoria(String displayName, String icon, String color) {
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
        if (fecha == null) fecha = LocalDate.now();
        if (hora == null) hora = LocalTime.now();
    }

    public FoodEntry() {}

    public FoodEntry(String nombre, String categoria, Integer calorias, User user) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.calorias = calorias;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getCalorias() { return calorias; }
    public void setCalorias(Integer calorias) { this.calorias = calorias; }

    public Double getProteinas() { return proteinas; }
    public void setProteinas(Double proteinas) { this.proteinas = proteinas; }

    public Double getCarbohidratos() { return carbohidratos; }
    public void setCarbohidratos(Double carbohidratos) { this.carbohidratos = carbohidratos; }

    public Double getGrasas() { return grasas; }
    public void setGrasas(Double grasas) { this.grasas = grasas; }

    public Double getGramos() { return gramos; }
    public void setGramos(Double gramos) { this.gramos = gramos; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
