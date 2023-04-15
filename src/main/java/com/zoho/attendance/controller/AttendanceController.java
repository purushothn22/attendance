package com.zoho.attendance.controller;

import com.zoho.attendance.dto.*;
import com.zoho.attendance.entity.AttendanceHistoryEntity;
import com.zoho.attendance.service.AttendanceService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;


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
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByDate(@RequestBody AttendanceReportDTO request) throws DataFormatException, IOException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000)
                .setConnectTimeout(5000)
                .build();

        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
        return ResponseEntity.ok(attendanceservice.getAttendanceByDate(request.getDate()));
    }

    @PostMapping(path = "/checkAttendance")
    public Map<String, Object> checkAttendance(@RequestBody MonthlyAttendanceDTO request) {
        return attendanceservice.checkAttendance(request);
    }

    @PostMapping(path = "/attendanceHistory")
    public Page<AttendanceHistoryEntity> getAttendanceHistory(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int pageSize, @RequestBody AttendanceHistoryDTO request) {
        Sort sort = Sort.by(Sort.Direction.ASC, "date");
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return attendanceservice.getAttendanceHistory(request, pageable);
    }

    @PostMapping(path = "/getPhoto")
    public Map<String, Object> getPhoto(@RequestBody PhotoDTO request) {
        return attendanceservice.getPhoto(request);
    }

    @PostMapping(path = "/checkClockOut")
    public Map<String, Object> checkClockOut(@RequestBody MonthlyAttendanceDTO request) {
        return attendanceservice.checkClockOut(request);
    }

    @PostMapping(path = "/getAttendanceByMonth")
    public List<AttendanceDTO> getAttendanceByMonth(@RequestBody MonthlyAttendanceDTO request) throws DataFormatException, IOException {
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

/*    @PostMapping(path = "/markAttendance")
    public AttendanceEntity markAttendance(@RequestPart("photo") MultipartFile photo, @RequestPart String attendanceJson) throws IOException {
        AttendanceDTO request = new AttendanceDTO();
        ObjectMapper objectMapper = new ObjectMapper();
        request = objectMapper.readValue(attendanceJson, AttendanceDTO.class);
        request.setPhoto(ImageUtil.compressImage(photo.getBytes()));
        return attendanceservice.markAttendance(request);
    }*/

    @PostMapping(path = "/markAttendance")
    public Map<String, Object> markAttendance(@RequestBody AttendanceReqDTO request) throws IOException {
        return attendanceservice.markAttendance1(request);
    }

/*    @PostMapping(path = "/corsTest")
    public EmployeeInfoEntity corsTest() {
        final String uri = "http://resyindustries.osc-fr1.scalingo.io/api/employee/{emp}";
        System.out.println("cors authorized");
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("emp", "E1");
        EmployeeInfoEntity result = restTemplate.getForObject(uri, EmployeeInfoEntity.class, params);
        return result;
    }*/

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception e, WebRequest request) {
        e.printStackTrace();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}