package com.zoho.attendance.service;

import com.zoho.attendance.Util.ImageUtil;
import com.zoho.attendance.dto.AttendanceDTO;
import com.zoho.attendance.dto.MonthlyAttendanceDTO;
import com.zoho.attendance.entity.AttendanceEntity;
import com.zoho.attendance.repository.AttendanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private ModelMapper modelMapper;


    public List<AttendanceEntity> getAttendanceByDate(String reqDate) {
/*        List<AttendanceEntity> responseList=new ArrayList<>();
        List<AttendanceEntity> entityList= attendanceRepository.getAttendanceByDate(reqDate);
        for(AttendanceEntity entity:entityList){
            entity.setPhoto(ImageUtil.decompressImage(entity.getPhoto()));
            responseList.add(entity);
        }*/
        List<AttendanceEntity> responseList = attendanceRepository.getAttendanceByDate(reqDate)
                .stream()
                .peek(entity -> entity.setPhoto(ImageUtil.decompressImage(entity.getPhoto())))
                .collect(Collectors.toList());
        return responseList;
    }

    public Map<String, Object> checkAttendance(MonthlyAttendanceDTO request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("count", attendanceRepository.checkAttendance(request.getEmpId(), request.getDate()));
        return responseMap;
    }

    public List<AttendanceEntity> getAttendanceByMonth(MonthlyAttendanceDTO request) {
        List<AttendanceEntity> responseList= attendanceRepository.getAttendanceByMonth(request.getEmpId(), request.getMonth())
                .stream()
                .peek(entity -> entity.setPhoto(ImageUtil.decompressImage(entity.getPhoto())))
                .collect(Collectors.toList());
        return responseList;
    }

    public List<Map<String, Object>> getAttendanceSummaryByEmp(String empId) {
        return attendanceRepository.getAttendanceSummaryByEmp(empId);
    }

    public List<Map<String, Object>> getAttendanceForEmp(MonthlyAttendanceDTO request) {
        List<Map<String, Object>> attendanceList = attendanceRepository.getAttendanceForEmp(request.getEmpId(), request.getMonth());
        Map<String, Map<String, Object>> attendanceMap = new HashMap<>();
        for (Map<String, Object> attendance : attendanceList) {
            String key = attendance.get("empId") + "_" + attendance.get("date");
            if (!attendanceMap.containsKey(key)) {
                attendanceMap.put(key, new HashMap<>());
                attendanceMap.get(key).put("empId", attendance.get("empId"));
                attendanceMap.get(key).put("date", attendance.get("date"));
                attendanceMap.get(key).put("attendance", new ArrayList<>());
            }
            List<Map<String, Object>> attendanceListForDate = (List<Map<String, Object>>) attendanceMap.get(key).get("attendance");
            Map<String, Object> attendanceForTime = new HashMap<>();
            attendanceForTime.put("clockTime", attendance.get("clockTime"));
            attendanceForTime.put("status", attendance.get("status"));
            attendanceForTime.put("location", attendance.get("location"));
            attendanceForTime.put("latitude", attendance.get("latitude"));
            attendanceForTime.put("longitude", attendance.get("longitude"));
            attendanceForTime.put("logCount", attendance.get("logCount"));
            attendanceListForDate.add(attendanceForTime);
        }
        List<Map<String, Object>> attendanceOutputList = new ArrayList<>(attendanceMap.values());
        return attendanceOutputList;
    }

    public AttendanceEntity markAttendance(AttendanceDTO request) throws IOException {
        String ret = null;
        int logCount = 0;
        AttendanceEntity attendance = modelMapper.map(request, AttendanceEntity.class);

        LocalDate localDate = request.getDate().toLocalDate();
        String clockDate = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
        attendance.setClockDate(clockDate);
        logCount += attendanceRepository.checkAttendance(request.getEmpId(), clockDate);
        attendance.setLogCount(logCount);
            /*long now = System.currentTimeMillis();
            Time sqlTime = new Time(now);*/
        Time sqlTime = Time.valueOf(request.getClockTime());
        attendance.setClockTime(sqlTime);
        return attendanceRepository.save(attendance);
    }
}
