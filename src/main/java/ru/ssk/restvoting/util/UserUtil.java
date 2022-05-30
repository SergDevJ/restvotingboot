package ru.ssk.restvoting.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import ru.ssk.restvoting.model.Role;
import ru.ssk.restvoting.model.User;

import java.util.Collections;


public class UserUtil {
    private UserUtil() {}

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setName(user.getName().trim());
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        if (user.getRoles() == null || user.getRoles().isEmpty()) user.setRoles(Collections.singletonList(Role.USER));
        return user;
    }
}
