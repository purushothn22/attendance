package com.zoho.attendance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {
    private String label;
    @JsonProperty("countryCode")
    private String countrycode;
    @JsonProperty("countryName")
    private String countryname;
    @JsonProperty("stateCode")
    private String statecode;
    private String state;
    private String county;
    private String city;
    private String district;
    @JsonProperty("postalCode")
    private String postalcode;
}
