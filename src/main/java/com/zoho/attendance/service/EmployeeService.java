
package com.zoho.attendance.service;

import com.zoho.attendance.dto.EmployeeInfoDTO;
import com.zoho.attendance.entity.EmployeeInfoEntity;
import com.zoho.attendance.repository.EmployeeInfoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    public static final String RETURN_CODE="returnCode";
    public static final String RETURN_MSG="returnMsg";

    @Autowired
    private EmployeeInfoRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public List<EmployeeInfoEntity> findAllEmployee() {
        return (List<EmployeeInfoEntity>) repository.findAll();
    }

    public EmployeeInfoEntity findEmployee(String empId) {
        return repository.findByEmpId(empId);
    }

    public Map<String,Object> updateEmpInfo(EmployeeInfoDTO employee) {
        Map<String,Object> responseMap=new HashMap<>();
        EmployeeInfoEntity entity = repository.findByEmpId(employee.getEmpId());

        if (entity != null) {
            EmployeeInfoEntity employeeEntity = modelMapper.map(employee, EmployeeInfoEntity.class);
            repository.save(employeeEntity);
            responseMap.put(RETURN_CODE,0);
            responseMap.put(RETURN_MSG,"User data updated successfully");
            return responseMap;
        }
        responseMap.put(RETURN_CODE,1);
        responseMap.put(RETURN_MSG,"No record found");
        return responseMap;
    }

    public void deleteEmployee(String empId) {
        repository.deleteByEmpId(empId);
    }
}