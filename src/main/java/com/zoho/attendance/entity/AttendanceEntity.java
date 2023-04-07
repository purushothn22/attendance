package com.zoho.attendance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Base64;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance")
@IdClass(AttendanceId.class)
public class AttendanceEntity {
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
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;
    private String imageType;
    private byte[] checkoutPhoto;
    private String outImageType;
    @Id
    @Column(name = "checkin")
    private int logCount;
    private String outLatitude;
    private String outLongitude;
}