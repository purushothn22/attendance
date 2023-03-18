package com.zoho.attendance.repository;


import com.zoho.attendance.entity.AttendanceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AttendanceRepository extends CrudRepository<AttendanceEntity, String> {

    List<AttendanceEntity> findByEmpId(String employeeid);

    @Transactional
    void deleteByEmpId(String employeeid);
}	