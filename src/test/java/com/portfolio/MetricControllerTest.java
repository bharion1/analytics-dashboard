package com.portfolio;

import com.portfolio.controller.MetricController;
import com.portfolio.model.Metric;
import com.portfolio.service.MetricService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricControllerTest {
    
    @Mock
    private MetricService metricService;
    
    @InjectMocks
    private MetricController metricController;
    
    @Test
    void testCreateMetric() {
        Metric metric = new Metric();
        metric.setName("Test");
        metric.setCategory("Vendas");
        metric.setValue(100.0);
        
        when(metricService.createMetric(any(Metric.class))).thenReturn(metric);
        
        ResponseEntity<Metric> response = metricController.createMetric(metric);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(metricService, times(1)).createMetric(any(Metric.class));
    }
    
    @Test
    void testGetAllMetrics() {
        List<Metric> metrics = Arrays.asList(new Metric());
        when(metricService.getAllMetrics()).thenReturn(metrics);
        
        ResponseEntity<List<Metric>> response = metricController.getAllMetrics();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
    
    @Test
    void testGetMetricById() {
        Metric metric = new Metric();
        metric.setId(1L);
        when(metricService.getMetricById(1L)).thenReturn(Optional.of(metric));
        
        ResponseEntity<Metric> response = metricController.getMetricById(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}

