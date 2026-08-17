package org.rancidcode.incidentengine.job;

import lombok.extern.slf4j.Slf4j;
import org.rancidcode.incidentengine.domain.DataTask;
import org.rancidcode.incidentengine.domain.enums.IncidentType;
import org.rancidcode.incidentengine.infra.db.IncidentTable;
import org.rancidcode.incidentengine.monitoring.pipeline.KafkaConnectivityChecker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.node.ObjectNode;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class IncidentJob {

    //private final JdbcTemplate jdbcTelemetry;
    private final JdbcTemplate jdbcIncident;
    private final KafkaConnectivityChecker kafkaConnectivityChecker;
    private final DataTask dataTask = new DataTask();

    @Value(value = "${spring.application.name}")
    private String applicationName;

    //public IncidentJob(@Qualifier("incidentDataSource") DataSource telemetryDataSource, @Qualifier("incidentDataSource") DataSource incidentDataSource, KafkaConnectivityChecker kafkaConnectivityChecker) {
    public IncidentJob(@Qualifier("incidentDataSource") DataSource incidentDataSource, KafkaConnectivityChecker kafkaConnectivityChecker) {
        this.jdbcIncident = new JdbcTemplate(incidentDataSource);
        //this.jdbcTelemetry = new JdbcTemplate(telemetryDataSource);
        this.kafkaConnectivityChecker = kafkaConnectivityChecker;
    }

    @Scheduled(cron = "${schedulers.incidentChecker.cron}")
    public void scheduleIncidentChecker() {
        log.info("scheduler is running ...");

        //boolean dataStopped = dataTask.dataStopped(jdbcTelemetry);
        boolean dataStopped = dataTask.dataStopped(jdbcIncident);
        boolean isLastIncidentOpen = dataTask.isLastIncidentOpen(jdbcIncident);

        log.info("Data stopped: {}, Last Incident Open: {}", dataStopped, isLastIncidentOpen);

        if (dataStopped && !isLastIncidentOpen) {
            openIncident();
        } else if (!dataStopped && isLastIncidentOpen) {
            closeIncident();
        }
    }

    private void openIncident() {
        String errorType = "";

        boolean isKafkaReachable = kafkaConnectivityChecker.isKafkaReachable();
        boolean isMqttConnected = dataTask.isMqttConnnected(jdbcIncident);
        boolean dlqFound = dataTask.dataDlq(jdbcIncident);

        log.info("Connection details - Kafka: {}, Mqtt: {}, dlq: {}", !isKafkaReachable, !isMqttConnected, dlqFound);

        if (!isKafkaReachable) errorType = IncidentType.KAFKA_DISCONNECTED.name();
        else if (!isMqttConnected) errorType = IncidentType.MQTT_DISCONNECTED.name();
        else if (dlqFound) errorType = IncidentType.INVALID_DATA.name();
        else errorType = IncidentType.DEVICE_OFFLINE.name();
        //not yet completed for DEVICE_ERROR when to write

        log.info("Error found: " + errorType);

        insertIncident(errorType, applicationName, "OPEN", Timestamp.from(Instant.now()), null);
    }

    private void closeIncident() {
        dataTask.closeIncident(jdbcIncident);
    }

    private void insertIncident(String errorType, String source, String status, Timestamp open, Instant closed) {
        //id | error_type | source | status | open_time | close_time
        ObjectNode objectNode = null;
        Map<String, Object> values = null;

        values = new LinkedHashMap<>();
        values.put(IncidentTable.COL_ERROR_TYPE, errorType);
        values.put(IncidentTable.COL_SOURCE, source);
        values.put(IncidentTable.COL_STATUS, status);
        values.put(IncidentTable.COL_OPEN_TIME, open);
        values.put(IncidentTable.COL_CLOSE_TIME, closed);

        dataTask.insertData(jdbcIncident, IncidentTable.TABLE, values);
    }
}