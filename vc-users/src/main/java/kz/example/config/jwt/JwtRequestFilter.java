package kz.example.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    private final String jwtSecret;
    private SecretKey key;

    public JwtRequestFilter(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @jakarta.annotation.PostConstruct
    public void initKey() {
        try {
            this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to get JWT secret bytes", e);
            throw new RuntimeException("Failed to get JWT secret bytes", e);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        Long userId = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                        .parseClaimsJws(jwtToken).getBody();
                Object userIdObj = claims.get("userId");
                if (userIdObj instanceof Number) {
                    userId = ((Number) userIdObj).longValue();
                } else {
                    logger.warn("userId claim is missing or not a Number in JWT: " + userIdObj);
                }
            } catch (IllegalArgumentException e) {
                logger.error("JWT Token parsing error or unable to get JWT Token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token has expired", e);
            } catch (SignatureException e) {
                logger.error("Invalid JWT signature", e);
            } catch (MalformedJwtException e) {
                logger.error("Invalid JWT token", e);
            } catch (UnsupportedJwtException e) {
                logger.error("Unsupported JWT token", e);
            } catch (Exception e) {
                logger.error("Other JWT error", e);
            }
        } else {
             logger.warn("JWT Token does not begin with Bearer String or is not present");
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userId, null, new ArrayList<>());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.debug("User ID '" + userId + "' authenticated successfully and set in security context.");
        }
        chain.doFilter(request, response);
    }
}