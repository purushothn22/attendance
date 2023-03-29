package com.zoho.attendance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private String empId;
    private String clockDate;
    private String status;
    private String clockTime;
    private String location;
    private String latitude;
    private String longitude;
    private String base64Image;
}
