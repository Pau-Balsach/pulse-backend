package com.pulse.metrics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetricsResponse {
    private String serviceId;
    private String serviceName;
    private double uptimePercentage;
    private double avgResponseTimeMs;
    private double p95ResponseTimeMs;
    private long totalChecks;
    private long totalDown;
    private long openIncidents;
}