package com.zoho.attendance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private String empId;
    private String clockDate;
    private String status;
    private Time checkinTime;
    private Time checkoutTime;
    private String checkinLocation;
    private String checkoutLocation;
    private String latitude;
    private String longitude;
    private String base64Image;
    private int logCount;
}
