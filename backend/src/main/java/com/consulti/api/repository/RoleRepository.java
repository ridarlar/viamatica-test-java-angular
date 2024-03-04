package com.consulti.api.repository;

import com.consulti.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findOneByName(String name);

    Role findByName(String name);
}
