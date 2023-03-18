package com.zoho.attendance.repository;

import com.zoho.attendance.entity.EmployeeInfoEntity;
import com.zoho.attendance.entity.UsersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EmployeeInfoRepository extends CrudRepository<EmployeeInfoEntity, String> {

    EmployeeInfoEntity findByEmpId(String empId);

    @Transactional
    void deleteByEmpId(String empId);
}