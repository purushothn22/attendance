package com.zoho.attendance.repository;


import com.zoho.attendance.entity.AttendanceEntity;
import com.zoho.attendance.entity.AttendanceHistoryEntity;
import com.zoho.attendance.entity.AttendanceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceHistoryRepository extends JpaRepository<AttendanceHistoryEntity, AttendanceId> {

    public static final String  ATTENDANCE_HISTORY= "SELECT " +
            "emp_id,date,clock_date,status,checkin_time,checkout_time,checkin_location,checkout_location,latitude,longitude,checkin,out_latitude,out_longitude " +
            "FROM attendance where (?1 is null or date(date)=?1) and (?2 is null or emp_id=?2) and (?3 is null or MONTH(date)=?3) and (?4 is null or year(date)=?4) and weekday(date)!=6";

    public static final String  COUNT_ATTENDANCE_HISTORY= "SELECT " +
            "count(*) FROM attendance where (?1 is null or date(date)=?1) and (?2 is null or emp_id=?2) and (?3 is null or MONTH(date)=?3) and (?4 is null or year(date)=?4) and weekday(date)!=6";

    @Query(value=ATTENDANCE_HISTORY, countQuery = COUNT_ATTENDANCE_HISTORY,nativeQuery = true)
    Page<AttendanceHistoryEntity> getAttendanceHistory(String reqDate, String empId, String month,String year, Pageable pageable);
}	