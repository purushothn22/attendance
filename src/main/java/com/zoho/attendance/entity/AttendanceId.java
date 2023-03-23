package com.zoho.attendance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceId implements Serializable {
    private static final long serialVersionUID=3788955538639156111L;
    private String empId;
    private String clockDate;
    private int logCount;
}