# Automated	Root Cause Analysis	Platform

> The **RCA Platform** integrates Nagios monitoring with Kafka message streaming and SNMP trap processing to identify, suppress, and analyze network or system alarms automatically.  
This system reduces human effort in troubleshooting and accelerates root cause identification.

---

## Overview

The **RCA Dashboard** project integrates system monitoring, message streaming through three key components:

1. **Nagios** – monitors systems, services, and network resources.
2. **Apache Kafka** – acts as a message broker to transmit alert data from Nagios.
3. **Spring Boot RCA Dashboard** – a web-based dashboard that gives the alerts and helps with Root Cause Analysis.

---

## Architecture Workflow

<img width="1024" height="1536" alt="rca-architecture" src="https://github.com/user-attachments/assets/a2f312ae-d834-44a3-98c3-f8a95f849485" />

---

## Step-by-Step Setup

### Step 1: Environment Preparation
Set up a Linux-based environment (e.g., Ubuntu) with required dependencies such as Apache, PHP, and Java. These components are prerequisites for Nagios, Kafka, and the Spring Boot application.

### Step 2: Nagios Installation
Install and configure **Nagios Core**, the monitoring tool that tracks system and service statuses. Once installed, verify the Nagios web interface is running.
<img width="1132" height="1008" alt="Screenshot 2025-10-07 131621" src="https://github.com/user-attachments/assets/51ab948c-98dd-4da9-a3f8-e8a964f90fec" />

### Step 3: Configure SNMP and SNMPTT
Install and configure SNMP agents and **SNMPTT (SNMP Trap Translator)** to capture and translate SNMP traps into a format Nagios understands. This enables device-level monitoring and automated notifications.

### Step 4: Kafka Setup
Set up **Apache Kafka** and **Zookeeper**. Create a Kafka topic (e.g., `alarms`) that will receive alert messages from Nagios. Kafka acts as the intermediary for streaming alert data.

### Step 5: Nagios Event Handler Integration
Create and configure a **custom Nagios event handler script** that sends alerts to Kafka. When Nagios detects an issue, the event handler will automatically forward the alert details to the Kafka topic in JSON format.

### Step 6: RCA Dashboard (Spring Boot Application)
Build and configure the **Spring Boot-based RCA Dashboard**. This application consumes Kafka messages, processes alert data, stores it in a database, and presents it in a web interface for analysis.

### Step 7: System Testing
Generate test alerts from Nagios and verify that they are received by Kafka and displayed in the RCA Dashboard. This confirms that the message flow between Nagios → Kafka → RCA Dashboard is working.

### Step 8: Visualization and RCA
Use the dashboard to visualize alerts, identify root causes, and track the health of monitored systems.
<img width="852" height="634" alt="Screenshot 2025-11-05 071249" src="https://github.com/user-attachments/assets/e42b9d08-b125-4bb3-960d-c1b464dabe9e" />

---
## Summary
The Automated Root Cause Analysis (RCA) Platform helps monitor systems and fix problems faster. It connects Nagios, Kafka, and SNMP to collect and manage alerts automatically.
The system finds the main cause of problems and shows it on a simple dashboard, helping users fix issues faster and with less effort.






