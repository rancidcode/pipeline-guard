# Pipeline Guard

Event-driven IoT telemetry processing and incident monitoring platform.

## Architecture

```text
                    IoT Device
                        │
                      MQTT
                        ▼
                   EMQX Cloud
                        │
                        ▼
               Telemetry Collector
                 │            │
                 │            └── MQTT Status
                 │                (Webhook)
                 ▼                    │
              Apache Kafka            │
                 │                    │
          ┌──────┴──────┐             │
          ▼             ▼             │
   telemetry.raw   telemetry.dlq      │
          │             │             │
     ┌────┴────┐        │             │
     │         │        │             │
     ▼         │        │             │
  Telemetry    │        │             │
  Aggregator   │        │             │
(Kafka Streams)│        │             │
     │         │        │             │
  ┌──┴──┐      │        │             │
  ▼     ▼      │        │             │
avg.1m avg.2m  │        │             │
  │     │      │        │             │
  └──┬──┘      │        │             │
     │         │        │             │
     └─────────┴────────┴─────────────┘
                        │
                        ▼
          ┌─────────────────────────────┐
          │       Incident Engine       │
          │                             │
          │ • Monitor raw + 1m/2m avg   │
          │ • Monitor Kafka, MQTT,      │
          │   devices & DLQ             │
          │ • Persist telemetry &       │
          │   incidents                 │
          │ • Create & resolve incidents│
          └──────────────┬──────────────┘
                         │
                         ▼
                    PostgreSQL
```

## Applications

**Telemetry Collector**  
Receives and validates MQTT telemetry, publishes events to Kafka, routes invalid data to DLQ, and reports MQTT connection status via webhook.

**Telemetry Aggregator**  
Processes raw telemetry using Kafka Streams and produces 1-minute and 2-minute telemetry aggregations.

**Incident Engine**  
Monitors telemetry and pipeline health, detects operational issues, manages incidents, and persists telemetry and incident data to PostgreSQL.

## Tech Stack

Java 17 · Spring Boot · Apache Kafka 4.0 (KRaft) · Kafka Streams · MQTT · PostgreSQL · Docker · Docker Compose · AWS EC2

## Status

Core telemetry processing and incident monitoring platform completed and deployed on AWS EC2.

**Next:** Monitoring, observability, and alerting.