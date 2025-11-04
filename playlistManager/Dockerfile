# ---- build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# copy only the files needed to resolve deps first (better layer caching)
COPY playlistManager/pom.xml .
COPY playlistManager/.mvn .mvn
COPY playlistManager/mvnw ./
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

# now copy sources and build
COPY playlistManager/src ./src
RUN ./mvnw -q -DskipTests clean package

# ---- run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# copy the built jar from the builder
COPY --from=build /app/target/playlistManager-0.0.1-SNAPSHOT.jar app.jar

# Render will set PORT; your app already reads it via server.port
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
