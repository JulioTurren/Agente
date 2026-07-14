package com.gimnasio.repository;

import com.gimnasio.model.FoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    List<FoodEntry> findByUserIdAndFechaOrderByHoraDesc(Long userId, LocalDate fecha);

    @Query("SELECT COALESCE(SUM(f.calorias), 0) FROM FoodEntry f WHERE f.user.id = :userId AND f.fecha = :fecha")
    Integer sumCaloriasByUserAndFecha(@Param("userId") Long userId, @Param("fecha") LocalDate fecha);

    @Query("SELECT COALESCE(SUM(f.calorias), 0) FROM FoodEntry f WHERE f.user.id = :userId AND f.fecha BETWEEN :inicio AND :fin")
    Integer sumCaloriasByUserAndRango(@Param("userId") Long userId, @Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT COALESCE(SUM(f.proteinas), 0.0) FROM FoodEntry f WHERE f.user.id = :userId AND f.fecha = :fecha")
    Double sumProteinasByUserAndFecha(@Param("userId") Long userId, @Param("fecha") LocalDate fecha);

    @Query("SELECT COALESCE(SUM(f.carbohidratos), 0.0) FROM FoodEntry f WHERE f.user.id = :userId AND f.fecha = :fecha")
    Double sumCarbohidratosByUserAndFecha(@Param("userId") Long userId, @Param("fecha") LocalDate fecha);

    @Query("SELECT COALESCE(SUM(f.grasas), 0.0) FROM FoodEntry f WHERE f.user.id = :userId AND f.fecha = :fecha")
    Double sumGrasasByUserAndFecha(@Param("userId") Long userId, @Param("fecha") LocalDate fecha);
}
