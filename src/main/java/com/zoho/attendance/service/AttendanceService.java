package com.zoho.attendance.service;

import com.zoho.attendance.dto.AttendanceDTO;
import com.zoho.attendance.entity.AttendanceEntity;
import com.zoho.attendance.repository.AttendanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendancerepository;
    @Autowired
    private ModelMapper modelMapper;


    public List<AttendanceEntity> findAllUser() {
        List<AttendanceEntity> attendanceList = (List<AttendanceEntity>) attendancerepository.findAll();

        if (attendanceList != null) {
            return attendanceList;
        }
        return null;
    }

    public List<AttendanceEntity> findByDate(String reqDate) {
        return attendancerepository.findByDate(reqDate);
    }

    public List<AttendanceEntity> findByEmpId(String employeeid) {
        List<AttendanceEntity> attendanceList = attendancerepository.findByEmpId(employeeid);

        if (attendanceList != null) {
            return attendanceList;
        }
        return null;
    }

    public String markAttendance(AttendanceDTO request) {
        String ret = null;
        if (request != null) {
            AttendanceEntity attendance = modelMapper.map(request, AttendanceEntity.class);
            /*long now = System.currentTimeMillis();
            Time sqlTime = new Time(now);*/
            Time sqlTime = Time.valueOf(request.getClockTime());
            attendance.setClockTime(sqlTime);
            attendancerepository.save(attendance);
            ret = "Attendance marked successfully, Emp Id = " + request.getEmpId();
        }
        return ret;
    }
}
