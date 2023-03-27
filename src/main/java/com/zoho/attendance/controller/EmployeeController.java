package com.zoho.attendance.controller;

import com.zoho.attendance.dto.EmployeeInfoDTO;
import com.zoho.attendance.entity.EmployeeInfoEntity;
import com.zoho.attendance.service.EmployeeService;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/employee")
//@ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
public class EmployeeController {

    private EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping()
    public EmployeeInfoEntity findEmployee() {
        return service.findAllEmployee();
    }

    @GetMapping(path = "/{empId}")
    public EmployeeInfoEntity findEmployee(@PathVariable String empId) {
        return service.findEmployee(empId);
    }

    @PutMapping()
    public Map<String, Object> updateEmpInfo(@RequestBody EmployeeInfoDTO employee) {
        return service.updateEmpInfo(employee);
    }

    @DeleteMapping(path = "/{empId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String empId) {
        service.deleteEmployee(empId);
        return ResponseEntity.noContent().build();
    }
}
