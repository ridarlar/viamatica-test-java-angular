package com.consulti.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Getter
@Table(name="userList")
public class User extends BaseEntity{

    @Getter
    @Column(unique = true)
    private String userName;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private Integer identityCard;

    private LocalDate dateOfBirth;

    private String password;

    private Boolean isAdmin=false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public User() {
    }

    public User(String userName, String firstName, String lastName, Integer identityCard, LocalDate dob, String password, Boolean isAdmin) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityCard = identityCard;
        this.dateOfBirth = dob;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordEncripted(String userPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(userPassword);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setIdentityCard(Integer identityCard) {
        this.identityCard = identityCard;
    }

    public void setDob(LocalDate dob) {
        this.dateOfBirth = dob;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin=isAdmin;
    }
}
