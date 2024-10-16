//package com.food.order.system.security;
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.food.order.system.entity.Account;
//import com.food.order.system.repository.AccountRepository;
//import com.food.order.system.security.model.CustomUserDetails;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
// @Autowired
// private AccountRepository accountRepository;
//
// /**
//  * Loads a user by username (email).
//  *
//  * @param email The user's email.
//  * @return CustomUserDetails object.
//  */
// @Override
// @Transactional
// public CustomUserDetails loadUserByUsername(String email) 
//         throws UsernameNotFoundException {
//     Account account = accountRepository.findByEmail(email);
//     if(account == null){
//         throw new UsernameNotFoundException("User not found with email: " + email);
//     }
//
//     return CustomUserDetails.create(account);
// }
//
// /**
//  * Loads a user by ID.
//  *
//  * @param id The user's ID.
//  * @return CustomUserDetails object.
//  */
// @Transactional
// public CustomUserDetails loadUserById(Long id){
//     Account account = accountRepository.findById(id)
//             .orElseThrow(() -> 
//                 new UsernameNotFoundException("User not found with id: " + id)
//             );
//
//     return CustomUserDetails.create(account);
// }
//}
//
