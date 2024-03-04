package com.consulti.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@ToString
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="isDeleted", nullable = false)
    private Boolean isDeleted=false;

    @Column(name="createdAt",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updatedAt")
    private LocalDateTime updatedAt;

    @Column(name="updatedBy")
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setDelete() {
        this.isDeleted= true;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setUpdatedBy(String userName) {
        this.updatedBy = userName;
    }

    public void setIsDelete() {
        this.isDeleted=true;
    }
}
