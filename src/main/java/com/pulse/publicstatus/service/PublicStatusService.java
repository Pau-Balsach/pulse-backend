package com.pulse.publicstatus.service;

import com.pulse.incident.repository.IncidentRepository;
import com.pulse.metrics.dto.MetricsResponse;
import com.pulse.metrics.service.MetricsService;
import com.pulse.project.entity.Project;
import com.pulse.project.repository.ProjectRepository;
import com.pulse.publicstatus.dto.PublicServiceStatus;
import com.pulse.publicstatus.dto.PublicStatusResponse;
import com.pulse.service.entity.MonitoredService;
import com.pulse.service.repository.MonitoredServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicStatusService {

    private final ProjectRepository projectRepository;
    private final MonitoredServiceRepository serviceRepository;
    private final IncidentRepository incidentRepository;
    private final MetricsService metricsService;

    public PublicStatusResponse getStatus(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<MonitoredService> services = serviceRepository.findByProjectIdOrderByNameAsc(projectId);

        List<PublicServiceStatus> statusList = services.stream().map(service -> {

            MetricsResponse metrics = metricsService.getMetrics(service.getId());

            boolean hasOpenIncident = incidentRepository
                    .findTopByServiceIdAndStatusOrderByStartedAtDesc(service.getId(), "OPEN")
                    .isPresent();

            String status = hasOpenIncident
                    ? "OUTAGE"
                    : metrics.getUptimePercentage() >= 95
                    ? "OPERATIONAL"
                    : "DEGRADED";

            return new PublicServiceStatus(
                    service.getName(),
                    service.getUrl(),
                    status,
                    metrics.getUptimePercentage(),
                    metrics.getAvgResponseTimeMs()
            );
        }).toList();

        long outages = statusList.stream()
                .filter(s -> s.getStatus().equals("OUTAGE"))
                .count();

        long degraded = statusList.stream()
                .filter(s -> s.getStatus().equals("DEGRADED"))
                .count();

        String overall = outages > 0
                ? "OUTAGE"
                : degraded > 0
                ? "DEGRADED"
                : "OPERATIONAL";

        return new PublicStatusResponse(
                project.getName(),
                overall,
                statusList
        );
    }
}