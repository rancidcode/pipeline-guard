\# Pipeline Guard



Pipeline Guard is an IoT telemetry processing platform built with Spring Boot, Apache Kafka, Docker, and AWS.



\## Overview



The system collects telemetry data from IoT devices through MQTT, validates incoming messages, and publishes them to Kafka for downstream processing.



Current architecture:



```text

Device

&#x20; ↓

EMQX Cloud

&#x20; ↓

Telemetry Collector

&#x20; ↓

Apache Kafka

```



\## Tech Stack



\* Java 17

\* Spring Boot

\* Apache Kafka (KRaft Mode)

\* MQTT (EMQX Cloud)

\* Docker

\* Docker Compose

\* AWS EC2



\## Components



\### Telemetry Collector



Responsibilities:



\* Subscribe to MQTT topics

\* Receive telemetry from devices

\* Validate incoming messages

\* Publish raw telemetry events to Kafka



\### Kafka



Responsibilities:



\* Store telemetry events

\* Decouple producers and consumers

\* Serve as the event backbone for the platform



\## Deployment



Current deployment environment:



\* AWS EC2 (Ubuntu)

\* Docker Compose



Services currently deployed:



\* telemetry-collector

\* kafka



\## Current Progress



\### Completed



\* MQTT integration with EMQX Cloud

\* Telemetry ingestion pipeline

\* Kafka producer integration

\* Docker containerization

\* Docker Compose orchestration

\* EC2 deployment

\* End-to-end telemetry validation



\### In Progress



\* Telemetry Aggregator Service



\### Planned



\* PostgreSQL integration

\* Incident Service

\* Alerting workflow

\* Monitoring and observability



\## Future Architecture



```text

Device

&#x20; ↓

EMQX Cloud

&#x20; ↓

Telemetry Collector

&#x20; ↓

Apache Kafka

&#x20; ↓

Telemetry Aggregator

&#x20; ↓

PostgreSQL

&#x20; ↓

Incident Service

```



\## Author



Personal backend engineering learning project focused on distributed systems, event-driven architecture, and cloud deployment.

