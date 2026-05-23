package com.ismp.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Spring Security Configuration
 * 
 * This configuration:
 * - Disables CSRF (suitable for REST APIs)
 * - Configures CORS with externalized origins
 * - Sets up stateless session management for JWT-based authentication
 * - Defines public and protected API endpoints
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${app.cors.allowed-origins:http://localhost:3000}")
	private String allowedOrigins;

	@Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
	private String allowedMethods;

	@Value("${app.cors.max-age:3600}")
	private long maxAge;

	/**
	 * Configures the main security filter chain
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// 1. Disable CSRF (Common for REST APIs)
			.csrf(csrf -> csrf.disable())
			
			// 2. Configure CORS using the CORS configuration source bean
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			
			// 3. Configure authorization rules
			.authorizeHttpRequests(auth -> auth
				// Public endpoints - no authentication required
				.requestMatchers("/api/public/**").permitAll()
				.requestMatchers("/api/auth/login").permitAll()
				.requestMatchers("/api/auth/register").permitAll()
				.requestMatchers("/api/auth/refresh").permitAll()
				
				// Health check endpoint
				.requestMatchers("/actuator/health").permitAll()
				
				// All other /api/** endpoints require authentication
				.requestMatchers("/api/**").authenticated()
				
				// Any other request requires authentication
				.anyRequest().authenticated()
			)
			
			// 4. Configure stateless session management (for JWT)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			
			// 5. Handle exceptions
			.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint((request, response, authException) -> {
					response.setStatus(401);
					response.setContentType("application/json");
					response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
				})
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					response.setStatus(403);
					response.setContentType("application/json");
					response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"Access Denied\"}");
				})
			);

		return http.build();
	}

	/**
	 * CORS Configuration Source Bean
	 * Externalized configuration allows different origins for different environments
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		
		// Parse comma-separated origins from properties
		List<String> origins = Arrays.asList(allowedOrigins.split(","));
		configuration.setAllowedOrigins(origins.stream()
			.map(String::trim)
			.toList());
		
		// Set allowed HTTP methods
		List<String> methods = Arrays.asList(allowedMethods.split(","));
		configuration.setAllowedMethods(methods.stream()
			.map(String::trim)
			.toList());
		
		// Allow all headers
		configuration.setAllowedHeaders(List.of("*"));
		
		// Allow credentials (cookies, authorization headers)
		configuration.setAllowCredentials(true);
		
		// Set max age for preflight requests
		configuration.setMaxAge(maxAge);
		
		// Expose headers that client can access
		configuration.setExposedHeaders(List.of("Authorization", "X-Total-Count"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}

	/**
	 * Password Encoder Bean
	 * Uses BCrypt with default strength (10 rounds)
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
