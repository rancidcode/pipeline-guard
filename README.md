# Pipeline Guard

Event-driven IoT telemetry processing and incident monitoring platform.

## Architecture

```text
IoT Device
    |
    | MQTT
    v
EMQX Cloud
    |
    v
Telemetry Collector
    |
    +---- MQTT Status (Webhook) ----+
    |                               |
    v                               |
Apache Kafka                        |
    |                               |
    +---- telemetry.raw ------------+
    |          |                    |
    |          v                    |
    |    Telemetry Aggregator       |
    |      (Kafka Streams)          |
    |          |                    |
    |      +---+---+                |
    |      |       |                |
    |      v       v                |
    |    avg.1m  avg.2m             |
    |      |       |                |
    |      +---+---+                |
    |          |                    |
    |          +--------------------+
    |                               |
    +---- telemetry.dlq ------------+
                                    |
                                    v
                            Incident Engine
                                    |
                         - Monitor raw + 1m/2m telemetry
                         - Monitor Kafka & MQTT connectivity
                         - Detect device offline & invalid data
                         - Manage incident lifecycle
                         - Persist telemetry & incidents
                                    |
                                    v
                               PostgreSQL
```

## Applications

**Telemetry Collector**  
Receives and validates MQTT telemetry, publishes events to Kafka, routes invalid data to DLQ, and reports MQTT connection status via webhook.

**Telemetry Aggregator**  
Processes raw telemetry using Kafka Streams and produces 1-minute and 2-minute telemetry aggregations.

**Incident Engine**  
Monitors raw and aggregated telemetry, Kafka, MQTT, devices, and DLQ events. Manages incidents and persists telemetry and incident data to PostgreSQL.

## Tech Stack

Java 17 · Spring Boot · Apache Kafka 4.0 (KRaft) · Kafka Streams · MQTT · PostgreSQL · Docker · Docker Compose · AWS EC2