package io.florianlopes.usersmanagement.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.florianlopes.usersmanagement.api.users.domain.Metrics;
import io.florianlopes.usersmanagement.api.users.domain.listener.event.UserCreationSuccessEvent;
import io.florianlopes.usersmanagement.api.users.persistence.repository.UserRepository;
import io.florianlopes.usersmanagement.api.users.web.dto.v1.CreateUserRequest;
import io.micrometer.core.instrument.Statistic;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = UsersManagementSystemIT.Initializer.class)
@TestPropertySource(properties = {"embedded.containers.enabled=true"})
@Testcontainers
class UsersManagementSystemIT {

    private static final String USERS_ENDPOINTS = "/v1/users";
    private static final String METRICS_ENDPOINT = "/actuator/metrics/";

    @Container
    private static final RabbitMQContainer rabbitMQContainer =
            new RabbitMQContainer().withExposedPorts(5672, 15672);

    private static final WireMockServer wireMockServer = new WireMockServer();

    @Autowired private TestRestTemplate testRestTemplate;

    @Autowired private UserRepository userRepository;

    @Autowired private RabbitTemplate rabbitTemplate;

    @BeforeAll
    static void setUpClass() {
        wireMockServer.start();
    }

    @AfterAll
    static void tearDownClass() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("POST - /v1/users")
    void postRequestShouldCreateUser() throws IOException {
        stupIpApiResponseForLocalIP();

        this.userRepository.deleteAll();
        assumeThat(this.userRepository.count()).isEqualTo(0);

        final CreateUserRequest createUserRequest =
                new CreateUserRequest("John", "Doe", "john.doe@email.com", "test");
        final HttpEntity<CreateUserRequest> requestEntity =
                new HttpEntity<>(createUserRequest, null);
        final ResponseEntity<Void> pageDtoResponseEntity =
                this.testRestTemplate.exchange(
                        USERS_ENDPOINTS, HttpMethod.POST, requestEntity, Void.class);

        assertThat(pageDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        UserCreationSuccessEvent userCreationSuccessEvent =
                this.rabbitTemplate.receiveAndConvert(
                        "users.creation.success",
                        new ParameterizedTypeReference<UserCreationSuccessEvent>() {});

        final UserCreationSuccessEvent expectedUserCreationSuccessEvent =
                new UserCreationSuccessEvent("", "John", "Doe", "john.doe@email.com", "test");
        assertThat(userCreationSuccessEvent)
                .isNotNull()
                .isEqualToIgnoringGivenFields(expectedUserCreationSuccessEvent, "userId");
        assertThat(userCreationSuccessEvent.getUserId()).isNotBlank();

        final ResponseEntity<MetricsEndpoint.MetricResponse> usersCreationSuccessMetric =
                this.testRestTemplate.getForEntity(
                        METRICS_ENDPOINT + Metrics.NUMBER_OF_SUCCESSFUL_USER_CREATIONS,
                        MetricsEndpoint.MetricResponse.class);
        assertThat(usersCreationSuccessMetric.getBody()).isNotNull();
        assertThat(usersCreationSuccessMetric.getBody().getName())
                .isEqualTo(Metrics.NUMBER_OF_SUCCESSFUL_USER_CREATIONS);
        assertThat(usersCreationSuccessMetric.getBody().getMeasurements()).isNotEmpty();

        final MetricsEndpoint.Sample metricMeasurement =
                usersCreationSuccessMetric.getBody().getMeasurements().get(0);
        assertThat(metricMeasurement.getStatistic()).isEqualTo(Statistic.COUNT);
        assertThat(metricMeasurement.getValue()).isEqualTo(1);
    }

    private void stupIpApiResponseForLocalIP() throws IOException {
        wireMockServer.stubFor(
                get(urlPathEqualTo("/" + "127.0.0.1" + "/json/"))
                        .withHeader(HttpHeaders.ACCEPT, equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                HttpHeaders.CONTENT_TYPE,
                                                MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(
                                                StreamUtils.copyToString(
                                                        this.getClass()
                                                                .getResourceAsStream(
                                                                        "wiremock-local-ip-address-response.json"),
                                                        Charset.defaultCharset()))));
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            final TestPropertyValues values =
                    TestPropertyValues.of(
                            "spring.rabbitmq.port=" + rabbitMQContainer.getMappedPort(5672),
                            "app.ipapi.base.url=http://localhost:" + wireMockServer.port());
            values.applyTo(configurableApplicationContext);
        }
    }
}
