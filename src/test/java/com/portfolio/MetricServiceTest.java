package com.portfolio;

import com.portfolio.model.Metric;
import com.portfolio.repository.MetricRepository;
import com.portfolio.service.MetricService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricServiceTest {
    
    @Mock
    private MetricRepository metricRepository;
    
    @InjectMocks
    private MetricService metricService;
    
    private Metric testMetric;
    
    @BeforeEach
    void setUp() {
        testMetric = new Metric();
        testMetric.setId(1L);
        testMetric.setName("Test Metric");
        testMetric.setCategory("Vendas");
        testMetric.setValue(100.0);
        testMetric.setTimestamp(LocalDateTime.now());
    }
    
    @Test
    void testCreateMetric() {
        when(metricRepository.save(any(Metric.class))).thenReturn(testMetric);
        
        Metric created = metricService.createMetric(testMetric);
        
        assertNotNull(created);
        assertEquals(testMetric.getName(), created.getName());
        verify(metricRepository, times(1)).save(any(Metric.class));
    }
    
    @Test
    void testGetAllMetrics() {
        List<Metric> metrics = Arrays.asList(testMetric);
        when(metricRepository.findAllOrderByTimestampDesc()).thenReturn(metrics);
        
        List<Metric> result = metricService.getAllMetrics();
        
        assertEquals(1, result.size());
        verify(metricRepository, times(1)).findAllOrderByTimestampDesc();
    }
    
    @Test
    void testGetMetricById() {
        when(metricRepository.findById(1L)).thenReturn(Optional.of(testMetric));
        
        Optional<Metric> result = metricService.getMetricById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(testMetric.getId(), result.get().getId());
    }
    
    @Test
    void testDeleteMetric() {
        doNothing().when(metricRepository).deleteById(1L);
        
        metricService.deleteMetric(1L);
        
        verify(metricRepository, times(1)).deleteById(1L);
    }
}

