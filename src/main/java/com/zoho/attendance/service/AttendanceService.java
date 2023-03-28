package com.zoho.attendance.service;

import com.zoho.attendance.dto.AttendanceDTO;
import com.zoho.attendance.dto.MonthlyAttendanceDTO;
import com.zoho.attendance.entity.AttendanceEntity;
import com.zoho.attendance.repository.AttendanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
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
            attendance.setBase64Image(Base64.getEncoder().encodeToString(entity.getPhoto()));
            responseList.add(attendance);
        }
        return responseList;
    }

    public Map<String, Object> checkAttendance(MonthlyAttendanceDTO request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("count", attendanceRepository.checkAttendance(request.getEmpId(), request.getDate()));
        return responseMap;
    }

    public List<AttendanceDTO> getAttendanceByMonth(MonthlyAttendanceDTO request) throws DataFormatException, IOException {
        List<AttendanceDTO> responseList = new ArrayList<>();
/*        List<AttendanceEntity> responseList = attendanceRepository.getAttendanceByMonth(request.getEmpId(), request.getMonth())
                .stream()
                .peek(entity -> entity.setPhoto(ImageUtil.decompressImage(entity.getPhoto())))
                .collect(Collectors.toList());*/
        List<AttendanceEntity> entityList = attendanceRepository.getAttendanceByMonth(request.getEmpId(), request.getMonth());
        for (AttendanceEntity entity : entityList) {
            AttendanceDTO attendance = modelMapper.map(entity, AttendanceDTO.class);
            attendance.setBase64Image(Base64.getEncoder().encodeToString(entity.getPhoto()));
            responseList.add(attendance);
        }
        return responseList;
    }

    public List<Map<String, Object>> getAttendanceSummaryByEmp(String empId) {
        return attendanceRepository.getAttendanceSummaryByEmp(empId);
    }

    public List<Map<String, Object>> getAttendanceForEmp(MonthlyAttendanceDTO request) {
        List<Map<String, Object>> attendanceList = attendanceRepository.getAttendanceForEmp(request.getEmpId(), request.getMonth());
        Map<String, Map<String, Object>> attendanceMap = new HashMap<>();
        for (Map<String, Object> attendance : attendanceList) {
            String key = attendance.get("empId") + "_" + attendance.get("date");
            if (!attendanceMap.containsKey(key)) {
                attendanceMap.put(key, new HashMap<>());
                attendanceMap.get(key).put("empId", attendance.get("empId"));
                attendanceMap.get(key).put("date", attendance.get("date"));
                attendanceMap.get(key).put("attendance", new ArrayList<>());
            }
            List<Map<String, Object>> attendanceListForDate = (List<Map<String, Object>>) attendanceMap.get(key).get("attendance");
            Map<String, Object> attendanceForTime = new HashMap<>();
            attendanceForTime.put("clockTime", attendance.get("clockTime"));
            attendanceForTime.put("status", attendance.get("status"));
            attendanceForTime.put("location", attendance.get("location"));
            attendanceForTime.put("latitude", attendance.get("latitude"));
            attendanceForTime.put("longitude", attendance.get("longitude"));
            attendanceForTime.put("logCount", attendance.get("logCount"));
            attendanceListForDate.add(attendanceForTime);
        }
        List<Map<String, Object>> attendanceOutputList = new ArrayList<>(attendanceMap.values());
        return attendanceOutputList;
    }

    public AttendanceEntity markAttendance(AttendanceDTO request) throws IOException {
        String ret = null;
        int logCount = 0;
        AttendanceEntity attendance = modelMapper.map(request, AttendanceEntity.class);

        LocalDate localDate = request.getDate().toLocalDate();
        String clockDate = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
        attendance.setClockDate(clockDate);
        logCount += attendanceRepository.checkAttendance(request.getEmpId(), clockDate);
        attendance.setLogCount(logCount);
            /*long now = System.currentTimeMillis();
            Time sqlTime = new Time(now);*/
        Time sqlTime = Time.valueOf(request.getClockTime());
        attendance.setClockTime(sqlTime);
        return attendanceRepository.save(attendance);
    }

    public AttendanceEntity markAttendance1(AttendanceDTO request) throws IOException {
        String ret = null;
        int logCount = 0;
        AttendanceEntity attendance = modelMapper.map(request, AttendanceEntity.class);

        LocalDate localDate = request.getDate().toLocalDate();
        String clockDate = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
        attendance.setClockDate(clockDate);
        logCount += attendanceRepository.checkAttendance(request.getEmpId(), clockDate);
        attendance.setLogCount(logCount);
        Time sqlTime = Time.valueOf(request.getClockTime());
        attendance.setClockTime(sqlTime);
        byte[] imageBytes = Base64.getDecoder().decode(request.getBase64Image());

        attendance.setPhoto(imageBytes);
        return attendanceRepository.save(attendance);
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

    public byte[] compress(byte[] input) throws IOException {

        Deflater deflater = new Deflater();
        deflater.setInput(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(input.length);

        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();

        byte[] compressedData = outputStream.toByteArray();
        System.out.println("Original: " + input.length / 1024 + " Kb");
        System.out.println("Compressed: " + outputStream.toByteArray().length / 1024 + " Kb");
        return outputStream.toByteArray();
    }

    public byte[] decompress(byte[] compressedData)
            throws IOException {
        // decompress data
        Inflater inflater = new Inflater();
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

        return outputStream.toByteArray();
    }
}
