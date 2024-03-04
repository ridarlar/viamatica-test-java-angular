package com.consulti.api.service;

import com.consulti.api.model.SessionRecord;
import com.consulti.api.model.User;
import com.consulti.api.repository.SessionRecordRepository;
import com.consulti.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionRecordService {

    private final SessionRecordRepository sessionRecordRepository;
    private final UserRepository userRepository;

    @Autowired
    public SessionRecordService(
            SessionRecordRepository sessionRecordRepository,
            UserRepository userRepository
    ) {
        this.sessionRecordRepository = sessionRecordRepository;
        this.userRepository=userRepository;
    }

    public SessionRecord registerAttendance(User user) {
        SessionRecord lastRecord = sessionRecordRepository.findTopByUserOrderByIdDesc(user);

        if (lastRecord == null || lastRecord.getCheckOut() != null) {
            // Create a new record if no history exists or the last history has both check-in and check-out
            SessionRecord newRecord = new SessionRecord(user);
            newRecord.setCheckIn(LocalDateTime.now());
            return sessionRecordRepository.save(newRecord);
        } else if (lastRecord.getCheckIn() != null) {
            // Register check-out if the last history only has check-in and not check-out
            lastRecord.setCheckOut(LocalDateTime.now());
            return sessionRecordRepository.save(lastRecord);
        } else {
            // Create a new record if the last history already has both check-in and check-out
            SessionRecord newRecord = new SessionRecord(user);
            newRecord.setCheckIn(LocalDateTime.now());
            return sessionRecordRepository.save(newRecord);
        }
    }

    public List<SessionRecord> findHistoryByUserName(String userName) {
        try {
            User currentUser = this.userRepository.findUserByUserName(userName);

            if (currentUser != null) {
                return this.sessionRecordRepository.findByUser(currentUser);
            } else {
                // Handle the case where the user is not found
                throw new RuntimeException("User not found with username: " + userName);
            }
        } catch (Exception error) {
            System.out.println("Error retrieving attendance history: " + error.getMessage());
            throw new RuntimeException("Error retrieving attendance history", error);
        }
    }

    public List<SessionRecord> findAllHistory(){
        try {
            List<SessionRecord> history= this.sessionRecordRepository.findAll();
            return history.stream()
                    .filter(record->!record.getUser().getIsDeleted())
                    .collect(Collectors.toList());
        } catch (Exception error) {
            throw new RuntimeException("Error retrieving attendance history", error);
        }
    }

    public SessionRecord registerAttendanceImported(String identityCard, LocalDateTime checkIn, LocalDateTime checkOut, LocalDate date){
        User currentUser =this.userRepository.findByIdentityCard(Integer.parseInt(identityCard));
        SessionRecord importRegister= new SessionRecord();

        importRegister.setUser(currentUser);
        importRegister.setCheckInTime(checkIn);
        importRegister.setCheckOutTime(checkOut);

        return importRegister;
    }

    public void saveRegister(SessionRecord newRegister) {
        this.sessionRecordRepository.save(newRegister);
    }
}

