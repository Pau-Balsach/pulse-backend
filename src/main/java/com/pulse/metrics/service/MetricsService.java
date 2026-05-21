package com.pulse.metrics.service;

import com.pulse.incident.repository.IncidentRepository;
import com.pulse.metrics.dto.MetricsResponse;
import com.pulse.monitor.entity.MonitorCheck;
import com.pulse.monitor.repository.MonitorCheckRepository;
import com.pulse.service.entity.MonitoredService;
import com.pulse.service.repository.MonitoredServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final MonitoredServiceRepository serviceRepository;
    private final MonitorCheckRepository checkRepository;
    private final IncidentRepository incidentRepository;

    public MetricsResponse getMetrics(String serviceId) {
        MonitoredService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        List<MonitorCheck> checks = checkRepository
                .findTop20ByServiceIdOrderByCheckedAtDesc(serviceId);

        long totalChecks = checks.size();
        long totalDown = checks.stream()
                .filter(c -> c.getStatus().equals("DOWN"))
                .count();

        double uptimePercentage = totalChecks == 0 ? 100.0 :
                ((double)(totalChecks - totalDown) / totalChecks) * 100.0;

        double avgResponseTime = checks.stream()
                .filter(c -> c.getResponseTimeMs() != null)
                .mapToLong(MonitorCheck::getResponseTimeMs)
                .average()
                .orElse(0.0);

        double p95 = calculateP95(checks);

        long openIncidents = incidentRepository
                .findTopByServiceIdAndStatusOrderByStartedAtDesc(serviceId, "OPEN")
                .isPresent() ? 1 : 0;

        return new MetricsResponse(
                serviceId,
                service.getName(),
                Math.round(uptimePercentage * 100.0) / 100.0,
                Math.round(avgResponseTime * 100.0) / 100.0,
                Math.round(p95 * 100.0) / 100.0,
                totalChecks,
                totalDown,
                openIncidents
        );
    }

    private double calculateP95(List<MonitorCheck> checks) {
        List<Long> times = checks.stream()
                .filter(c -> c.getResponseTimeMs() != null)
                .map(MonitorCheck::getResponseTimeMs)
                .sorted(Comparator.naturalOrder())
                .toList();

        if (times.isEmpty()) return 0.0;

        int index = (int) Math.ceil(0.95 * times.size()) - 1;
        return times.get(Math.max(0, index));
    }
}