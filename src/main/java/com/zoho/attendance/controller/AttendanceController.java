package com.zoho.attendance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoho.attendance.Util.ImageUtil;
import com.zoho.attendance.dto.*;
import com.zoho.attendance.entity.AttendanceEntity;
import com.zoho.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceservice;

    @PostMapping(path = "/getLocation")
    public Object getLocation(@RequestBody LocationInfoDTO attendanceRequest) {
        final String uri = "https://revgeocode.search.hereapi.com/v1/revgeocode" +
                "?at={location}" +
                "&lang=en-US" +
                "&apiKey=9gaca_hmaWwdYwyZHXPhU5Vm4tHirD2x1HGWISSQD3U";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("location", attendanceRequest.getLatitude() + "," + attendanceRequest.getLongitude());
        Object result = restTemplate.getForObject(uri, Object.class, params);
        return result;
    }

    @PostMapping(path = "/getDailyAttendance")
    public List<AttendanceEntity> getAttendanceByDate(@RequestBody AttendanceReportDTO request) {
        return attendanceservice.getAttendanceByDate(request.getDate());
    }

    @PostMapping(path = "/checkAttendance")
    public Map<String, Object> checkAttendance(@RequestBody MonthlyAttendanceDTO request) {
        return attendanceservice.checkAttendance(request);
    }

    @PostMapping(path = "/getAttendanceByMonth")
    public List<AttendanceEntity> getAttendanceByMonth(@RequestBody MonthlyAttendanceDTO request) {
        return attendanceservice.getAttendanceByMonth(request);
    }

    @GetMapping(path = "/getAttendanceSummary/{empId}")
    public List<Map<String, Object>> getAttendanceSummaryByEmp(@PathVariable String empId) {
        return attendanceservice.getAttendanceSummaryByEmp(empId);
    }

    @PostMapping(path = "/getAttendanceForEmp")
    public List<Map<String, Object>> getAttendanceForEmp(@RequestBody MonthlyAttendanceDTO request) {
        return attendanceservice.getAttendanceForEmp(request);
    }

    @PostMapping(path = "/markAttendance")
    public AttendanceEntity markAttendance(@RequestPart("photo") MultipartFile photo, @RequestPart String attendanceJson) throws IOException {
        AttendanceDTO request = new AttendanceDTO();
        ObjectMapper objectMapper = new ObjectMapper();
        request = objectMapper.readValue(attendanceJson, AttendanceDTO.class);
        request.setPhoto(ImageUtil.compressImage(photo.getBytes()));
        return attendanceservice.markAttendance(request);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception e, WebRequest request) {
        e.printStackTrace();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}