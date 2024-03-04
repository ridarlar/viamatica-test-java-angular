package com.consulti.api.repository;

import com.consulti.api.model.SessionRecord;
import com.consulti.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SessionRecordRepository extends JpaRepository<SessionRecord, Long> {
    SessionRecord findTopByUserOrderByIdDesc(User user);

    List<SessionRecord> findByUser(User user);

    @Modifying
    @Query("DELETE FROM SessionRecord a WHERE a.user = :user")
    void deleteByUser(@Param("user") User user);
}
