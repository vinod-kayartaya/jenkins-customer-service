# Customer Service Application

A robust Spring Boot application demonstrating a full-stack REST API with robust testing, containerization, and a complete CI/CD pipeline managed by Jenkins.

## 🚀 Tech Stack
- **Java 17** & **Spring Boot 3.2.5**
- **Spring Data JPA** & **Hibernate**
- **MySQL 8** (Production/Local Database)
- **H2 In-Memory Database** (Integration Testing)
- **Lombok** (Boilerplate reduction)
- **Docker & Docker Compose**
- **Jenkins** (CI/CD Pipeline)

---

## 🏗️ Architecture

The application is structured using standard Spring Boot layered architecture:
- **Entity**: `Customer` object representing the database table.
- **DTO**: `CustomerDto` with Jakarta Bean Validation (`@NotBlank`, `@Email`) to ensure data integrity at the boundaries.
- **Repository**: `CustomerRepository` extending `JpaRepository` with custom finder methods (`findByCity`, `findByEmail`).
- **Service**: `CustomerService` interface and `CustomerServiceImpl` for business logic, mapping, and database interaction.
- **Controller**: `CustomerController` exposing RESTful endpoints (`POST`, `GET`, `PUT`, `DELETE`).
- **Exception Handling**: A global `@ControllerAdvice` (`GlobalExceptionHandler`) ensures all errors (404s, 400 Validation errors, 500s) are returned as clean, consistent JSON payloads utilizing custom exceptions like `ResourceNotFoundException`.

---

## 🏃‍♂️ Running Locally

1. **Build the Application**
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Start Docker Compose**
   This spins up the MySQL 8 database (`customer-mysql`) and the application container (`customer-service-app`). It automatically seeds the database with 15 records via `db-scripts/init.sql`.
   ```bash
   docker compose up -d --build
   ```

3. **Test the API**
   ```bash
   curl http://localhost:8080/api/customers
   ```

---

## 🧪 Testing

The project implements comprehensive testing across all layers:
- **Unit Tests**: 
  - `@WebMvcTest` for Controller routing and mock injection.
  - Mockito extensions for testing the Service layer in isolation.
  - `@DataJpaTest` for Repository testing against the H2 database.
- **Integration Tests**: `@SpringBootTest` utilizing an in-memory H2 database via the `test` profile (`application-test.properties`) to run end-to-end verifications.

To run tests locally:
```bash
./mvnw clean test
```

---

## ⚙️ CI/CD Pipeline (Jenkins)

The project includes a robust, multi-stage `Jenkinsfile` designed to run in a Dockerized Jenkins environment.

### Pipeline Stages
1. **Compile**: Compiles the source code.
2. **Test**: Runs the Maven test suite and collects Surefire JUnit reports.
3. **Build**: Packages the `.jar` file and archives it as a Jenkins artifact.
4. **Create Docker Image**: Builds the Dockerfile using the artifact.
5. **Test Docker Container**: 
   - Spins up the container using the `test` Spring profile.
   - Wait for initialization.
   - Executes `wget -qO- http://localhost:8080/api/customers` directly *inside* the container to verify the endpoint is alive, bypassing host/Docker network isolations.
   - Cleans up and stops the container.
6. **Integration Test**: Executes explicit integration tests if defined.
7. **Push Docker Image**: Authenticates using the `docker-learnwithvinod` Jenkins credential ID and pushes the tagged image to Docker Hub (`learnwithvinod/customer-service`).

### 🔧 Running Jenkins as a Docker Container (Steps & Precautions)

When running Jenkins itself as a Docker container, you must configure it to be able to spawn sibling containers and build Docker images. We call this **Docker-in-Docker (or Docker-outside-of-Docker)**.

#### 1. Start Jenkins with Docker Socket Mounted
You cannot use the standard `docker run jenkins` command. You **must** mount the host's Docker socket and optionally the Jenkins data volume:

```bash
docker run -d --name jenkins -p 8080:8080 -p 50000:50000 \
  -v jenkins-data:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts-jdk21
```
*Precaution*: Mounting the Docker socket gives the Jenkins container root-level access to your host machine. Ensure your Jenkins instance is secured.

#### 2. Install the Docker CLI inside Jenkins
The official `jenkins/jenkins` image does not come with the `docker` command. You must install it into the running Jenkins container:

```bash
docker exec -u root jenkins apt-get update
docker exec -u root jenkins apt-get install -y docker.io
```

#### 3. Fix Socket Permissions
By default, the `jenkins` user inside the container does not have permission to read/write the `/var/run/docker.sock` file owned by root. You will see a `permission denied` error during the pipeline build stage. Fix this by opening up the socket permissions:

```bash
docker exec -u root jenkins chmod 666 /var/run/docker.sock
```

#### 4. Networking Isolation Considerations
When Jenkins is a Docker container, `localhost` inside Jenkins refers to the Jenkins container itself, not the Docker host.
- Do **not** use `curl http://localhost:8080` from the Jenkinsfile to test a newly spun up container (it will fail).
- **Solution used in this project**: The pipeline runs `docker exec test-container-${BUILD_NUMBER} wget ...` which executes the test directly *inside* the running app container, completely bypassing the network isolation boundaries!
