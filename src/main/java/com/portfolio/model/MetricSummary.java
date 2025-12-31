package com.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricSummary {
    private String name;
    private Double value;
    private String category;
    private String color;
    private Double percentage;
}

