package vn.utc.service.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vn.utc.service.dtos.UserPrincipal;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

  @Value("${app.jwt.secret}")
  private String jwtSecret;

  @Value("${app.jwt.expiration}")
  private int jwtExpirationInMs;

  // Generate JWT token
  public String generateToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    // Add roles to claims
    List<String> roles =
        userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

    return Jwts.builder()
        .setSubject(userPrincipal.getUsername())
        .claim("userId", userPrincipal.getId())
        .claim("roles", roles)
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  // Extract username from token
  public String getUsernameFromJWT(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

    return claims.getSubject();
  }

  // Get user ID from token
  public Long getUserIdFromJWT(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

    return Long.parseLong(claims.get("userId").toString());
  }

  // Get username from http request
  public String getUsernameFromRequest(HttpServletRequest request) {
    String token = getJwtFromRequest(request);
    if (StringUtils.hasText(token) && validateToken(token)) {
      return getUsernameFromJWT(token);
    }
    return null;
  }

  // get roles from http request
  public List<String> getRolesFromRequest(HttpServletRequest request) {
    String token = getJwtFromRequest(request);
    if (StringUtils.hasText(token) && validateToken(token)) {
      Claims claims =
          Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
      return (List<String>) claims.get("roles");
    }
    return new ArrayList<>();
  }

  // Validate token
  public boolean validateToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException ex) {
      // Invalid JWT token
      log.error("Invalid JWT token");
      return false;
    } catch (ExpiredJwtException ex) {
      // Expired JWT token
      log.error("Expired JWT token");
      return false;
    } catch (UnsupportedJwtException ex) {
      // Unsupported JWT token
      log.error("Unsupported JWT token");
      return false;
    } catch (IllegalArgumentException ex) {
      // JWT claims string is empty
      log.error("JWT claims string is empty");
      return false;
    }
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
