//package com.food.order.system.controller;
//
//
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.food.order.system.entity.Account;
//import com.food.order.system.entity.Role;
//import com.food.order.system.repository.AccountRepository;
//import com.food.order.system.repository.RoleRepository;
//import com.food.order.system.security.JwtTokenProvider;
//import com.food.order.system.security.model.JwtAuthenticationResponse;
//import com.food.order.system.security.model.LoginRequest;
//import com.food.order.system.security.model.RegisterRequest;
//
//import jakarta.validation.Valid;
////
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
// @Autowired
// private AuthenticationManager authenticationManager;
//
// @Autowired
// private AccountRepository accountRepository;
//
// @Autowired
// private RoleRepository roleRepository;
//
// @Autowired
// private PasswordEncoder passwordEncoder;
//
// @Autowired
// private JwtTokenProvider tokenProvider;
// 
// @PostMapping("/register")
// public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
//     // Check if email is already in use
//     if(accountRepository.existsByEmail(registerRequest.getEmail())){
//         return ResponseEntity.badRequest().body("Email Address already in use!");
//     }
//
//     // Create user account
//     Account account = new Account();
//     account.setEmail(registerRequest.getEmail());
//     account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//
//     // Assign role
//     Role userRole;
//     String requestedRole = registerRequest.getRole().toUpperCase();
//     if(requestedRole.equals("CUSTOMER")){
//         userRole = roleRepository.findByRole("ROLE_CUSTOMER");
//     }
//     else if(requestedRole.equals("RESTAURANT")){
//         userRole = roleRepository.findByRole("ROLE_RESTAURANT");
//     }
//     else {
//         return ResponseEntity.badRequest().body("Invalid role specified!");
//     }
//
//     if(userRole == null){
//         // Create the role if it doesn't exist
//         userRole = new Role();
//         userRole.setRole("ROLE_" + requestedRole);
//         userRole.setAccounts(Set.of(account));
//         roleRepository.save(userRole);
//     }
//
//     // Add the role to the account
//     account.getRoles().add(userRole);
//
//     // Saving the account
//     accountRepository.save(account);
//
//     return ResponseEntity.ok("User registered successfully");
// }
//
//
// /**
//  * Authenticates a user and returns a JWT token.
//  *
//  * @param loginRequest The login request containing email and password.
//  * @return JWT token.
//  */
// @PostMapping("/login")
// public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
//
//     Authentication authentication = authenticationManager.authenticate(
//         new UsernamePasswordAuthenticationToken(
//             loginRequest.getEmail(),
//             loginRequest.getPassword()
//         )
//     );
//
//     SecurityContextHolder.getContext().setAuthentication(authentication);
//
//     String jwt = tokenProvider.generateToken(authentication);
//     return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
// }
//}
