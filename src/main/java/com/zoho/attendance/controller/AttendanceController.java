package com.zoho.attendance.controller;

import com.zoho.attendance.dto.AttendanceDTO;
import com.zoho.attendance.dto.LocationInfoDTO;
import com.zoho.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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
        /*System.out.print("result"+result.toString());
        final String baseUrl= "https://revgeocode.search.hereapi.com/v1";

        List<LocationDTO> location= WebClient.create().get()
                .uri(builder -> builder.scheme("https")
                        .host("revgeocode.search.hereapi.com").path("v1").path("/revgeocode")
                        .queryParam("at", attendanceRequest.getLatitude()+","+attendanceRequest.getLongitude())
                        .queryParam("lang", "en-US")
                        .queryParam("apiKey", "9gaca_hmaWwdYwyZHXPhU5Vm4tHirD2x1HGWISSQD3U")
                        .build())
                .retrieve()
                .bodyToFlux(LocationDTO.class)      //returning multiple values, so bodyToFlux
                .collectList()
                .block();
        System.out.print("webclient"+location);
        return location;*/
    }


    @GetMapping(path = "/findAll")
    public ResponseEntity<?> findAllUser() {
        HttpHeaders headers = new HttpHeaders();

        try {
            //System.out.println("en da ipdi "+employeeid+employeeservice.findByempid(employeeid));
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(attendanceservice.findAllUser());
        } catch (Exception e) {
            headers.add("Message", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body("Failed to add the user");
        }
    }


    @GetMapping(path = "/findbyempid/{empId}")
    public ResponseEntity<?> findByempid(@RequestParam String empId) {
        HttpHeaders headers = new HttpHeaders();

        try {
            //System.out.println("en da ipdi "+employeeid+employeeservice.findByempid(employeeid));
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(attendanceservice.findByEmpId(empId));
        } catch (Exception e) {
            headers.add("Message", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body("Failed to add the user");
        }
    }

    @PostMapping(path="/markAttendance")
    public ResponseEntity<?> markAttendance(@RequestBody AttendanceDTO request) {
        HttpHeaders headers = new HttpHeaders();

        try {
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(attendanceservice.markAttendance(request));
        } catch (Exception e) {
            headers.add("Message", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body("Failed to add the user");
        }
    }

}