package com.pulse.metrics.controller;

import com.pulse.metrics.dto.MetricsResponse;
import com.pulse.metrics.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/{serviceId}/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;

    @GetMapping
    public ResponseEntity<MetricsResponse> getMetrics(@PathVariable String serviceId) {
        return ResponseEntity.ok(metricsService.getMetrics(serviceId));
    }
}