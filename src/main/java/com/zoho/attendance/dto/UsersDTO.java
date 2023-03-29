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
public class UsersDTO {
    private String empId;
    private String password;
    private String phoneNumber;
    private String role;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String address;
    private String email;
    private String multiLocation;
}
