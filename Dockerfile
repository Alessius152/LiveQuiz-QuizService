
from maven:3.9-eclipse-temurin-21 as build
workdir /app
copy pom.xml ./
run mvn dependency:go-offline -B
copy src ./src
run mvn clean package -DskipTests

from eclipse-temurin:21-jre-jammy
workdir /app
copy --from=build /app/target/*.jar app.jar
expose 8080
entrypoint ["java", "-jar", "app.jar"]
