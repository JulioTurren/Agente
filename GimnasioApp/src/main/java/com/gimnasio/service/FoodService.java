package com.gimnasio.service;

import com.gimnasio.model.FoodEntry;
import com.gimnasio.model.User;
import com.gimnasio.repository.FoodEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class FoodService {

    private final FoodEntryRepository foodEntryRepository;

    public FoodService(FoodEntryRepository foodEntryRepository) {
        this.foodEntryRepository = foodEntryRepository;
    }

    public List<FoodEntry> getEntriesByDate(Long userId, LocalDate fecha) {
        return foodEntryRepository.findByUserIdAndFechaOrderByHoraDesc(userId, fecha);
    }

    public Integer getCaloriasDelDia(Long userId, LocalDate fecha) {
        return foodEntryRepository.sumCaloriasByUserAndFecha(userId, fecha);
    }

    public Double getProteinasDelDia(Long userId, LocalDate fecha) {
        return foodEntryRepository.sumProteinasByUserAndFecha(userId, fecha);
    }

    public Double getCarbohidratosDelDia(Long userId, LocalDate fecha) {
        return foodEntryRepository.sumCarbohidratosByUserAndFecha(userId, fecha);
    }

    public Double getGrasasDelDia(Long userId, LocalDate fecha) {
        return foodEntryRepository.sumGrasasByUserAndFecha(userId, fecha);
    }

    public Integer getCaloriasDeLaSemana(Long userId) {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
        return foodEntryRepository.sumCaloriasByUserAndRango(userId, inicioSemana, hoy);
    }

    @Transactional
    public FoodEntry addEntry(FoodEntry entry) {
        return foodEntryRepository.save(entry);
    }

    @Transactional
    public void deleteEntry(Long id) {
        foodEntryRepository.deleteById(id);
    }

    public DailySummary getSummary(Long userId) {
        LocalDate hoy = LocalDate.now();
        return new DailySummary(
                getCaloriasDelDia(userId, hoy),
                getProteinasDelDia(userId, hoy),
                getCarbohidratosDelDia(userId, hoy),
                getGrasasDelDia(userId, hoy),
                getCaloriasDeLaSemana(userId)
        );
    }

    public record DailySummary(Integer caloriasTotal, Double proteinas, Double carbohidratos, Double grasas, Integer caloriasSemana) {}
}
