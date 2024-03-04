package com.consulti.api.repository;

import com.consulti.api.model.Person;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRespository extends JpaRepository<Person, Long> {
    Optional<Person> findByIdentityCard(Integer identityCard);
}
