package io.florianlopes.usersmanagement.api.users.domain.ipapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import io.florianlopes.usersmanagement.api.users.domain.ipapi.exception.InvalidIpAddressIpApiException;
import io.florianlopes.usersmanagement.api.users.domain.ipapi.exception.IpApiException;

@Component
class RestIpApiService implements IpApiService {

    private final WebClient webClient;

    public RestIpApiService(@Value("${app.ipapi.base.url}") String ipApiBaseUrl) {
        this.webClient = WebClient.create(ipApiBaseUrl);
    }

    @Override
    public String getCountryCodeByIp(String ip) throws IpApiException {
        try {
            final IpApiResponse ipApiResponse =
                    this.webClient
                            .get()
                            .uri("/{ip}/json/", ip)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(IpApiResponse.class)
                            .block();
            if (ipApiResponse != null) {
                if (ipApiResponse.isError()) {
                    throw new InvalidIpAddressIpApiException(
                            "Could not retrieve country: " + ipApiResponse.getReason());
                }
                return ipApiResponse.getCountryCode();
            } else {
                throw new IpApiException("Could not retrieve country by IP");
            }
        } catch (WebClientException e) {
            throw new IpApiException("Could not retrieve country by IP, is ipapi service down?", e);
        }
    }
}
