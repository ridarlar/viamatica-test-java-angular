package com.consulti.api.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="sessionRecord")
public class SessionRecord extends BaseEntity{
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private LocalDateTime checkIn;

    private LocalDateTime  checkOut;

    public SessionRecord(User user) {
        this.user = user;
    }

    public SessionRecord() {

    }

    public void setCheckInTime(LocalDateTime checkInRegister) {
        this.checkIn = checkInRegister;
    }

    public void setCheckOutTime(LocalDateTime checkOutRegister) {
        this.checkOut= checkOutRegister;
    }
}