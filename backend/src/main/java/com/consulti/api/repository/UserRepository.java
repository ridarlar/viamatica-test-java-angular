package com.consulti.api.repository;

import com.consulti.api.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    List<User> findByDateOfBirth(LocalDate parsedDateOfBirth);

    User findUserByUserName(String userName);

    User findByIdentityCard(Integer identityCard);

    void delete(@NotNull User currentUser);
}
