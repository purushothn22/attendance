package com.zoho.attendance.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
public class LocationDTO {
    private List<Items> items;
}
