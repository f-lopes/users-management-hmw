# High-level system overview
![High-level system overview](users-management-api/docs/system-overview.png "High-level system overview")


## API

### Build 

Using Docker:
```bash
docker run --rm -it -v ~/.m2:/root/.m2 -v $(pwd):/workspace -w /workspace maven:3.6.3-jdk-11 mvn clean package
```

```bash
./mvnw clean package
```

### Tests

Unit tests:
```bash
./mvnw test
```

Integration tests:
```bash
./mvnw integration-test
```

#### Test code coverage

To enable code coverage, simply enable the `code-coverage` Maven profile:
```bash
[...] -Pcode-coverage
```
```bash
./mvnw clean verify -Pcode-coverage
```

### Code formatting

Code formatting is made using `Spotless` through its Maven plugin: `https://github.com/diffplug/spotless/tree/main/plugin-maven`
It is automatically run at the `process-sources`, given the `dev` Maven profile is enabled (by default).

### Development workflow

To spin up a development environment (`MongoDB` / `RabbitMQ`):
```bash
docker-compose up -d db broker
```

### Run / deploy

#### Hosts setup
Edit your `hosts` file (`/etc/hosts`):

```
127.0.0.1 broker.localhost
127.0.0.1 api.localhost
```

#### Start containers

1. Ensure the `users-management-api` Docker image has been built, run `mvn package` in the `users-management-api` directory, if needed

2.
```bash
docker-compose up -d
```

3. Set the `APP_USER_CREATION_IP_CHECK_ENABLED` environment variable to `false` to disable IP check on user creation

This `docker-compose.yml` file starts 4 containers:
- Traefik proxy
- REST API server
- MongoDB
- RabbitMQ broker

- Access the UI at `http://localhost`

- Access the API documentation at `http://api.localhost/swagger-ui.html`

You can access the RabbitMQ dashboard at `http://broker.localhost`

### API documentation

You can view Swagger-based API documentation at the following URL:
`http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config`

Or `http://api.localhost/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config` if you are running the API through its Docker container.