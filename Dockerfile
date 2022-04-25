FROM maven:3.8.3-openjdk-17 as builder
# https://spring.io/blog/2020/08/14/creating-efficient-docker-images-with-spring-boot-2-3
# - "dependencies":
#    - "BOOT-INF/lib/dependency1.jar"
#    - "BOOT-INF/lib/dependency2.jar"
#  - "spring-boot-loader":
#    - "org/"
#  - "snapshot-dependencies":
#    - "BOOT-INF/lib/dependency3-SNAPSHOT.jar"
#    - "BOOT-INF/lib/dependency4-SNAPSHOT.jar"
#  - "application":
#    - "BOOT-INF/classes/"
#    - "META-INF/"
WORKDIR /app
COPY pom.xml .
RUN mvn -errors --batch-mode dependency:resolve
COPY src ./src
RUN mvn -errors --batch-mode package -Dmaven.test.skip=true

FROM openjdk:17
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]