package com.pulse.service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitored_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoredService {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String projectId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(nullable = true)
    private String apiKeyHeader;
    @Column(nullable = true)
    private String apiKeyValue;

    @Column(nullable = false)
    private String method;

    private Integer expectedStatus;
    private Integer timeoutMs;
    private Integer checkIntervalSeconds;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.active == null) this.active = true;
        if (this.method == null) this.method = "GET";
        if (this.expectedStatus == null) this.expectedStatus = 200;
        if (this.timeoutMs == null) this.timeoutMs = 5000;
        if (this.checkIntervalSeconds == null) this.checkIntervalSeconds = 300;
    }
}