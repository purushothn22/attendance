package com.zoho.attendance.service;

import com.zoho.attendance.dto.AttendanceDTO;
import com.zoho.attendance.dto.UsersDTO;
import com.zoho.attendance.entity.AttendanceEntity;
import com.zoho.attendance.entity.EmployeeInfoEntity;
import com.zoho.attendance.entity.UsersEntity;
import com.zoho.attendance.repository.AttendanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            attendancerepository.save(attendance);
            ret = "Attendance marked successfully, Emp Id = " + request.getEmpId();
        }
        return ret;
    }
}
