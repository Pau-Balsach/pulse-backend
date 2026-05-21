package com.pulse.publicstatus.service;

import com.pulse.incident.repository.IncidentRepository;
import com.pulse.monitor.entity.MonitorCheck;
import com.pulse.monitor.repository.MonitorCheckRepository;
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
    private final MonitorCheckRepository checkRepository;
    private final IncidentRepository incidentRepository;

    public PublicStatusResponse getStatus(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<MonitoredService> services = serviceRepository.findByProjectId(projectId);

        List<PublicServiceStatus> statusList = services.stream().map(service -> {
            List<MonitorCheck> checks = checkRepository
                    .findTop20ByServiceIdOrderByCheckedAtDesc(service.getId());

            long total = checks.size();
            long down = checks.stream().filter(c -> c.getStatus().equals("DOWN")).count();
            double uptime = total == 0 ? 100.0 : ((double)(total - down) / total) * 100.0;
            double avg = checks.stream()
                    .filter(c -> c.getResponseTimeMs() != null)
                    .mapToLong(MonitorCheck::getResponseTimeMs)
                    .average().orElse(0.0);

            boolean hasOpenIncident = incidentRepository
                    .findTopByServiceIdAndStatusOrderByStartedAtDesc(service.getId(), "OPEN")
                    .isPresent();

            String status = hasOpenIncident ? "OUTAGE" : uptime >= 95 ? "OPERATIONAL" : "DEGRADED";

            return new PublicServiceStatus(
                    service.getName(),
                    service.getUrl(),
                    status,
                    Math.round(uptime * 100.0) / 100.0,
                    Math.round(avg * 100.0) / 100.0
            );
        }).toList();

        long outages = statusList.stream().filter(s -> s.getStatus().equals("OUTAGE")).count();
        long degraded = statusList.stream().filter(s -> s.getStatus().equals("DEGRADED")).count();
        String overall = outages > 0 ? "OUTAGE" : degraded > 0 ? "DEGRADED" : "OPERATIONAL";

        return new PublicStatusResponse(project.getName(), overall, statusList);
    }
}