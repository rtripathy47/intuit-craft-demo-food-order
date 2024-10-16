//package com.food.order.system.security;
////src/main/java/com/intuit/tms/security/JwtAuthenticationFilter.java
//
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
// @Autowired
// private JwtTokenProvider tokenProvider;
//
// @Autowired
// private CustomUserDetailsService customUserDetailsService;
//
// @Override
// protected void doFilterInternal(
//   HttpServletRequest request,
//   HttpServletResponse response,
//   FilterChain filterChain) throws ServletException, IOException {
//     
//     try {
//         String jwt = getJwtFromRequest(request);
//
//         if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
//             Long userId = tokenProvider.getUserIdFromJWT(jwt);
//
//             UserDetails userDetails = customUserDetailsService.loadUserById(userId);
//             UsernamePasswordAuthenticationToken authentication = 
//                 new UsernamePasswordAuthenticationToken(
//                     userDetails, 
//                     null, 
//                     userDetails.getAuthorities());
//             
//             authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//             // Set to Security Context
//             SecurityContextHolder.getContext().setAuthentication(authentication);
//         }
//
//     } catch(Exception ex){
//         System.out.println("Could not set user authentication in security context");
//     }
//
//     filterChain.doFilter(request, response);
// }
//
// private String getJwtFromRequest(HttpServletRequest request){
//     String bearerToken = request.getHeader("Authorization");
//     if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
//         return bearerToken.substring(7, bearerToken.length());
//     }
//     return null;
// }
//}
