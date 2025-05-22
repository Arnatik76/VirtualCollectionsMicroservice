package kz.example.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal;
        }

        if (principal instanceof UserDetails) {
            try {
                return Long.parseLong(((UserDetails) principal).getUsername());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        if (principal instanceof String && !((String) principal).equalsIgnoreCase("anonymousUser")) {
            System.err.println("Warning: Principal is String, expected Long. User ID might not be retrievable directly: " + principal);
        }
        return null;
    }
}