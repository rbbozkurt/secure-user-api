# Variables
APP_NAME=springboot-demo
DOCKER_IMAGE=springboot-demo
DOCKER_COMPOSE_FILE=docker-compose.yml

# Tell make that these are phony targets (always run them)
.PHONY: build docker-build up down logs clean help

# Targets

# Build Spring Boot JAR
build:
	./gradlew clean bootJar

# Build Docker Image
docker-build: build
	docker build -t $(DOCKER_IMAGE) .

# Start using docker-compose
up:
	docker-compose -f $(DOCKER_COMPOSE_FILE) up --build

# Stop containers
down:
	docker-compose -f $(DOCKER_COMPOSE_FILE) down

# View logs
logs:
	docker-compose -f $(DOCKER_COMPOSE_FILE) logs -f

# Clean everything (containers + images)
clean:
	docker-compose -f $(DOCKER_COMPOSE_FILE) down --rmi all --volumes --remove-orphans

# Help (default target)
help:
	@echo "Usage:"
	@echo "  make build          -> Build Spring Boot JAR"
	@echo "  make docker-build   -> Build Docker image (after building JAR)"
	@echo "  make up             -> Start services with docker-compose"
	@echo "  make down           -> Stop services"
	@echo "  make logs           -> Tail the logs"
	@echo "  make clean          -> Full cleanup (containers, images, volumes)"
