package ru.ssk.restvoting.web.user;

import ru.ssk.restvoting.model.Role;
import ru.ssk.restvoting.model.User;

import java.util.Set;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthUser(User user) {
        super(user.getName(), user.getPassword(), true, true,true, true, user.getRoles());
        this.user = new User(user.getId(), user.getName(), user.getEmail(), user.getPassword());
        this.user.setRoles(user.getRoles());
    }

    public int getId() {
        return user.getId();
    }

    public Set<Role> getRoles() {
        return user.getRoles();
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return user.toString();
    }


}
