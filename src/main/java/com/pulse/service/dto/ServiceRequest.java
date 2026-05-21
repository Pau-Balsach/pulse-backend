package com.pulse.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ServiceRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String url;

    private String method = "GET";
    private Integer expectedStatus = 200;
    private Integer timeoutMs = 5000;
    private Integer checkIntervalSeconds = 300;
}