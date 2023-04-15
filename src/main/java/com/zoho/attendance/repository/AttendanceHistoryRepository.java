package com.zoho.attendance.repository;


import com.zoho.attendance.entity.AttendanceHistoryEntity;
import com.zoho.attendance.entity.AttendanceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AttendanceHistoryRepository extends JpaRepository<AttendanceHistoryEntity, AttendanceId> {

    public static final String  ATTENDANCE_HISTORY= "SELECT " +
            "emp_id,date,clock_date,status,checkin_time,checkout_time,checkin_location,checkout_location,latitude,longitude,checkin,out_latitude,out_longitude " +
            "FROM attendance where (?1 is null or emp_id=?1) and (?2 is null or date(date)>=?2) and (?3 is null or date(date)<=?3)";

    public static final String  COUNT_ATTENDANCE_HISTORY= "SELECT " +
            "count(*) FROM attendance where (?1 is null or emp_id=?1) and (?2 is null or date(date)>=?2) and (?3 is null or date(date)<=?3)";

    public static final String CHECKIN_PHOTO= "Select photo as \"photo\",image_Type as \"imageType\" from attendance " +
            "where emp_id=?1 and date(date)=?2 and checkin=?3";

    public static final String CHECKOUT_PHOTO= "Select checkout_photo as \"photo\",out_image_type as \"imageType\" from attendance " +
            "where emp_id=?1 and date(date)=?2 and checkin=?3";

    @Query(value=ATTENDANCE_HISTORY, countQuery = COUNT_ATTENDANCE_HISTORY,nativeQuery = true)
    Page<AttendanceHistoryEntity> getAttendanceHistory(String empId, String fromDate, String toDate, Pageable pageable);


    @Query(value=CHECKIN_PHOTO, nativeQuery = true)
    Map<String,Object> getCheckinPhoto(String empId, String date,String logCount);

    @Query(value=CHECKOUT_PHOTO, nativeQuery = true)
    Map<String,Object> getCheckoutPhoto(String empId, String date,String logCount);
}	