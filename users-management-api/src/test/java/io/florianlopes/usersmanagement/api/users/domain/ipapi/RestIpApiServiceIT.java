package io.florianlopes.usersmanagement.api.users.domain.ipapi;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.florianlopes.usersmanagement.api.users.domain.ipapi.exception.InvalidIpAddressIpApiException;
import io.florianlopes.usersmanagement.api.users.domain.ipapi.exception.IpApiException;

class RestIpApiServiceIT {

    private static final String WIREMOCK_INVALID_IP_ADDRESS_RESPONSE =
            "wiremock-invalid-ip-address-response.json";
    private static final String WIREMOCK_VALID_IP_ADDRESS_RESPONSE =
            "wiremock-valid-ip-address-response.json";

    private static final WireMockServer wireMockServer = new WireMockServer();

    private RestIpApiService restIpApiService;

    @BeforeAll
    static void setUpClass() {
        wireMockServer.start();
    }

    @AfterAll
    static void tearDownClass() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setUp() {
        this.restIpApiService = new RestIpApiService("http://localhost:" + wireMockServer.port());
    }

    @Test
    void getCountryByIpInvalidIpAddressShouldThrowInvalidIpAddressException() throws IOException {
        wireMockServer.stubFor(
                get(urlPathEqualTo("/1.1/json/"))
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
                                                                        WIREMOCK_INVALID_IP_ADDRESS_RESPONSE),
                                                        Charset.defaultCharset()))));

        assertThatExceptionOfType(InvalidIpAddressIpApiException.class)
                .isThrownBy(() -> this.restIpApiService.getCountryCodeByIp("1.1"));
    }

    @ParameterizedTest(name = "ipApiResponseStatusCode")
    @ValueSource(ints = {400, 404, 405, 429})
    void getCountryByIpUnknownErrorsShouldThrowIpApiException(int ipApiResponseStatusCode) {
        wireMockServer.stubFor(
                get(urlPathEqualTo("/1.1/json/"))
                        .willReturn(aResponse().withStatus(ipApiResponseStatusCode)));

        assertThatExceptionOfType(IpApiException.class)
                .isThrownBy(() -> this.restIpApiService.getCountryCodeByIp("1.1"));
    }

    @Test
    void getCountryByIpNullResponseShoudThrowIpApiException() {
        wireMockServer.stubFor(
                get(urlPathEqualTo("/2.2.2.2/json/"))
                        .willReturn(aResponse().withBody(StringUtils.EMPTY).withStatus(200)));

        assertThatExceptionOfType(IpApiException.class)
                .isThrownBy(() -> this.restIpApiService.getCountryCodeByIp("2.2.2.2"));
    }

    @Test
    void getCountryByIpShoudReturnCountryCode() throws IOException, IpApiException {
        final String validIp = "1.1.1.1";
        wireMockServer.stubFor(
                get(urlPathEqualTo("/" + validIp + "/json/"))
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
                                                                        WIREMOCK_VALID_IP_ADDRESS_RESPONSE),
                                                        Charset.defaultCharset()))));

        assertThat(this.restIpApiService.getCountryCodeByIp(validIp)).isEqualTo("AU");
    }
}
