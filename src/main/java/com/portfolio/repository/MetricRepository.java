package com.portfolio.repository;

import com.portfolio.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    
    List<Metric> findByCategory(String category);
    
    List<Metric> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT m FROM Metric m ORDER BY m.timestamp DESC")
    List<Metric> findAllOrderByTimestampDesc();
    
    @Query("SELECT m.category, COUNT(m) FROM Metric m GROUP BY m.category")
    List<Object[]> countByCategory();
    
    @Query("SELECT AVG(m.value) FROM Metric m")
    Double findAverageValue();
}

