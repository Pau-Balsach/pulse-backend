package com.pulse.ssl.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ssl_checks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SslCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    private Boolean valid;

    @Column(name = "days_remaining")
    private Integer daysRemaining;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "error_message")
    private String errorMessage;
}