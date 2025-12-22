package com.rentit.repository;

import com.rentit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByResetPasswordToken(String token);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
