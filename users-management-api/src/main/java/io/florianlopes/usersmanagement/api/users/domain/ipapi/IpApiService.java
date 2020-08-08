package io.florianlopes.usersmanagement.api.users.domain.ipapi;

import io.florianlopes.usersmanagement.api.users.domain.ipapi.exception.IpApiException;

public interface IpApiService {

    String getCountryCodeByIp(String ip) throws IpApiException;
}
