package com.ismp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	// Custom method: Spring Boot will automatically write the SQL to find a user by their email!
    Optional<User> findByEmail(String email);
    
 // This allows the AuthService to find a user by their username string
    Optional<User> findByUsername(String username);
    
    // Optional: Useful if you want to check if an email is already taken
    Boolean existsByEmail(String email);
	
}
