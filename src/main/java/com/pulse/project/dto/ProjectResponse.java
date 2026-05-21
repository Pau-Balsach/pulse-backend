package com.pulse.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProjectResponse {
    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}