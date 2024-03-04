package com.consulti.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table(name="userList")
public class User extends BaseEntity{

    @Getter
    @Column(unique = true)
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;
    private Boolean sessionActive=true;
    private Boolean isAdmin=false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person ownerPerson;

    public User() {
    }

    public User(
            String userName,
            String password,
            Boolean isAdmin,
            String email,
            Boolean sessionActive,
            Person ownerPerson
    ) {
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
        this.email= email;
        this.sessionActive= sessionActive;
        this.ownerPerson=ownerPerson;
    }

    public void setPasswordEncripted(String userPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(userPassword);
    }

}
