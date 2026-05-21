package com.pulse.ssl.checker;

import com.pulse.service.entity.MonitoredService;
import com.pulse.ssl.entity.SslCheck;
import com.pulse.ssl.repository.SslCheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class SslChecker {

    private final SslCheckRepository sslCheckRepository;

    public void check(MonitoredService service) {
        if (!service.getUrl().startsWith("https")) return;

        SslCheck result;
        try {
            URL url = new URL(service.getUrl());
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.connect();

            X509Certificate cert = (X509Certificate) conn.getServerCertificates()[0];
            Date expiry = cert.getNotAfter();
            LocalDateTime expiryDate = expiry.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            long daysRemaining = ChronoUnit.DAYS.between(LocalDateTime.now(), expiryDate);

            result = SslCheck.builder()
                    .serviceId(service.getId())
                    .checkedAt(LocalDateTime.now())
                    .valid(daysRemaining > 0)
                    .daysRemaining((int) daysRemaining)
                    .expiryDate(expiryDate)
                    .build();

            conn.disconnect();

        } catch (Exception e) {
            result = SslCheck.builder()
                    .serviceId(service.getId())
                    .checkedAt(LocalDateTime.now())
                    .valid(false)
                    .daysRemaining(0)
                    .errorMessage(e.getMessage())
                    .build();
        }

        sslCheckRepository.save(result);
        log.info("SSL Check — Service: {} | Valid: {} | Days remaining: {}",
                service.getName(), result.getValid(), result.getDaysRemaining());
    }
}