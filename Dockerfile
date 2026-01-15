FROM gcr.io/distroless/java21-debian12

WORKDIR /app

COPY build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8083
CMD ["app.jar"]
