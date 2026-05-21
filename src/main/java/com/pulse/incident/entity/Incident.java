package com.pulse.incident.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String serviceId;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime resolvedAt;
    private Long durationSeconds;
    private String reason;

    @Column(nullable = false)
    private String status;

    @PrePersist
    protected void onCreate() {
        if (this.startedAt == null) this.startedAt = LocalDateTime.now();
        if (this.status == null) this.status = "OPEN";
    }
}