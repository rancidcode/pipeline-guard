package org.rancidcode.telemetrycollector.infra;

import lombok.extern.slf4j.Slf4j;
import org.rancidcode.telemetrycollector.dto.MqttStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class MqttStatusPublisher {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value(value = "${spring.application.name}")
    private String applicationName;

    public void publishMqttStatus(String webhookUrl, String status) {
        MqttStatus event = new MqttStatus();
        event.setEventType("MQTT_STATUS_CHANGED");
        event.setStatus(status);
        event.setEventTime(java.time.Instant.now());
        event.setSource(applicationName);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(webhookUrl, event, Void.class);
            log.info("MQTT webhook response: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("Webhook failed: " + e.getMessage());
        }
    }
}

