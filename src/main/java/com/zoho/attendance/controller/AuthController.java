package com.zoho.attendance.controller;

import com.zoho.attendance.config.JwtUtils;
import com.zoho.attendance.dto.LoginRequest;
import com.zoho.attendance.dto.LoginResponse;
import com.zoho.attendance.dto.UsersDTO;
import com.zoho.attendance.entity.UsersEntity;
import com.zoho.attendance.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private JwtUtils jwtUtils;
    private UsersService service;

    @Autowired
    public AuthController(JwtUtils jwtUtils, UsersService service) {
        this.jwtUtils = jwtUtils;
        this.service = service;
    }

/*    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            String token = jwtTokenProvider.createToken(loginRequest.getUsername());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        UsersEntity user = service.getUser(loginRequest.getEmpId());

        if (user == null || !BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwtToken = jwtUtils.generateToken(user.getEmpId());
        HttpHeaders headers = new HttpHeaders();
        headers.set("empId", user.getEmpId());
        headers.set("role", user.getRole());
        headers.set("phone", user.getPhoneNumber());
        return ResponseEntity.ok().headers(headers).body(new LoginResponse(jwtToken));
    }

    @PostMapping("/register")
    @CrossOrigin
    public ResponseEntity<?> register(@RequestBody UsersDTO user) {
        return ResponseEntity.ok(service.addUser(user));
    }
}