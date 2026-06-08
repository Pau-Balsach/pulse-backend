package com.pulse.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ServiceResponse {
    private String id;
    private String projectId;
    private String name;
    private String url;
    private String method;
    private Integer expectedStatus;
    private Integer timeoutMs;
    private Integer checkIntervalSeconds;
    private Boolean active;
    private LocalDateTime createdAt;
    private String apiKeyHeader;
    private String apiKeyValue;
}