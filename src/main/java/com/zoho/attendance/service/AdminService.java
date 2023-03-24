
package com.zoho.attendance.service;

import com.zoho.attendance.dto.EmployeeInfoDTO;
import com.zoho.attendance.entity.AdminInfoEntity;
import com.zoho.attendance.repository.AdminInfoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {
    public static final String RETURN_CODE="returnCode";
    public static final String RETURN_MSG="returnMsg";

    @Autowired
    private AdminInfoRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public AdminInfoEntity findAdmin(String empId) {
        return repository.findByEmpId(empId);
    }

    public Map<String,Object> updateEmpInfo(EmployeeInfoDTO employee) {
        Map<String,Object> responseMap=new HashMap<>();
        AdminInfoEntity entity = repository.findByEmpId(employee.getEmpId());

        if (entity != null) {
            AdminInfoEntity adminEntity = modelMapper.map(employee, AdminInfoEntity.class);
            repository.save(adminEntity);
            responseMap.put(RETURN_CODE,0);
            responseMap.put(RETURN_MSG,"User data updated successfully");
            return responseMap;
        }
        responseMap.put(RETURN_CODE,1);
        responseMap.put(RETURN_MSG,"No record found");
        return responseMap;
    }

    public void deleteAdmin(String empId) {
        repository.deleteByEmpId(empId);
    }
}