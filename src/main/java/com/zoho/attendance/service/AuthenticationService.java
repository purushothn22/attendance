package com.zoho.attendance.service;

import com.zoho.attendance.config.JwtTokenUtil;
import com.zoho.attendance.dto.JwtToken;
import com.zoho.attendance.dto.LoginRequest;
import com.zoho.attendance.entity.UsersEntity;
import com.zoho.attendance.repository.UsersRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private UsersRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationService(UsersRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Optional<JwtToken> authenticate(LoginRequest loginRequest) {
        UsersEntity user = userRepository.findByEmpId(loginRequest.getEmpId());

        String jwtToken = jwtTokenUtil.generateToken(user.getEmpId());
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", jwtToken);

        if (user != null) {
            if (BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
                String token = jwtTokenUtil.generateToken(user.getEmpId());
                return Optional.of(new JwtToken(token));
            }
        }
        return Optional.empty();
    }
}