package ru.ssk.restvoting;

import ru.ssk.restvoting.model.AbstractBaseEntity;
import ru.ssk.restvoting.model.Role;
import ru.ssk.restvoting.model.User;

import java.util.List;

public class UserTestData {
    public static final int ADMIN_ID = AbstractBaseEntity.START_SEQ;
    public static final int USER_ID = AbstractBaseEntity.START_SEQ + 1;
    public static final User admin = new User(ADMIN_ID,"Admin", "admin@mail.ru", "1111", List.of(Role.ADMIN, Role.USER));
    public static final User user = new User(USER_ID,"User1", "user1@gmail.com", "2222", List.of(Role.USER));
    public static final User invalidUser = new User(USER_ID,"User", "user@gmail.com", "user", List.of(Role.USER));
}
