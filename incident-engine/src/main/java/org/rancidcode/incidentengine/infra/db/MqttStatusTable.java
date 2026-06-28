package org.rancidcode.incidentengine.infra.db;

public class MqttStatusTable {
    public static final String TABLE = "mqtt_status";

    public static final String COL_ID = "id";
    public static final String COL_EVENT_TYPE = "event_type";
    public static final String COL_STATUS = "status";
    public static final String COL_SOURCE = "source";
    public static final String COL_EVENT_TIME = "event_time";
}

/*
CREATE TABLE mqtt_status (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    source VARCHAR(50) NOT NULL,
    event_time TIMESTAMPTZ NOT NULL
);
 */