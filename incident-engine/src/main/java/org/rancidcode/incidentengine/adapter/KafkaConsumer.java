package org.rancidcode.incidentengine.adapter;

import lombok.extern.slf4j.Slf4j;
import org.rancidcode.incidentengine.domain.DataTask;
import org.rancidcode.incidentengine.dto.Dlq;
import org.rancidcode.incidentengine.dto.Telemetry;
import org.rancidcode.incidentengine.dto.TelemetryAggregate;
import org.rancidcode.incidentengine.infra.db.AggregateTable;
import org.rancidcode.incidentengine.infra.db.DlqTable;
import org.rancidcode.incidentengine.infra.db.TelemetryTable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.rancidcode.incidentengine.infra.db.DataSchema.MAPPER;

@Service
@Slf4j
public class KafkaConsumer {

    private final JdbcTemplate jdbcTemplate;
    private final DataTask dataTask = new DataTask();


    public KafkaConsumer(@Qualifier("telemetryDataSource") DataSource telemetryDataSource) {
        this.jdbcTemplate = new JdbcTemplate(telemetryDataSource);
    }

    @KafkaListener(topics = "${kafka.topic.1m}", groupId = "${kafka.group.avg}")
    public void processAvg(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Topic : {}, message : {}", topic, message);

        dataTask.insertData(jdbcTemplate, AggregateTable.TABLE, extractAgg(message, "1m"));
    }

    @KafkaListener(topics = "${kafka.topic.dlq}", groupId = "${kafka.group.dlq}")
    public void processDlq(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Topic : {}, message : {}", topic, message);

        dataTask.insertData(jdbcTemplate, DlqTable.TABLE, extractDlq(message));
    }

    @KafkaListener(topics = "${kafka.topic.raw}", groupId = "${kafka.group.raw}")
    public void processRaw(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Topic : {}, message : {}", topic, message);

        dataTask.insertData(jdbcTemplate, TelemetryTable.TABLE, extractTelemetry(message));
    }

    private Map<String, Object> extractTelemetry(String rawMessage) {
        // id | device_id | temperature | heat_index | humidity | event_time

        try {
            Telemetry telemetry = MAPPER.readValue(rawMessage, Telemetry.class);

            Map<String, Object> values = new LinkedHashMap<>();
            values.put(TelemetryTable.COL_DEVICE_ID, telemetry.getDeviceId());
            values.put(TelemetryTable.COL_TEMPERATURE, telemetry.getTemperature());
            values.put(TelemetryTable.COL_HEAT_INDEX, telemetry.getHeatIndex());
            values.put(TelemetryTable.COL_HUMIDITY, telemetry.getHumidity());
            values.put(TelemetryTable.COL_TIMESTAMP, Timestamp.from(telemetry.getTimestamp()));

            log.info(MAPPER.writeValueAsString(values));
            return values;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract telemetry", e);
        }
    }

    private Map<String, Object> extractDlq(String dlqMessage) {
    /*
    {
      "timestamp": "2026-01-08T11:00:00Z",
      "dlqType": "INVALID_JSON",
      "source": "MQTT",
      "rawMessage": "{bad json payload}",
      "errorMessage": "Unexpected character ..."
    }
    */

        try {
            Dlq dlq = MAPPER.readValue(dlqMessage, Dlq.class);

            Map<String, Object> values = new LinkedHashMap<>();
            values.put(DlqTable.COL_DLQ_TYPE, dlq.getDlqType());
            values.put(DlqTable.COL_SOURCE, dlq.getSource());
            values.put(DlqTable.COL_RAW_MESSAGE, dlq.getRawMessage());
            values.put(DlqTable.COL_ERROR_MESSAGE, dlq.getErrorMessage());
            values.put(DlqTable.COL_EVENT_TIME, Timestamp.from(dlq.getTimestamp()));

            log.info(MAPPER.writeValueAsString(values));
            return values;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract DLQ message", e);
        }
    }

    private Map<String, Object> extractAgg(String aggMessage, String window) {
    /*
    {
  "count": 1,
  "sumTemperature": 33.0,
  "sumHumidity": 75.1,
  "sumHeatIndex": 45.76314,

  "startTime": "2026-06-15T13:36:07Z",
  "endTime": "2026-06-15T13:36:07Z",

  "avgTemperature": 33.0,
  "avgHeatIndex": 45.76314,
  "avgHumidity": 75.1
}
     */

        try {
            TelemetryAggregate agg = MAPPER.readValue(aggMessage, TelemetryAggregate.class);

            Map<String, Object> values = new LinkedHashMap<>();
            values.put(AggregateTable.COL_COUNT, agg.getCount());
            values.put(AggregateTable.COL_SUM_TEMPERATURE, agg.getSumTemperature());
            values.put(AggregateTable.COL_SUM_HEAT_INDEX, agg.getSumHeatIndex());
            values.put(AggregateTable.COL_SUM_HUMIDITY, agg.getSumHumidity());
            values.put(AggregateTable.COL_START, Timestamp.from(agg.getStartTime()));
            values.put(AggregateTable.COL_END, Timestamp.from(agg.getEndTime()));
            values.put(AggregateTable.COL_AVG_TEMPERATURE, agg.getAvgTemperature());
            values.put(AggregateTable.COL_AVG_HEAT_INDEX, agg.getAvgHeatIndex());
            values.put(AggregateTable.COL_AVG_HUMIDITY, agg.getAvgHumidity());
            values.put(AggregateTable.COL_AGG_WINDOW, window);

            log.info(MAPPER.writeValueAsString(values));
            return values;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract Aggregate message", e);
        }
    }
}