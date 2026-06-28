package org.rancidcode.incidentengine.infra.db;

public class AggregateTable {

    public static final String TABLE = "telemetry-aggregate";

    public static final String COL_ID = "id";
    public static final String COL_AGG_WINDOW = "aggregation_window";
    public static final String COL_START = "start_time";
    public static final String COL_END = "end_time";
    public static final String COL_COUNT = "count";
    public static final String COL_AVG_TEMPERATURE = "avg_temperature";
    public static final String COL_AVG_HEAT_INDEX = "avg_heat_index";
    public static final String COL_AVG_HUMIDITY = "avg_humidity";
    public static final String COL_SUM_TEMPERATURE = "sum_temperature";
    public static final String COL_SUM_HEAT_INDEX = "sum_heat_index";
    public static final String COL_SUM_HUMIDITY = "sum_humidity";
    public static final String COL_TIMESTAMP = "timestamp";
}
/*

CREATE TABLE telemetry_aggregate (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    aggregation_window VARCHAR(10) NOT NULL,

    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,

    count INTEGER NOT NULL,

    avg_temperature DOUBLE PRECISION NOT NULL,
    avg_heat_index DOUBLE PRECISION NOT NULL,
    avg_humidity DOUBLE PRECISION NOT NULL,

    sum_temperature DOUBLE PRECISION NOT NULL,
    sum_heat_index DOUBLE PRECISION NOT NULL,
    sum_humidity DOUBLE PRECISION NOT NULL,

    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
 */