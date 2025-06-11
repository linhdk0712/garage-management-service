# Health Check Configuration

This document describes the health check configuration for the Garage Management Service using Spring Boot Actuator.

## Configuration

The health checks are configured in `application.yml` with the following settings:

### Actuator Endpoints
- **Base Path**: `/actuator`
- **Exposed Endpoints**: `health`, `info`, `metrics`, `prometheus`
- **Health Details**: Shown when authorized
- **Components**: Always shown

### Health Indicators Enabled
- **Database Health**: Automatically checks database connectivity
- **Disk Space Health**: Monitors available disk space
- **Liveness State**: Kubernetes liveness probe support
- **Readiness State**: Kubernetes readiness probe support

## Available Health Check Endpoints

### 1. Basic Health Check
```
GET /actuator/health
```
Returns the overall health status of the application.

### 2. Detailed Health Check
```
GET /actuator/health/{component}
```
Returns detailed health information for specific components:
- `/actuator/health/db` - Database health
- `/actuator/health/diskspace` - Disk space health
- `/actuator/health/livenessState` - Liveness state
- `/actuator/health/readinessState` - Readiness state

### 3. Application Info
```
GET /actuator/info
```
Returns application information.

### 4. Metrics
```
GET /actuator/metrics
```
Returns application metrics.

### 5. Prometheus Metrics
```
GET /actuator/prometheus
```
Returns metrics in Prometheus format.

## Health Status Responses

### UP Status
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskspace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 419430400000,
        "threshold": 10485760
      }
    }
  }
}
```

### DOWN Status
```json
{
  "status": "DOWN",
  "components": {
    "db": {
      "status": "DOWN",
      "details": {
        "error": "Connection refused"
      }
    }
  }
}
```

## Security Configuration

Actuator endpoints are configured to be accessible without authentication:
- `/actuator/**`
- `/actuator/health/**`
- `/actuator/info/**`
- `/actuator/metrics/**`

## Usage Examples

### Check Application Health
```bash
curl http://localhost:8080/actuator/health
```

### Check Database Health
```bash
curl http://localhost:8080/actuator/health/db
```

### Check Disk Space
```bash
curl http://localhost:8080/actuator/health/diskspace
```

## Monitoring Integration

The health check endpoints can be integrated with:
- **Kubernetes**: Use `/actuator/health/liveness` and `/actuator/health/readiness` for probes
- **Prometheus**: Use `/actuator/prometheus` for metrics collection
- **Load Balancers**: Use `/actuator/health` for health checks
- **Monitoring Tools**: Use various endpoints for comprehensive monitoring

## Troubleshooting

If health checks are not working:

1. **Check Application Logs**: Look for actuator-related errors
2. **Verify Dependencies**: Ensure `spring-boot-starter-actuator` is in `build.gradle`
3. **Check Configuration**: Verify `application.yml` actuator configuration
4. **Test Endpoints**: Use curl or browser to test individual endpoints
5. **Check Security**: Ensure actuator endpoints are not blocked by security configuration 