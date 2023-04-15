package com.zoho.attendance.repository;


import com.zoho.attendance.entity.AttendanceEntity;
import com.zoho.attendance.entity.AttendanceHistoryEntity;
import com.zoho.attendance.entity.AttendanceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface AttendanceRepository extends CrudRepository<AttendanceEntity, AttendanceId> {

    public static final String  ATTENDANCE_BY_DATE= "SELECT " +
            "emp_id,date,clock_date,status,checkin_time,checkout_time,checkin_location,checkout_location,latitude,longitude,checkin,out_latitude,out_longitude " +
            "FROM attendance where date(date)=?1";

    public static final String CHECK_ATTENDANCE= "SELECT count(*) FROM attendance " +
            "where emp_id=?1 and date(date)=?2";

    public static final String CHECK_CLOCK_OUT= "Select count(*) from attendance " +
            "where emp_id=?1 and date(date)=?2 and checkout_time is null";


    public static final String ATTENDANCE_BY_MONTH= "SELECT " +
            "emp_id,date,clock_date,status,checkin_time,checkout_time,checkin_location,checkout_location,latitude,longitude,photo,image_Type,checkout_photo,out_image_type,checkin,out_latitude,out_longitude " +
            "FROM attendance where emp_id=?1 and MONTH(date)=?2 and year(date)=?3 order by date";

    public static final String EMP_ATTENDANCE_SUMMARY= "SELECT " +
            "count(distinct clock_date) as \"daysPresent\",concat(MONTHNAME(date), ' ',YEAR(date)) as \"month\",concat(YEAR(date),'-',LPAD(month(date),2,0)) as \"yearMonth\" " +
            "FROM attendance where emp_id=?1 and status='present' and date >= now()-interval 3 month and weekday(date)!=6 GROUP BY concat(MONTHNAME(date), ' ',YEAR(date)),concat(YEAR(date),'-',LPAD(month(date),2,0))";

    public static final String ATTENDANCE_DET_FOR_EMPLOYEE= "SELECT " +
            "emp_id as \"empId\",concat(year(date),'-',LPAD(month(date),2,0),'-',LPAD(day(date),2,0)) as \"date\",status,checkin_time as \"checkinTime\",checkout_time as \"checkoutTime\",checkin_location as \"checkinLocation\",checkout_location as \"checkoutLocation\",latitude,longitude,checkin as \"logCount\" " +
            "FROM attendance where emp_id=?1 and MONTH(date)=?2 order by date";

    public static final String GET_ALL_DAYS="" +
            "select a.Date " +
            "from (" +
            "    select LAST_DAY(CONCAT(?1, '-', ?2, '-01')) - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as Date " +
            "    from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a " +
            "    cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b " +
            "    cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c " +
            ") a " +
            "where a.Date between  DATE_FORMAT(CONCAT(?1, '-', ?2, '-01'), '%Y-%m-%d')  and LAST_DAY(CONCAT(?1, '-', ?2, '-01')) order by a.Date";

    public static final String UPDATE_CLOCK_OUT="UPDATE attendance SET checkout_time=TIME(?1),checkout_location=?2,checkout_photo=?3,out_image_type=?4,out_latitude=?5,out_longitude=?6 where emp_id=?7 and date(date)=?8 and checkin=?9";

    @Query(value=ATTENDANCE_BY_DATE, nativeQuery = true)
    List<AttendanceEntity> getAttendanceByDate(String reqDate);

    @Query(value=CHECK_ATTENDANCE, nativeQuery = true)
    int checkAttendance(String empId,String reqDate);

    @Query(value=CHECK_CLOCK_OUT, nativeQuery = true)
    int checkClockOut(String empId,String reqDate);

    @Query(value=ATTENDANCE_BY_MONTH, nativeQuery = true)
    List<AttendanceEntity> getAttendanceByMonth(String empId,String month,String year);

    @Query(value=EMP_ATTENDANCE_SUMMARY, nativeQuery = true)
    List<Map<String,Object>> getAttendanceSummaryByEmp(String empId);

    @Query(value=ATTENDANCE_DET_FOR_EMPLOYEE, nativeQuery = true)
    List<Map<String,Object>> getAttendanceForEmp(String empId,String month);

    @Query(value=GET_ALL_DAYS, nativeQuery = true)
    List<String> getAllDays(String year,String month);

    @Transactional
    @Modifying
    @Query(value=UPDATE_CLOCK_OUT, nativeQuery = true)
    int updateClockOutTime(String time,String location,byte[] checkoutPhoto,String imageType,String latitude,String longitude,String empId,String date,int logCount);

    @Transactional
    void deleteByEmpId(String empId);
}	