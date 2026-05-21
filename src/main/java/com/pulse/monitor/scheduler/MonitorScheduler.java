package com.pulse.monitor.scheduler;

import com.pulse.incident.service.IncidentService;
import com.pulse.monitor.checker.HttpChecker;
import com.pulse.monitor.entity.MonitorCheck;
import com.pulse.monitor.repository.MonitorCheckRepository;
import com.pulse.service.repository.MonitoredServiceRepository;
import com.pulse.ssl.checker.SslChecker;
import com.pulse.websocket.MonitorWebSocketPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MonitorScheduler {

    private final MonitoredServiceRepository serviceRepository;
    private final MonitorCheckRepository checkRepository;
    private final HttpChecker httpChecker;
    private final IncidentService incidentService;
    private final MonitorWebSocketPublisher wsPublisher;
    private final SslChecker sslChecker;

    @Scheduled(fixedRate = 60000)
    public void checkAllServices() {
        var services = serviceRepository.findByActiveTrue();
        log.info("Running checks for {} services", services.size());

        for (var service : services) {
            try {
                MonitorCheck result = httpChecker.check(service);
                checkRepository.save(result);

                incidentService.handleCheckResult(
                        service.getId(),
                        result.getStatus(),
                        result.getErrorMessage(),
                        service.getName(),
                        service.getUrl()
                );

                wsPublisher.publishCheckResult(
                        service.getId(),
                        result.getStatus(),
                        result.getResponseTimeMs()
                );
                sslChecker.check(service);

                log.info("Service: {} | Status: {} | Response: {}ms | HTTP: {}",
                        service.getName(), result.getStatus(),
                        result.getResponseTimeMs(), result.getHttpStatus());

            } catch (Exception e) {
                log.error("Error checking service {}: {}", service.getName(), e.getMessage());
            }
        }
    }
}