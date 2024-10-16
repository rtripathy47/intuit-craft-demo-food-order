package com.food.order.system.security.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
 @NotBlank
 @Email
 private String email; // Used as username

 @NotBlank
 @Size(min = 6, max = 20)
 private String password;

 @NotBlank
 private String role; // "CUSTOMER" or "RESTAURANT"

 // Constructors
 public RegisterRequest() {}

 public RegisterRequest(String email, String password, String role) {
     this.email = email;
     this.password = password;
     this.role = role;
 }

 // Getters and Setters

 public String getEmail(){
     return email;
 }

 public void setEmail(String email){
     this.email = email;
 }

 public String getPassword(){
     return password;
 }

 public void setPassword(String password){
     this.password = password;
 }

 public String getRole(){
     return role;
 }

 public void setRole(String role){
     this.role = role;
 }
}
