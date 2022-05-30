package ru.ssk.restvoting.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.ssk.restvoting.model.Role;
import ru.ssk.restvoting.web.user.AuthUser;

import java.util.Objects;

public class SecurityUtil {
    private SecurityUtil() {}

    public static AuthUser getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthUser) ? (AuthUser) principal : null;
    }

    public static int getAuthUserId() {
        AuthUser user = getAuthUser();
        Objects.requireNonNull(user, "No authenticated user");
        return user.getId();
    }

    public static boolean isAdminUser() {
        AuthUser user = getAuthUser();
        Objects.requireNonNull(user, "No authenticated user");
        return user.getRoles().contains(Role.ADMIN);
    }
}
