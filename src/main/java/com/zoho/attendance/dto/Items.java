package com.zoho.attendance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Items {
    private String title;
    private String id;
    @JsonProperty("resultType")
    private String resulttype;
    private Address address;
    private Position position;
    private List<Access> access;
    private int distance;
    private List<Categories> categories;
}
