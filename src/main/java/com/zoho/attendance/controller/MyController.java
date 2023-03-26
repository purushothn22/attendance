package com.zoho.attendance.controller;

import com.zoho.attendance.dto.ErrorDetails;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/corsTest")
public class MyController {

    @GetMapping("/external-data")
    public ResponseEntity<String> getExternalData() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://resyindustries.osc-fr1.scalingo.io/api/auth/corsTest", HttpMethod.GET, entity, String.class);
        return response;
    }


    @GetMapping("/login")
    @CrossOrigin
    public ResponseEntity<?> login() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/api/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("empId", "S1");
        requestBody.put("password", "Test");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ResponseEntity.class);
    return response;
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception e, WebRequest request) {
        e.printStackTrace();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
