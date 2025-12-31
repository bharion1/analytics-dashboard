package com.portfolio.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metric {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "metric_value", nullable = false)
    private Double value;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column
    private String description;
    
    @Column
    private String color; // Para visualização no frontend
    
    public Metric(String category, String name, Double value, String description, String color) {
        this.category = category;
        this.name = name;
        this.value = value;
        this.description = description;
        this.color = color;
        this.timestamp = LocalDateTime.now();
    }
}

