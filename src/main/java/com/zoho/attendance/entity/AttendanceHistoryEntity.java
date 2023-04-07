package com.zoho.attendance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.sql.Date;
import java.sql.Time;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AttendanceId.class)
public class AttendanceHistoryEntity {
    @Id
    private String empId;
    @Id
    private String clockDate;
    private Date date;
    private String status;
    private Time checkinTime;
    private Time checkoutTime;
    private String checkinLocation;
    private String checkoutLocation;
    private String latitude;
    private String longitude;
    @Id
    @Column(name = "checkin")
    private int logCount;
    private String outLatitude;
    private String outLongitude;
}