package com.zoho.attendance.controller;

import com.zoho.attendance.dto.EmployeeInfoDTO;
import com.zoho.attendance.entity.AdminInfoEntity;
import com.zoho.attendance.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService service;

    @Autowired
    public AdminController(AdminService service) {
        this.service = service;
    }

    @GetMapping(path = "/{empId}")
    public AdminInfoEntity findEmployee(@PathVariable String empId) {
        return service.findAdmin(empId);
    }

    @PutMapping()
    public Map<String, Object> updateEmpInfo(@RequestBody EmployeeInfoDTO employee) {
        return service.updateEmpInfo(employee);
    }

    @DeleteMapping(path = "/{empId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String empId) {
        service.deleteAdmin(empId);
        return ResponseEntity.noContent().build();
    }
}
