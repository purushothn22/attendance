
package com.zoho.attendance.service;

import com.zoho.attendance.dto.UsersDTO;
import com.zoho.attendance.entity.AdminInfoEntity;
import com.zoho.attendance.entity.EmployeeInfoEntity;
import com.zoho.attendance.entity.RoleEntity;
import com.zoho.attendance.entity.UsersEntity;
import com.zoho.attendance.repository.AdminInfoRepository;
import com.zoho.attendance.repository.EmployeeInfoRepository;
import com.zoho.attendance.repository.RoleRepository;
import com.zoho.attendance.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UsersService {
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
    @Autowired
    private RoleRepository roleRepository;

    public UsersEntity getUser(String empId) {
        return repository.findByEmpId(empId);
    }

    public String addUser(UsersDTO user) {
        String ret = null;
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        UsersEntity userAccountList = repository.findByEmpId(user.getEmpId());
        if (userAccountList == null) {
            UsersEntity newUser = modelMapper.map(user, UsersEntity.class);
            repository.save(newUser);
            if (user.getRole().equals("ADMIN")) {
                AdminInfoEntity adminInfo = modelMapper.map(user, AdminInfoEntity.class);
                adminRepository.save(adminInfo);
            } else {
                EmployeeInfoEntity employeeInfo = modelMapper.map(user, EmployeeInfoEntity.class);
                empRepository.save(employeeInfo);
            }

            ret = "User account has been created, Emp Id = " + user.getEmpId();
        }
        return ret;
    }


    public long CheckUser(String employeeId, String password) {
        UsersEntity userEntity = repository.findByEmpId(employeeId);
        String pass = bCryptPasswordEncoder.encode(password);
        System.out.println("PASSWORD : " + pass + "===>" + userEntity.getPassword());
        if (bCryptPasswordEncoder.matches(password, userEntity.getPassword())) {
            return 1;
        }
        return 0;
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
            userEntity.setPassword(user.getPassword());
            repository.save(userEntity);
            return ("User data update successfully.");
        }
        return "No record found";
    }

    private RoleEntity checkRoleExist(UsersDTO user) {
        RoleEntity role = new RoleEntity();
        role.setName(user.getRole());
        return roleRepository.save(role);
    }
}