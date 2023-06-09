package com.zoho.attendance.repository;

import com.zoho.attendance.entity.AdminInfoEntity;
import com.zoho.attendance.entity.EmployeeInfoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AdminInfoRepository extends CrudRepository<AdminInfoEntity, String> {

    List<AdminInfoEntity> findAll();

    AdminInfoEntity findByEmpId(String empId);

    @Transactional
    void deleteByEmpId(String empId);
}