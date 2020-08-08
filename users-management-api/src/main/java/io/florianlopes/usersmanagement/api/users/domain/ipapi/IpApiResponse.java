package io.florianlopes.usersmanagement.api.users.domain.ipapi;

import com.fasterxml.jackson.annotation.JsonProperty;

class IpApiResponse {

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("country_name")
    private String countryName;

    private boolean error;
    private String reason;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "IpApiResponse{"
                + "countryCode='"
                + countryCode
                + '\''
                + ", countryName='"
                + countryName
                + '\''
                + ", error="
                + error
                + ", reason='"
                + reason
                + '\''
                + '}';
    }
}
