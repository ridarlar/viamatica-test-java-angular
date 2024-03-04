package com.consulti.api.repository;

import com.consulti.api.model.AttendanceRecord;
import com.consulti.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    AttendanceRecord findTopByUserOrderByIdDesc(User user);

    List<AttendanceRecord> findByUser(User user);

    @Modifying
    @Query("DELETE FROM AttendanceRecord a WHERE a.user = :user")
    void deleteByUser(@Param("user") User user);
}
