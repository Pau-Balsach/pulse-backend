package com.pulse.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MonitorWebSocketPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishCheckResult(String serviceId, String status, Long responseTimeMs) {
        messagingTemplate.convertAndSend("/topic/checks", Map.of(
                "serviceId", serviceId,
                "status", status,
                "responseTimeMs", responseTimeMs != null ? responseTimeMs : 0
        ));
    }
}