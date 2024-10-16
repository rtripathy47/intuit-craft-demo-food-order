//package com.food.order.system.security;
//
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import com.food.order.system.security.model.CustomUserDetails;
//
////src/main/java/com/intuit/tms/security/JwtTokenProvider.java
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.SignatureException;
//import io.jsonwebtoken.UnsupportedJwtException;
//
//@Component
//public class JwtTokenProvider {
//
// @Value("${app.jwtSecret}")
// private String jwtSecret;
//
// @Value("${app.jwtExpirationInMs}")
// private int jwtExpirationInMs;
//
// // Generate JWT token
// public String generateToken(Authentication authentication){
//     CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
//
//     Date now = new Date();
//     Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
//
//     return Jwts.builder()
//             .setSubject(Long.toString(userPrincipal.getId()))
//             .claim("roles", userPrincipal.getAuthorities())
//             .setIssuedAt(new Date())
//             .setExpiration(expiryDate)
//             .signWith(SignatureAlgorithm.HS512, jwtSecret)
//             .compact();
// }
//
// // Get user ID from JWT token
// public Long getUserIdFromJWT(String token){
//     Claims claims = Jwts.parser()
//             .setSigningKey(jwtSecret)
//             .parseClaimsJws(token)
//             .getBody();
//
//     return Long.parseLong(claims.getSubject());
// }
//
// // Validate JWT token
// public boolean validateToken(String authToken){
//     try{
//         Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//         return true;
//     } catch(SignatureException ex){
//         System.out.println("Invalid JWT signature");
//     } catch(MalformedJwtException ex){
//         System.out.println("Invalid JWT token");
//     } catch(ExpiredJwtException ex){
//         System.out.println("Expired JWT token");
//     } catch(UnsupportedJwtException ex){
//         System.out.println("Unsupported JWT token");
//     } catch(IllegalArgumentException ex){
//         System.out.println("JWT claims string is empty.");
//     }
//     return false;
// }
//}
//
