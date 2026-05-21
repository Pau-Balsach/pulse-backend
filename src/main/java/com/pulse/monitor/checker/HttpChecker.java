package com.pulse.monitor.checker;

import com.pulse.monitor.entity.MonitorCheck;
import com.pulse.service.entity.MonitoredService;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class HttpChecker {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public MonitorCheck check(MonitoredService service) {
        long start = System.currentTimeMillis();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(service.getUrl()))
                    .method(service.getMethod(), HttpRequest.BodyPublishers.noBody())
                    .timeout(Duration.ofMillis(service.getTimeoutMs()))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            long responseTime = System.currentTimeMillis() - start;
            boolean isUp = response.statusCode() == service.getExpectedStatus();

            return MonitorCheck.builder()
                    .serviceId(service.getId())
                    .status(isUp ? "UP" : "DOWN")
                    .responseTimeMs(responseTime)
                    .httpStatus(response.statusCode())
                    .build();

        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - start;
            return MonitorCheck.builder()
                    .serviceId(service.getId())
                    .status("DOWN")
                    .responseTimeMs(responseTime)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
}