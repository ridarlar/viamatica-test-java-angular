package com.consulti.api.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="attendanceRecord")
public class AttendanceRecord extends BaseEntity{
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    private LocalDateTime checkIn;

    private LocalDateTime  checkOut;

    public AttendanceRecord() {

    }
    public AttendanceRecord(User user) {
        this.user = user;
    }

    public void setIdentityCard(String identityCard) {
        this.user.setIdentityCard(Integer.valueOf(identityCard));
    }

    public void setCheckInTime(LocalDateTime checkInRegister) {
        this.checkIn = checkInRegister;
    }

    public void setCheckOutTime(LocalDateTime checkOutRegister) {
        this.checkOut= checkOutRegister;
    }
}