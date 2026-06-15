# Pipeline Guard

Pipeline Guard is an event-driven IoT telemetry processing platform built with Spring Boot, Apache Kafka, Docker, and AWS.

## Overview

The system collects telemetry data from IoT devices through MQTT, validates incoming messages, and processes telemetry streams using Apache Kafka and Kafka Streams.

## Current Architecture

```text
Device
  ↓
EMQX Cloud
  ↓
Telemetry Collector
  ↓
Apache Kafka (telemetry.raw)
  ↓
Telemetry Aggregator (Kafka Streams)
  ├── telemetry.avg.1m
  └── telemetry.avg.2m
```

## Tech Stack

* Java 17
* Spring Boot
* Apache Kafka 4.0 (KRaft Mode)
* Kafka Streams
* MQTT (EMQX Cloud)
* Docker
* Docker Compose
* AWS EC2
* GitHub

## Components

### Telemetry Collector

Responsibilities:

* Subscribe to MQTT topics
* Receive telemetry data from devices
* Validate incoming telemetry payloads
* Publish raw telemetry events to Kafka

### Apache Kafka

Responsibilities:

* Store telemetry events
* Decouple producers and consumers
* Provide reliable event streaming between services

### Telemetry Aggregator

Responsibilities:

* Consume telemetry events from `telemetry.raw`
* Perform windowed aggregations using Kafka Streams
* Calculate averages and counts
* Publish aggregated results to:

  * `telemetry.avg.1m`
  * `telemetry.avg.2m`

## Deployment

Current deployment environment:

* AWS EC2 (Ubuntu)
* Docker Compose

Running services:

* telemetry-collector
* telemetry-aggregator
* kafka

## Progress

### Completed

* MQTT integration with EMQX Cloud
* Telemetry ingestion pipeline
* Kafka producer integration
* Kafka consumer integration
* Kafka Streams integration
* 1-minute window aggregation
* 2-minute window aggregation
* RocksDB state stores
* Docker containerization
* Docker Compose orchestration
* Kafka 4.0 KRaft deployment
* EC2 deployment
* End-to-end telemetry processing validation

### In Progress

* PostgreSQL integration
* Aggregated data persistence

### Planned

* Incident Service
* Alerting workflow
* Monitoring and observability
* REST API for querying aggregated metrics

## Future Architecture

```text
Device
  ↓
EMQX Cloud
  ↓
Telemetry Collector
  ↓
Apache Kafka
  ↓
Telemetry Aggregator
  ↓
PostgreSQL
  ↓
Incident Service
```

## Learning Objectives

This project is used to explore:

* Event-driven architecture
* Distributed systems
* Apache Kafka & Kafka Streams
* Stateful stream processing
* Spring Boot microservices
* Docker-based deployment
* Cloud infrastructure on AWS

## Author

Personal backend engineering project focused on building production-style data pipelines and cloud-native services.
