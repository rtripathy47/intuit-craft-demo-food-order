//package com.food.order.system.security.model;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.security.core.userdetails.UserDetails;
//
//import com.food.order.system.entity.Account;
//
//public class CustomUserDetails implements UserDetails {
//
// private Long id;
// private String email;
// private String password;
// private Collection<? extends GrantedAuthority> authorities;
//
// public CustomUserDetails(Long id, String email, String password, 
//                          Collection<? extends GrantedAuthority> authorities){
//     this.id = id;
//     this.email = email;
//     this.password = password;
//     this.authorities = authorities;
// }
//
// public static CustomUserDetails create(Account account){
//     List<GrantedAuthority> authorities = account.getRoles().stream()
//         .map(role -> new SimpleGrantedAuthority(role.getRole()))
//         .collect(Collectors.toList());
//
//     return new CustomUserDetails(
//         account.getId(),
//         account.getEmail(),
//         account.getPassword(),
//         authorities
//     );
// }
//
// public Long getId(){
//     return id;
// }
//
// @Override
// public String getUsername(){
//     return email;
// }
//
// @Override
// public String getPassword(){
//     return password;
// }
//
// @Override
// public Collection<? extends GrantedAuthority> getAuthorities(){
//     return authorities;
// }
//
// @Override
// public boolean isAccountNonExpired(){
//     return true;
// }
//
// @Override
// public boolean isAccountNonLocked(){
//     return true;
// }
//
// @Override
// public boolean isCredentialsNonExpired(){
//     return true;
// }
//
// @Override
// public boolean isEnabled(){
//     return true;
// }
//}
//
