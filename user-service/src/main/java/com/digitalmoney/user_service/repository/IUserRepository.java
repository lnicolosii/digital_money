package com.digitalmoney.user_service.repository;

import com.digitalmoney.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(long userId);

    Optional<User> findByDni(String dni);

    Optional<User> findByEmail(String email);
}
