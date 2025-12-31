package com.portfolio.service;

import com.portfolio.model.*;
import com.portfolio.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricService {
    
    private final MetricRepository metricRepository;
    
    @Transactional
    public Metric createMetric(Metric metric) {
        if (metric.getTimestamp() == null) {
            metric.setTimestamp(LocalDateTime.now());
        }
        return metricRepository.save(metric);
    }
    
    public List<Metric> getAllMetrics() {
        return metricRepository.findAllOrderByTimestampDesc();
    }
    
    public List<Metric> getMetricsByCategory(String category) {
        return metricRepository.findByCategory(category);
    }
    
    public List<Metric> getMetricsByDateRange(LocalDateTime start, LocalDateTime end) {
        return metricRepository.findByTimestampBetween(start, end);
    }
    
    public DashboardData getDashboardData() {
        List<Metric> allMetrics = metricRepository.findAll();
        
        if (allMetrics.isEmpty()) {
            return new DashboardData(0L, 0.0, new HashMap<>(), new ArrayList<>(), 
                                   new ArrayList<>(), new HashMap<>());
        }
        
        // Total de métricas
        Long totalMetrics = (long) allMetrics.size();
        
        // Média dos valores
        Double averageValue = metricRepository.findAverageValue();
        
        // Métricas por categoria
        Map<String, Long> metricsByCategory = allMetrics.stream()
            .collect(Collectors.groupingBy(
                Metric::getCategory,
                Collectors.counting()
            ));
        
        // Top 5 métricas
        List<MetricSummary> topMetrics = allMetrics.stream()
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .limit(5)
            .map(m -> {
                double percentage = (m.getValue() / allMetrics.stream()
                    .mapToDouble(Metric::getValue)
                    .max()
                    .orElse(1.0)) * 100;
                return new MetricSummary(
                    m.getName(),
                    m.getValue(),
                    m.getCategory(),
                    m.getColor() != null ? m.getColor() : "#4A90E2",
                    percentage
                );
            })
            .collect(Collectors.toList());
        
        // Dados de série temporal (últimas 24 horas)
        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        List<Metric> recentMetrics = metricRepository.findByTimestampBetween(
            last24Hours, LocalDateTime.now()
        );
        
        List<TimeSeriesData> timeSeries = recentMetrics.stream()
            .map(m -> new TimeSeriesData(m.getTimestamp(), m.getValue(), m.getCategory()))
            .sorted(Comparator.comparing(TimeSeriesData::getTimestamp))
            .collect(Collectors.toList());
        
        // Totais por categoria
        Map<String, Double> categoryTotals = allMetrics.stream()
            .collect(Collectors.groupingBy(
                Metric::getCategory,
                Collectors.summingDouble(Metric::getValue)
            ));
        
        return new DashboardData(
            totalMetrics,
            averageValue,
            metricsByCategory,
            topMetrics,
            timeSeries,
            categoryTotals
        );
    }
    
    @Transactional
    public void deleteMetric(Long id) {
        metricRepository.deleteById(id);
    }
    
    public Optional<Metric> getMetricById(Long id) {
        return metricRepository.findById(id);
    }
    
    public List<Metric> getFilteredMetrics(String category, String startDate, String endDate, 
                                          Double minValue, Double maxValue) {
        List<Metric> metrics = getAllMetrics();
        
        return metrics.stream()
            .filter(m -> category == null || m.getCategory().equalsIgnoreCase(category))
            .filter(m -> {
                if (startDate == null && endDate == null) return true;
                LocalDateTime metricDate = m.getTimestamp();
                if (startDate != null && metricDate.isBefore(LocalDateTime.parse(startDate))) return false;
                if (endDate != null && metricDate.isAfter(LocalDateTime.parse(endDate))) return false;
                return true;
            })
            .filter(m -> minValue == null || m.getValue() >= minValue)
            .filter(m -> maxValue == null || m.getValue() <= maxValue)
            .collect(Collectors.toList());
    }
}

