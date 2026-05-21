package com.pulse.monitor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitor_checks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitorCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String serviceId;

    @Column(nullable = false)
    private String status;

    private Long responseTimeMs;
    private Integer httpStatus;
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime checkedAt;

    @PrePersist
    protected void onCreate() {
        this.checkedAt = LocalDateTime.now();
    }
}