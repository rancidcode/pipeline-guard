# Pipeline Guard

Pipeline Guard is an event-driven IoT telemetry processing platform built with Spring Boot, Apache Kafka, Docker, and AWS.

## Overview

The system collects telemetry data from IoT devices through MQTT, validates incoming messages, and publishes them to Kafka for downstream processing.

### Current Architecture

```text
Device
  ↓
EMQX Cloud
  ↓
Telemetry Collector
  ↓
Apache Kafka
```

## Tech Stack

* Java 17
* Spring Boot
* Apache Kafka (KRaft Mode)
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
* Provide reliable event streaming for downstream services

## Deployment

Current deployment environment:

* AWS EC2 (Ubuntu)
* Docker Compose

Running services:

* telemetry-collector
* kafka

## Progress

### Completed

* MQTT integration with EMQX Cloud
* Telemetry ingestion pipeline
* Kafka producer integration
* Docker containerization
* Docker Compose orchestration
* EC2 deployment
* End-to-end telemetry validation

### In Progress

* Telemetry Aggregator Service

### Planned

* PostgreSQL integration
* Incident Service
* Alerting workflow
* Monitoring and observability

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
* Apache Kafka
* Spring Boot microservices
* Docker-based deployment
* Cloud infrastructure on AWS

## Author

Personal backend engineering project focused on building production-style data pipelines and cloud-native services.
