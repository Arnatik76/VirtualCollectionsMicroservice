package kz.example.vccollections.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private SecurityUtil() {}

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            logger.debug("No authenticated user found in security context.");
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            try {
                return Long.parseLong(userDetails.getUsername());
            } catch (NumberFormatException e) {
                logger.error("Failed to parse user ID from UserDetails username: {}", userDetails.getUsername(), e);
                return null;
            }
        } else if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                logger.error("Failed to parse user ID from String principal: {}", principal, e);
                return null;
            }
        } else if (principal instanceof Long) {
            return (Long) principal;
        }


        logger.warn("User principal is of an unexpected type: {}", principal.getClass().getName());
        return null;
    }
}