# BaitMateWeb - Your Fishing Companion App (Spring-Backend)

> Spring Boot-based backend service for fish catch loging and sharing

> Android App: [BaitMateMobile](https://github.com/Wionerlol/BaitmateMobile)

> Python Backend: [BaitMatePython](https://github.com/HIT-cenhaoyang/BaitMatePython)


## 📌 Project Overview

- **Core Features**: Keep Your Catches Tracked, Shaare your Big Catches
- **Architecture**: Modular design, RESTful APIs, JWT authentication, multi-datasource support
- **Use Cases**: Fishing Catch management, Fish Recognition, Community for Sharing Catches

## 🛠️ Tech Stack

### Core Frameworks
- Spring Boot
- Spring Data JPA
- Restful API

### Databases
- PostGreSQL 

### Other Components
- Lombok
- Hibernate Validator

## 🚀 Quick Start

### Prerequisites
- JDK 21
- PostGreSQL database

### Installation
1. Clone repository:
```bash
git clone https://github.com/Zzzhiye/BaitMateWeb.git
```
2. Create Schema Baitmate in Database
### Starting
#### (Option 1) Start the project in IDEA
#### (Option 2) Using Command Line(Not Verified, please check the path before running)
   ```bash
   mvn clean install
   java -jar target/baitmate-web-1.0.0.jar
   ```

### Cloud Deployment
- Deployed on docker
Using ci-cd.yml inside .github to deploy to the cloud

- or you can use the following command to build a docker image
```bash
docker build -t baitmate-web .
```
Push the image to docker hub
Pull from docker hub and run the image
```bash
docker run -d --name=baitmate-web --network=monitoring -p 8080:8080
````

### Grafana and Prometheus installing
- Install Grafana and Prometheus
1. (RECOMMENDED) Using zip file from Official website
2. Or using docker
```bash
# Pull the Grafana and Prometheus images from Docker Hub
docker pull grafana/grafana
docker pull prom/prometheus

# Create a Docker network
docker network create monitoring

# Run Prometheus container
docker run -d --name=prometheus --network=monitoring -p 9090:9090 prom/prometheus

# Run Grafana container
docker run -d --name=grafana --network=monitoring -p 3000:3000 grafana/grafana

# Access Grafana at http://localhost:3000 and Prometheus at http://localhost:9090
```

- Configuring Prometheus
```yaml
# my global config
global:
  scrape_interval: 15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Alertmanager configuration
alerting:
  alertmanagers:
    - static_configs:
        - targets:
          # - alertmanager:9093

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: "spring-boot"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ["<YOUR_SPRING_BACKEND_LINK>:8080"]
```

- Configuring Grafana
1. Access Grafana at http://localhost:3000 (default login: admin / admin).  
2. Add Prometheus as a data source:  
- Go to Configuration > Data Sources > Add data source.
- Select Prometheus.
- Set the URL to http://prometheus:9090.
- Click Save & Test.
3. Import Grafana dashboards or create your own to visualize the metrics.

### Change links in grafana.html to the link of your grafana