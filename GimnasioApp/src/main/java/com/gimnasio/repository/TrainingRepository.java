package com.gimnasio.repository;

import com.gimnasio.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByUserIdOrderByDateCreatedDesc(Long userId);
    List<Training> findByUserIdAndCategoryOrderByDateCreatedDesc(Long userId, Training.TrainingCategory category);

    @Query("SELECT t FROM Training t WHERE t.user.id = :userId ORDER BY t.dateCreated DESC")
    List<Training> findRecentTrainings(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Training t WHERE t.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
}
