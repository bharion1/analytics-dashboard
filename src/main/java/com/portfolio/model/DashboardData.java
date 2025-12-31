package com.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardData {
    private Long totalMetrics;
    private Double averageValue;
    private Map<String, Long> metricsByCategory;
    private List<MetricSummary> topMetrics;
    private List<TimeSeriesData> timeSeries;
    private Map<String, Double> categoryTotals;
}

