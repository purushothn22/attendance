
package com.zoho.attendance.service;

import com.zoho.attendance.dto.PasswordRequest;
import com.zoho.attendance.dto.UsersDTO;
import com.zoho.attendance.entity.AdminInfoEntity;
import com.zoho.attendance.entity.EmployeeInfoEntity;
import com.zoho.attendance.entity.UsersEntity;
import com.zoho.attendance.repository.AdminInfoRepository;
import com.zoho.attendance.repository.EmployeeInfoRepository;
import com.zoho.attendance.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsersService {
    public static final String RETURN_CODE = "returnCode";
    public static final String RETURN_MSG = "returnMsg";

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UsersRepository repository;
    @Autowired
    private EmployeeInfoRepository empRepository;
    @Autowired
    private AdminInfoRepository adminRepository;
    @Autowired
    private ModelMapper modelMapper;

    public UsersEntity getUser(String empId) {
        return repository.findByEmpId(empId);
    }

    public Map<String,Object> addUser(UsersDTO user) {
        Map<String,Object> responseMap=new HashMap<>();
        String ret = null;
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        UsersEntity userAccountList = repository.findByEmpId(user.getEmpId());
        if (userAccountList == null) {
            UsersEntity newUser = modelMapper.map(user, UsersEntity.class);
            repository.save(newUser);
            if (user.getRole().equalsIgnoreCase("ADMIN")) {
                AdminInfoEntity adminInfo = modelMapper.map(user, AdminInfoEntity.class);
                java.sql.Date sqlDate = java.sql.Date.valueOf(user.getDob());
                adminInfo.setDateOfBirth(sqlDate);
                adminRepository.save(adminInfo);
            } else {
                EmployeeInfoEntity employeeInfo = modelMapper.map(user, EmployeeInfoEntity.class);
                java.sql.Date sqlDate = java.sql.Date.valueOf(user.getDob());
                employeeInfo.setDateOfBirth(sqlDate);
                employeeInfo.setMultiLocation(user.getMultiLocation());
                empRepository.save(employeeInfo);
            }
            responseMap.put("returnCode",0);
            responseMap.put("returnMsg","User account has been created, Emp Id = " + user.getEmpId());
        }else {
            responseMap.put("returnCode",1);
            responseMap.put("returnMsg","User already registered");
        }
        return responseMap;
    }

    public Map<String, Object> changePassword(PasswordRequest request) {
        Map<String, Object> responseMap = new HashMap<>();

        UsersEntity user = getUser(request.getEmpId());

        if (user != null) {
            if (BCrypt.checkpw(request.getOldPassword(), user.getPassword())) {
                if (repository.updatePassword(bCryptPasswordEncoder.encode(request.getNewPassword()), request.getEmpId()) > 0) {
                    responseMap.put(RETURN_CODE, 0);
                    responseMap.put(RETURN_MSG, "Password changed successfully");
                } else {
                    responseMap.put(RETURN_CODE, 2);
                    responseMap.put(RETURN_MSG, "Password update failed");
                }
            } else {
                responseMap.put(RETURN_CODE, 3);
                responseMap.put(RETURN_MSG, "Incorrect Current password");
            }
        } else {
            responseMap.put(RETURN_CODE, 1);
            responseMap.put(RETURN_MSG, "User not found");
        }
        return responseMap;
    }

    public Map<String, Object> passwordReset(PasswordRequest request) {
        Map<String, Object> responseMap = new HashMap<>();

        UsersEntity user = getUser(request.getEmpId());

        if (user != null) {
                if (repository.updatePassword(bCryptPasswordEncoder.encode(request.getNewPassword()), request.getEmpId()) > 0) {
                    responseMap.put(RETURN_CODE, 0);
                    responseMap.put(RETURN_MSG, "Password changed successfully");
                } else {
                    responseMap.put(RETURN_CODE, 2);
                    responseMap.put(RETURN_MSG, "Password update failed");
                }
        } else {
            responseMap.put(RETURN_CODE, 1);
            responseMap.put(RETURN_MSG, "User not found");
        }
        return responseMap;
    }


    public List<UsersEntity> findAllUsers() {
        List<UsersEntity> usersList = (List<UsersEntity>) repository.findAll();
        if (usersList != null) {
            return usersList;
        }
        return null;
    }

    public String updateUser(UsersDTO user) {
        UsersEntity userEntity = repository.findByEmpId(user.getEmpId());

        if (userEntity != null) {
            userEntity.setEmpId(user.getEmpId());
            userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            repository.save(userEntity);
            return ("User data update successfully.");
        }
        return "No record found";
    }
}