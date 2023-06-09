package com.zoho.attendance.service;

import com.zoho.attendance.dto.*;
import com.zoho.attendance.entity.AttendanceEntity;
import com.zoho.attendance.entity.AttendanceHistoryEntity;
import com.zoho.attendance.repository.AttendanceHistoryRepository;
import com.zoho.attendance.repository.AttendanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AttendanceHistoryRepository historyRepo;
    @Autowired
    private GeofenceService geofenceService;


    public List<AttendanceDTO> getAttendanceByDate(String reqDate) throws DataFormatException, IOException {
        List<AttendanceDTO> responseList = new ArrayList<>();
      /*   List<AttendanceEntity> entityList= attendanceRepository.getAttendanceByDate(reqDate);
        for(AttendanceEntity entity:entityList){
            entity.setPhoto(ImageUtil.decompressImage(entity.getPhoto()));
            responseList.add(entity);
        }*/
        List<AttendanceEntity> entityList = attendanceRepository.getAttendanceByDate(reqDate);
        for (AttendanceEntity entity : entityList) {
            AttendanceDTO attendance = modelMapper.map(entity, AttendanceDTO.class);
/*            attendance.setInPhoto(decompress(entity.getPhoto()));
            attendance.setOutPhoto(decompress(entity.getCheckoutPhoto()));
            attendance.setInImageType(entity.getImageType());
            attendance.setOutImageType(entity.getOutImageType());*/
            responseList.add(attendance);
        }
        return responseList;
    }

    public Map<String, Object> checkAttendance(MonthlyAttendanceDTO request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("count", attendanceRepository.checkAttendance(request.getEmpId(), request.getDate()));
        return responseMap;
    }

    public Page<AttendanceHistoryEntity> getAttendanceHistory(AttendanceHistoryDTO request, Pageable pageable) {
        return historyRepo.getAttendanceHistory(request.getEmpId() != null && request.getEmpId().isBlank() ? null : request.getEmpId(),
                request.getFromDate() != null && request.getFromDate().isBlank() ? null : request.getFromDate(),
                request.getToDate() != null && request.getToDate().isBlank() ? null : request.getToDate(),
                pageable);
    }

    public Map<String, Object> getPhoto(PhotoDTO request) {
        if (request.getFlag() != null && request.getFlag().equalsIgnoreCase("I"))
            return historyRepo.getCheckinPhoto(request.getEmpId(), request.getDate(), request.getLogCount());
        else if (request.getFlag() != null && request.getFlag().equalsIgnoreCase("O"))
            return historyRepo.getCheckoutPhoto(request.getEmpId(), request.getDate(), request.getLogCount());
        else
            return Collections.emptyMap();
    }

    public Map<String, Object> checkClockOut(MonthlyAttendanceDTO request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("count", attendanceRepository.checkClockOut(request.getEmpId(), request.getDate()));
        return responseMap;
    }

    public List<AttendanceDTO> getAttendanceByMonth(MonthlyAttendanceDTO request) throws DataFormatException, IOException {
        List<AttendanceDTO> responseList = new ArrayList<>();
/*        List<AttendanceEntity> responseList = attendanceRepository.getAttendanceByMonth(request.getEmpId(), request.getMonth())
                .stream()
                .peek(entity -> entity.setPhoto(ImageUtil.decompressImage(entity.getPhoto())))
                .collect(Collectors.toList());*/
        List<AttendanceEntity> entityList = attendanceRepository.getAttendanceByMonth(request.getEmpId(), request.getMonth(), request.getYear());
        for (AttendanceEntity entity : entityList) {
            AttendanceDTO attendance = modelMapper.map(entity, AttendanceDTO.class);
/*            attendance.setInPhoto(decompress(entity.getPhoto()));
            attendance.setOutPhoto(decompress(entity.getCheckoutPhoto()));
            attendance.setInImageType(entity.getImageType());
            attendance.setOutImageType(entity.getOutImageType());*/
            responseList.add(attendance);
        }
        return responseList;
    }

    public List<Map<String, Object>> getAttendanceSummaryByEmp(String empId) {
        List<Map<String, Object>> responseList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate threeMonthsAgo = today.minus(3, ChronoUnit.MONTHS);
        Map<String, Object> daysCountMap = new HashMap<>();
        while (!threeMonthsAgo.isAfter(today)) {
            int year = threeMonthsAgo.getYear();
            int month = threeMonthsAgo.getMonthValue(); // March
            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();
            /*int sundaysInMonth = 0;
            for (int i = 1; i <= daysInMonth; i++) {
                LocalDate date = LocalDate.of(year, month, i);
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                if (dayOfWeek == DayOfWeek.SUNDAY) {
                    sundaysInMonth++;
                }
            }
            int daysInMonthExcludingSunday = daysInMonth - sundaysInMonth;*/
            daysCountMap.put(yearMonth.toString(), daysInMonth);
            threeMonthsAgo = threeMonthsAgo.plus(1, ChronoUnit.MONTHS);
        }
        //List<String> daysList=attendanceRepository.getAllDays(request.getYear(),request.getMonth());
        List<Map<String, Object>> summaryList = attendanceRepository.getAttendanceSummaryByEmp(empId);
        Map<String, Object> responseMap;
        for (Map<String, Object> summaryMap : summaryList) {
            responseMap = new HashMap<>(summaryMap);
            if (daysCountMap.containsKey(responseMap.get("yearMonth"))) {
                responseMap.put("workingDays", daysCountMap.get(summaryMap.get("yearMonth")));
            }
            responseList.add(responseMap);
        }
        return responseList;
    }

    public List<Map<String, Object>> getAttendanceForEmp(MonthlyAttendanceDTO request) {
        List<Map<String, Object>> attendanceList = attendanceRepository.getAttendanceForEmp(request.getEmpId(), request.getMonth());
        List<String> daysList = attendanceRepository.getAllDays(request.getYear(), request.getMonth());
        Map<String, Map<String, Object>> attendanceMap = new HashMap<>();
        for (String days : daysList) {
            attendanceMap.put(days, new HashMap<>());
            attendanceMap.get(days).put("date", days);
            attendanceMap.get(days).put("present", "N");
        }
        for (Map<String, Object> attendance : attendanceList) {
            String key = (String) attendance.get("date");
            if (attendanceMap.containsKey(key)) {
                //attendanceMap.put(key, new HashMap<>());
                attendanceMap.get(key).put("date", attendance.get("date"));
                attendanceMap.get(key).put("present", "Y");
                if (!attendanceMap.get(key).containsKey("attendance"))
                    attendanceMap.get(key).put("attendance", new ArrayList<>());
            }
            if (attendanceMap.get(key).containsKey("attendance")) {
                List<Map<String, Object>> attendanceListForDate = (List<Map<String, Object>>) attendanceMap.get(key).get("attendance");
                Map<String, Object> attendanceForTime = new HashMap<>();
                attendanceForTime.put("checkinTime", attendance.get("checkinTime"));
                attendanceForTime.put("checkoutTime", attendance.get("checkoutTime"));
                attendanceForTime.put("status", attendance.get("status"));
                attendanceForTime.put("checkinLocation", attendance.get("checkinLocation"));
                attendanceForTime.put("checkoutLocation", attendance.get("checkoutLocation"));
                attendanceForTime.put("latitude", attendance.get("latitude"));
                attendanceForTime.put("longitude", attendance.get("longitude"));
                attendanceForTime.put("logCount", attendance.get("logCount"));
                attendanceListForDate.add(attendanceForTime);
            }
        }
        TreeMap<String, Map<String, Object>> treeMap = new TreeMap<>(attendanceMap);
        List<Map<String, Object>> attendanceOutputList = new ArrayList<>(treeMap.values());
        return attendanceOutputList;
    }

    public AttendanceEntity markAttendance(AttendanceReqDTO request) throws IOException {
        String ret = null;
        int logCount = 0;
        AttendanceEntity attendance = modelMapper.map(request, AttendanceEntity.class);

        java.sql.Date sqlDate = new java.sql.Date(request.getDateTime());
        LocalDate localDate = sqlDate.toLocalDate();
        String clockDate = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
        attendance.setClockDate(clockDate);
        attendance.setDate(sqlDate);
        logCount += attendanceRepository.checkAttendance(request.getEmpId(), clockDate);
        attendance.setLogCount(logCount);
        Time sqlTime = Time.valueOf(request.getCheckinTime());
        attendance.setCheckinTime(sqlTime);
        return attendanceRepository.save(attendance);
    }

    public Map<String, Object> markAttendance1(AttendanceReqDTO request) throws IOException {
        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Object> geofenceMap = geofenceService.isInsideGeoFence(Double.parseDouble(request.getLatitude()), Double.parseDouble(request.getLongitude()), request.getGeoFenceId());
        if ((int) geofenceMap.get("returnCode") != 0) {
            return geofenceMap;
        }
        String ret = null;
        int logCount = 1;
        AttendanceEntity attendance = modelMapper.map(request, AttendanceEntity.class);
        java.sql.Date sqlDate = new java.sql.Date(request.getDateTime());
        LocalDate localDate = sqlDate.toLocalDate();
        String clockDate = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
        attendance.setClockDate(clockDate);
        attendance.setDate(sqlDate);
        if (request.getCheckinFlag() != null) {
            if (request.getCheckinFlag().equalsIgnoreCase("I")) {
                logCount += attendanceRepository.checkAttendance(request.getEmpId(), clockDate);
                attendance.setLogCount(logCount);
                Time sqlTime = Time.valueOf(request.getCheckinTime());
                attendance.setCheckinTime(sqlTime);
                attendance.setCheckinLocation(request.getLocation());
                attendance.setPhoto(compress(request.getBase64Image()));
                if (attendanceRepository.save(attendance) != null) {
                    responseMap.put("returnCode", 0);
                    responseMap.put("returnMsg", "Attendance marked Successfully");
                } else {
                    responseMap.put("returnCode", 1);
                    responseMap.put("returnMsg", "CheckIn Failure");
                }
            } else {
                if (attendanceRepository.updateClockOutTime(request.getCheckoutTime(), request.getLocation(), compress(request.getBase64Image()), request.getImageType(), request.getLatitude(), request.getLongitude(), request.getEmpId(), clockDate, request.getLogCount()) > 0) {
                    responseMap.put("returnCode", 0);
                    responseMap.put("returnMsg", "Checkout Success");
                } else {
                    responseMap.put("returnCode", 1);
                    responseMap.put("returnMsg", "Checkout Failure");
                }
            }
            return responseMap;
        }
        responseMap.put("returnCode", 2);
        responseMap.put("returnMsg", "CheckinFlag null");
        return responseMap;
    }

     /*public static byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        System.out.println("Original: " + data.length / 1024 + " Kb");
        System.out.println("Compressed: " + output.length / 1024 + " Kb");
        return output;
    }

    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        return output;
    }*/

    public byte[] compress(String base64Img) throws IOException {

        if (base64Img != null && !base64Img.isEmpty()) {
            byte[] input = Base64.getDecoder().decode(base64Img);
            /*Deflater deflater = new Deflater();
            deflater.setInput(input);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(input.length);

            deflater.finish();
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            return outputStream.toByteArray();*/
            return input;
        } else
            return null;
    }

    public String decompress(byte[] compressedData) throws IOException {
        if (compressedData != null) {
            // decompress data
/*            Inflater inflater = new Inflater();
            inflater.setInput(compressedData);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);

            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                try {
                    int count = inflater.inflate(buffer);
                    outputStream.write(buffer, 0, count);
                } catch (DataFormatException e) {
                    // handle exception
                }
            }
            outputStream.close();
            System.out.println("decompress-->"+outputStream);*/
            return Base64.getEncoder().encodeToString(compressedData);
        } else
            return null;
    }


}
