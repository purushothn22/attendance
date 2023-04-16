package com.zoho.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoFenceDTO {

    private Long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private double radius;

}