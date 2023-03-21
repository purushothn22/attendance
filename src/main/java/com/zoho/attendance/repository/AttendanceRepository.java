package com.zoho.attendance.repository;


import com.zoho.attendance.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Repository
public interface AttendanceRepository extends CrudRepository<AttendanceEntity, String> {

    List<AttendanceEntity> findByEmpId(String employeeid);

    @Query(value="select emp_id, date, status, clock_time, location from attendance where date(date)=?1", nativeQuery = true)
    List<AttendanceEntity> findByDate(String reqDate);

    @Transactional
    void deleteByEmpId(String employeeid);
}	