//package com.food.order.system.config;
//
////src/main/java/com/example/foodorder/config/SecurityConfig.java
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
////Import statements for Spring Security
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
////Import statements for Spring Security Filters and other components
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.food.order.system.security.JwtAuthenticationEntryPoint;
//import com.food.order.system.security.JwtAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//public class SecurityConfig {
//
// @Autowired
// private JwtAuthenticationEntryPoint unauthorizedHandler;
//
// @Bean
// public JwtAuthenticationFilter jwtAuthenticationFilter(){
//     return new JwtAuthenticationFilter();
// }
//
// // Define the AuthenticationManager bean
// @Bean
// public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//     return authConfig.getAuthenticationManager();
// }
//
////  Configure HTTP security using lambda-based configuration
// @Bean
// public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
//     http
//         // Disable CSRF as we're using JWT
//         .csrf(csrf -> csrf.disable())
//         // Exception handling
//         .exceptionHandling(exception -> exception
//             .authenticationEntryPoint(unauthorizedHandler)
//         )
//         // Make session stateless
//         .sessionManagement(session -> session
//             .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//         )
//         // Define URL authorization
//         .authorizeHttpRequests(authorize -> authorize
//             // Permit registration and login endpoints
//             .requestMatchers("/api/auth/**").permitAll()
//             // Allow public access to items and restaurants
//             .requestMatchers("/api/items/**", "/api/restaurants/**").permitAll()
//             // Secure other endpoints
//             .anyRequest().authenticated()
//         );
//
//     // Add JWT filter before UsernamePasswordAuthenticationFilter
//     http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//     return http.build();
// }
// 
// @Bean
// public PasswordEncoder passwordEncoder() {
//     return new BCryptPasswordEncoder();
// }
//}
//
