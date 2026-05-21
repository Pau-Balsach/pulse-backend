package com.pulse.metrics.controller;

import com.pulse.metrics.dto.MetricsResponse;
import com.pulse.metrics.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;

    @GetMapping("/api/services/{serviceId}/metrics")
    public ResponseEntity<MetricsResponse> getMetrics(@PathVariable String serviceId) {
        return ResponseEntity.ok(metricsService.getMetrics(serviceId));
    }

    @GetMapping("/api/projects/{projectId}/metrics")
    public ResponseEntity<List<MetricsResponse>> getMetricsByProject(@PathVariable String projectId) {
        return ResponseEntity.ok(metricsService.getMetricsByProject(projectId));
    }
}