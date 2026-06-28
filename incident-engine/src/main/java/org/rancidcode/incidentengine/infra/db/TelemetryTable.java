package org.rancidcode.incidentengine.infra.db;

public final class TelemetryTable {
    public static final String TABLE = "telemetry";

    public static final String COL_ID = "id";
    public static final String COL_DEVICE_ID = "device_id";
    public static final String COL_TEMPERATURE = "temperature";
    public static final String COL_HEAT_INDEX = "heat_index";
    public static final String COL_HUMIDITY = "humidity";
    public static final String COL_TIMESTAMP = "timestamp";
}

/*
CREATE TABLE telemetry (
    id BIGSERIAL PRIMARY KEY,
    device_id VARCHAR(50) NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    heat_index DOUBLE PRECISION NOT NULL,
    humidity DOUBLE PRECISION NOT NULL,
    "timestamp" TIMESTAMPTZ NOT NULL
);
 */