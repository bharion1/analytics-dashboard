package com.portfolio.controller;

import com.portfolio.model.Metric;
import com.portfolio.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MetricController {
    
    private final MetricService metricService;
    
    @PostMapping
    public ResponseEntity<Metric> createMetric(@Valid @RequestBody Metric metric) {
        Metric created = metricService.createMetric(metric);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping
    public ResponseEntity<List<Metric>> getAllMetrics() {
        return ResponseEntity.ok(metricService.getAllMetrics());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Metric> getMetricById(@PathVariable Long id) {
        return metricService.getMetricById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Metric>> getMetricsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(metricService.getMetricsByCategory(category));
    }
    
    @GetMapping("/filter")
    public ResponseEntity<List<Metric>> getFilteredMetrics(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Double minValue,
            @RequestParam(required = false) Double maxValue) {
        return ResponseEntity.ok(metricService.getFilteredMetrics(category, startDate, endDate, minValue, maxValue));
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData() {
        return ResponseEntity.ok(metricService.getDashboardData());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id) {
        metricService.deleteMetric(id);
        return ResponseEntity.noContent().build();
    }
}

