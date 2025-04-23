# Variables
APP_NAME=springboot-demo
DOCKER_IMAGE=springboot-demo-app
DOCKER_COMPOSE_FILE=docker-compose.yml

# Tell make these are not real files
.PHONY: build docker-build up down logs clean help

# Build Spring Boot JAR
build:
	./gradlew bootJar

# Build Docker Image only when needed
docker-build: build
	docker build -t $(DOCKER_IMAGE) .

# Start services (reuse existing images)
up:
	docker-compose -f $(DOCKER_COMPOSE_FILE) up

# Stop services
down:
	docker-compose -f $(DOCKER_COMPOSE_FILE) down

# Logs
logs:
	docker-compose -f $(DOCKER_COMPOSE_FILE) logs -f

# Clean all
clean:
	docker-compose -f $(DOCKER_COMPOSE_FILE) down --rmi all --volumes --remove-orphans

# Help message
help:
	@echo "Usage:"
	@echo "  make build         -> Build Spring Boot JAR only"
	@echo "  make docker-build  -> Build Docker image (after JAR)"
	@echo "  make up            -> Start using existing image (no build)"
	@echo "  make down          -> Stop containers"
	@echo "  make logs          -> View logs"
	@echo "  make clean         -> Clean everything"
