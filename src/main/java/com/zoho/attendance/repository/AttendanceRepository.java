package com.zoho.attendance.repository;


import com.zoho.attendance.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface AttendanceRepository extends CrudRepository<AttendanceEntity, String> {

    public static final String ATTENDANCE_BY_DATE= "SELECT " +
            "emp_id,date,clock_date,status,clock_time,location,latitude,longitude,photo,checkin " +
            "FROM attendance where date(date)=?1";

    public static final String CHECK_ATTENDANCE= "SELECT count(*) FROM attendance " +
            "where emp_id=?1 and date(date)=?2";

    public static final String ATTENDANCE_BY_MONTH= "SELECT " +
            "emp_id,date,clock_date,status,clock_time,location,latitude,longitude,photo,checkin " +
            "FROM attendance where emp_id=?1 and MONTHNAME(date)=?2 order by date";

    public static final String EMP_ATTENDANCE_SUMMARY= "SELECT " +
            "count(emp_id) as \"daysPresent\",concat(MONTHNAME(date), ' ',YEAR(date)) as \"month\" " +
            "FROM attendance where emp_id=?1 and status='present' and date >= now()-interval 3 month GROUP BY concat(MONTHNAME(date), ' ',YEAR(date))";

    public static final String ATTENDANCE_DET_FOR_EMPLOYEE= "SELECT " +
            "emp_id as \"empId\",clock_date as \"date\",status,clock_time as \"clockTime\",location,latitude,longitude,checkin as \"logCount\" " +
            "FROM attendance where emp_id=?1 and MONTHNAME(date)=?2 order by date";

    @Query(value=ATTENDANCE_BY_DATE, nativeQuery = true)
    List<AttendanceEntity> getAttendanceByDate(String reqDate);

    @Query(value=CHECK_ATTENDANCE, nativeQuery = true)
    int checkAttendance(String empId,String reqDate);

    @Query(value=ATTENDANCE_BY_MONTH, nativeQuery = true)
    List<AttendanceEntity> getAttendanceByMonth(String empId,String month);

    @Query(value=EMP_ATTENDANCE_SUMMARY, nativeQuery = true)
    List<Map<String,Object>> getAttendanceSummaryByEmp(String empId);

    @Query(value=ATTENDANCE_DET_FOR_EMPLOYEE, nativeQuery = true)
    List<Map<String,Object>> getAttendanceForEmp(String empId,String month);

    @Transactional
    void deleteByEmpId(String empId);
}	