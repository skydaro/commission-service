package com.sikorski.commission;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@Slf4j
@AutoConfigureMetrics
@Tag("ContainerizedIntegrationTest")
@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "logging.level.org.hibernate.SQL=DEBUG",
                "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE",
                "spring.jpa.properties.hibernate.format_sql=true",
                "logging.level.org.springframework.jdbc.core.JdbcTemplate=DEBUG",
                "logging.level.org.springframework.jdbc.core.StatementCreatorUtils=TRACE",
                "spring.jpa.show-sql=true"
        }
)
@Testcontainers
@DirtiesContext(classMode = AFTER_CLASS)
public abstract class ContainerizedIntegrationTest {

    public static final DockerImageName MOCKSERVER_IMAGE = DockerImageName.parse("jamesdbloom/mockserver:mockserver-5.11.2");

    @Container
    protected static final MockServerContainer mockServerContainer = new MockServerContainer(MOCKSERVER_IMAGE);

    @Container
    protected static PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres:13.3-alpine");

    @DynamicPropertySource
    protected static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("fx.api.baseUrl", mockServerContainer::getEndpoint);
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:postgresql://localhost:%d/%s", postgreSqlContainer.getFirstMappedPort(), postgreSqlContainer.getDatabaseName()));
        registry.add("spring.datasource.username", postgreSqlContainer::getUsername);
        registry.add("spring.datasource.password", postgreSqlContainer::getPassword);
    }
}
