package com.pulse.incident.service;

import com.pulse.incident.entity.Incident;
import com.pulse.incident.repository.IncidentRepository;
import com.pulse.notification.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final EmailNotificationService emailNotificationService;

    public void handleCheckResult(String serviceId, String status, String errorMessage,
                                  String serviceName, String serviceUrl) {
        if (status.equals("DOWN")) {
            openIncidentIfNotExists(serviceId, errorMessage, serviceName, serviceUrl);
        } else {
            closeIncidentIfExists(serviceId);
        }
    }

    private void openIncidentIfNotExists(String serviceId, String errorMessage,
                                         String serviceName, String serviceUrl) {
        Optional<Incident> existing = incidentRepository
                .findTopByServiceIdAndStatusOrderByStartedAtDesc(serviceId, "OPEN");

        if (existing.isEmpty()) {
            Incident incident = Incident.builder()
                    .serviceId(serviceId)
                    .reason(errorMessage != null ? errorMessage : "Service returned unexpected response")
                    .status("OPEN")
                    .build();

            incidentRepository.save(incident);
            log.warn("Incident OPENED for service: {}", serviceId);

            emailNotificationService.sendIncidentAlert(serviceName, serviceUrl, errorMessage);
        }
    }

    private void closeIncidentIfExists(String serviceId) {
        Optional<Incident> existing = incidentRepository
                .findTopByServiceIdAndStatusOrderByStartedAtDesc(serviceId, "OPEN");

        existing.ifPresent(incident -> {
            LocalDateTime now = LocalDateTime.now();
            long duration = ChronoUnit.SECONDS.between(incident.getStartedAt(), now);

            incident.setResolvedAt(now);
            incident.setDurationSeconds(duration);
            incident.setStatus("RESOLVED");

            incidentRepository.save(incident);
            log.info("Incident RESOLVED for service: {} (duration: {}s)", serviceId, duration);
        });
    }

    public List<Incident> getByServiceId(String serviceId) {
        return incidentRepository.findByServiceIdOrderByStartedAtDesc(serviceId);
    }
}