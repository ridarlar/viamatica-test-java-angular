package com.consulti.api.service;

import com.consulti.api.model.AttendanceRecord;
import com.consulti.api.model.User;
import com.consulti.api.repository.AttendanceRecordRepository;
import com.consulti.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceRecordService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final UserRepository userRepository;

    @Autowired
    public AttendanceRecordService(
            AttendanceRecordRepository attendanceRecordRepository,
            UserRepository userRepository
    ) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.userRepository=userRepository;
    }

    public AttendanceRecord registerAttendance(User user) {
        AttendanceRecord lastRecord = attendanceRecordRepository.findTopByUserOrderByIdDesc(user);

        if (lastRecord == null || lastRecord.getCheckOut() != null) {
            // Create a new record if no history exists or the last history has both check-in and check-out
            AttendanceRecord newRecord = new AttendanceRecord(user);
            newRecord.setCheckIn(LocalDateTime.now());
            return attendanceRecordRepository.save(newRecord);
        } else if (lastRecord.getCheckIn() != null) {
            // Register check-out if the last history only has check-in and not check-out
            lastRecord.setCheckOut(LocalDateTime.now());
            return attendanceRecordRepository.save(lastRecord);
        } else {
            // Create a new record if the last history already has both check-in and check-out
            AttendanceRecord newRecord = new AttendanceRecord(user);
            newRecord.setCheckIn(LocalDateTime.now());
            return attendanceRecordRepository.save(newRecord);
        }
    }

    public List<AttendanceRecord> findHistoryByUserName(String userName) {
        try {
            User currentUser = this.userRepository.findUserByUserName(userName);

            if (currentUser != null) {
                return this.attendanceRecordRepository.findByUser(currentUser);
            } else {
                // Handle the case where the user is not found
                throw new RuntimeException("User not found with username: " + userName);
            }
        } catch (Exception error) {
            System.out.println("Error retrieving attendance history: " + error.getMessage());
            throw new RuntimeException("Error retrieving attendance history", error);
        }
    }

    public List<AttendanceRecord> findAllHistory(){
        try {
            List<AttendanceRecord> history= this.attendanceRecordRepository.findAll();
            return history.stream()
                    .filter(record->!record.getUser().getIsDeleted())
                    .collect(Collectors.toList());
        } catch (Exception error) {
            throw new RuntimeException("Error retrieving attendance history", error);
        }
    }

    public AttendanceRecord registerAttendanceImported(String identityCard, LocalDateTime checkIn, LocalDateTime checkOut, LocalDate date){
        User currentUser =this.userRepository.findByIdentityCard(Integer.parseInt(identityCard));
        AttendanceRecord importRegister= new AttendanceRecord();

        importRegister.setUser(currentUser);
        importRegister.setCheckInTime(checkIn);
        importRegister.setCheckOutTime(checkOut);

        return importRegister;
    }

    public void saveRegister(AttendanceRecord newRegister) {
        this.attendanceRecordRepository.save(newRegister);
    }
}

