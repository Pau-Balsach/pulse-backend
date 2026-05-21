package com.pulse.publicstatus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicServiceStatus {
    private String name;
    private String url;
    private String status;
    private double uptimePercentage;
    private double avgResponseTimeMs;
}