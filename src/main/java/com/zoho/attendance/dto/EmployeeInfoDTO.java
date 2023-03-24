package com.zoho.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfoDTO {
    private String empId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private String email;
    private String multiLocation;
}
