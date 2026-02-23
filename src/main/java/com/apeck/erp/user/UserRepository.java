package com.apeck.erp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository
 * Data access layer for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find all users by role
     */
    List<User> findByRole(String role);

    /**
     * Find all active users
     */
    List<User> findByIsActiveTrue();

    /**
     * Find all users by department
     */
    List<User> findByDepartment(String department);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
}