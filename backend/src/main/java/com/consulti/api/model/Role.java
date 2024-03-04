package com.consulti.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;

    public Role(String name) {
        this.name=name;
    }
    public Role(){}

    public void setPrivileges(Collection<Privilege> privileges) {
        if (this.privileges == null) {
            this.privileges = new ArrayList<>();
        }
        this.privileges.addAll(privileges);
    }

    public Collection<? extends Privilege> getPrivileges() {
        return this.privileges;
    }

    public String getName() {
        return this.name;
    }
}
