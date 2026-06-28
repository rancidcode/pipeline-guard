package org.rancidcode.incidentengine.dto;

import org.rancidcode.incidentengine.common.HasTimestamp;

import java.time.Instant;

public class TelemetryAggregate implements HasTimestamp {

    private final int id;
    private final String aggregateionWindow;
    private final Instant startTime;
    private final Instant endTime;
    private final int count;
    private final double avgTemperature;
    private final double avgHeatIndex;
    private final double avgHumidity;
    private final double sumTemperature;
    private final double sumHeatIndex;
    private final double sumHumidity;
    private final Instant timestamp;

    public TelemetryAggregate(int id, String aggregateionWindow, Instant timestamp, double sumHumidity, double sumHeatIndex, Instant startTime, double avgTemperature, double avgHeatIndex, double sumTemperature, double avgHumidity, Instant endTime, int count) {
        this.id = id;
        this.aggregateionWindow = aggregateionWindow;
        this.timestamp = timestamp;
        this.sumHumidity = sumHumidity;
        this.sumHeatIndex = sumHeatIndex;
        this.startTime = startTime;
        this.avgTemperature = avgTemperature;
        this.avgHeatIndex = avgHeatIndex;
        this.sumTemperature = sumTemperature;
        this.avgHumidity = avgHumidity;
        this.endTime = endTime;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public String getAggregateionWindow() {
        return aggregateionWindow;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public int getCount() {
        return count;
    }

    public double getAvgTemperature() {
        return avgTemperature;
    }

    public double getAvgHeatIndex() {
        return avgHeatIndex;
    }

    public double getAvgHumidity() {
        return avgHumidity;
    }

    public double getSumTemperature() {
        return sumTemperature;
    }

    public double getSumHeatIndex() {
        return sumHeatIndex;
    }

    public double getSumHumidity() {
        return sumHumidity;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}