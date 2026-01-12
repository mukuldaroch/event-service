#!/usr/bin/env bash
set -e
# BLUE
b="\e[34m"
# RED
re="\e[31m"
# GREEN
g="\e[32m"
# RESET
r="\e[0m"

echo -e "${b}Building JAR...${r}"
# ./gradlew clean build -e SPRING_PROFILES_ACTIVE=docker
./gradlew clean build

echo -e "${re}Removing old container if exists...${r}"
docker rm -f event-service 2> /dev/null || true
# docker rm -f event-service

echo -e "${b}Building Docker image...${r}"
docker build -t event-service .

echo -e "${b}Starting new container...${r}"

docker run -d \
    --name event-service \
    --network event-mesh \
    --add-host auth.local:host-gateway \
    -p 8083:8083 \
    -e SPRING_DATASOURCE_URL=jdbc:postgresql://event-database:5432/eventdb \
    -e SPRING_DATASOURCE_USERNAME=postgres \
    -e SPRING_DATASOURCE_PASSWORD=daroch \
    -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://auth.local:8080/realms/event-service \
    -e keycloak.client-id=event-api \
    -e keycloak.credentials.secret=g1Js0gl1dbyaxwacTVJiSQSMDIULQjgp \
    event-service

echo -e "${b}Event Service running at http://localhost:8083${r}"
echo -e "${b}Logs: docker logs -f event-service${b}"
