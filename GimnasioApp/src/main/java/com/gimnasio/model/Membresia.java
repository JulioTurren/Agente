package com.gimnasio.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "membresias")
public class Membresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MembresiaType tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    private boolean activa;

    @Column(name = "dias_por_semana")
    private Integer diasPorSemana;

    public enum MembresiaType {
        FREE("Free", "Entrenamientos basicos", 0.0, "#636e72",
             new String[]{"Pecho", "Espalda", "Piernas", "Hombros", "Brazos", "Abdomen"},
             5, "Acceso a rutinas basicas 5 dias a la semana"),

        PRO("Pro", "Nutricion + Calorias", 9.99, "#6c5ce7",
            new String[]{"Pecho", "Espalda", "Piernas", "Hombros", "Brazos", "Abdomen", "Cardio", "Full Body"},
            7, "Todo lo de Free + tracking de comidas y calorias"),

        PREMIUM("Premium", "Entrenador virtual", 19.99, "#e91e63",
                new String[]{"Pecho", "Espalda", "Piernas", "Hombros", "Brazos", "Abdomen", "Cardio", "Full Body"},
                7, "Todo lo de Pro + entrenador virtual personalizado");

        private final String displayName;
        private final String tagline;
        private final double price;
        private final String color;
        private final String[] categoriasDisponibles;
        private final int diasPorSemana;
        private final String descripcion;

        MembresiaType(String displayName, String tagline, double price, String color,
                      String[] categoriasDisponibles, int diasPorSemana, String descripcion) {
            this.displayName = displayName;
            this.tagline = tagline;
            this.price = price;
            this.color = color;
            this.categoriasDisponibles = categoriasDisponibles;
            this.diasPorSemana = diasPorSemana;
            this.descripcion = descripcion;
        }

        public String getDisplayName() { return displayName; }
        public String getTagline() { return tagline; }
        public double getPrice() { return price; }
        public String getColor() { return color; }
        public String[] getCategoriasDisponibles() { return categoriasDisponibles; }
        public int getDiasPorSemana() { return diasPorSemana; }
        public String getDescripcion() { return descripcion; }
    }

    @PrePersist
    protected void onCreate() {
        if (fechaInicio == null) fechaInicio = LocalDate.now();
        activa = true;
    }

    public Membresia() {}

    public Membresia(MembresiaType tipo, User user) {
        this.tipo = tipo;
        this.user = user;
        this.fechaInicio = LocalDate.now();
        if (tipo != MembresiaType.FREE) {
            this.fechaFin = LocalDate.now().plusMonths(1);
        }
        this.diasPorSemana = tipo.getDiasPorSemana();
        this.activa = true;
    }

    public boolean tieneAcceso(MembresiaType requerido) {
        if (!activa) return false;
        return this.tipo.ordinal() >= requerido.ordinal();
    }

    public boolean puedeAccederCategoria(String categoria) {
        for (String cat : tipo.getCategoriasDisponibles()) {
            if (cat.equalsIgnoreCase(categoria)) return true;
        }
        return false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MembresiaType getTipo() { return tipo; }
    public void setTipo(MembresiaType tipo) { this.tipo = tipo; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public Integer getDiasPorSemana() { return diasPorSemana; }
    public void setDiasPorSemana(Integer diasPorSemana) { this.diasPorSemana = diasPorSemana; }
}
