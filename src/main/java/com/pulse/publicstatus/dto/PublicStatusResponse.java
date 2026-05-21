package com.pulse.publicstatus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PublicStatusResponse {
    private String projectName;
    private String overallStatus;
    private List<PublicServiceStatus> services;
}