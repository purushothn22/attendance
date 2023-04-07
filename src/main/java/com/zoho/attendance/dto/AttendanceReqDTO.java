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
public class AttendanceReqDTO {
    private String empId;
    private long dateTime;
    private String status;
    private String checkinTime;
    private String checkoutTime;
    private String location;
    private String latitude;
    private String longitude;
    private String base64Image;
    private String imageType;
    private String checkinFlag;
    private int logCount;
}
