package com.consulti.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="person")
public class Person extends BaseEntity{
    private String firstName;

    private String lastName;

    @Column(unique = true)
    private Integer identityCard;

    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    public Person(String firstName, String lastName, Integer identityCard, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityCard = identityCard;
        this.dateOfBirth = dateOfBirth;
    }

    public void addUser(User user) {
        users.add(user);
        user.setPerson(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setPerson(null);
    }

}
