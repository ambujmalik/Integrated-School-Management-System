package com.ismp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ismp.model.User;

/**
 * Repository interface for User entity.
 * Provides CRUD operations and custom query methods for user authentication and management.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by email.
     *
     * @param email the email address
     * @return optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds all users with a specific role.
     *
     * @param userRole the user role
     * @return list of users with the specified role
     */
    List<User> findByUserRole(String userRole);

    /**
     * Finds all active users.
     *
     * @param isActive the active status
     * @return list of active users
     */
    List<User> findByIsActive(Boolean isActive);

    /**
     * Finds all active users with a specific role.
     *
     * @param userRole the user role
     * @param isActive the active status
     * @return list of active users with the specified role
     */
    @Query("SELECT u FROM User u WHERE u.userRole = :userRole AND u.isActive = :isActive")
    List<User> findActiveUsersByRole(@Param("userRole") String userRole, @Param("isActive") Boolean isActive);

    /**
     * Checks if a username already exists.
     *
     * @param username the username
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if an email already exists.
     *
     * @param email the email address
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Counts all active users.
     *
     * @return count of active users
     */
    long countByIsActive(Boolean isActive);

    /**
     * Counts users with a specific role.
     *
     * @param userRole the user role
     * @return count of users with the role
     */
    long countByUserRole(String userRole);
}
