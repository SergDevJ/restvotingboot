package ru.ssk.restvoting.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ssk.restvoting.model.User;
import ru.ssk.restvoting.repository.UserDataJpaRepository;
import ru.ssk.restvoting.util.SecurityUtil;
import ru.ssk.restvoting.web.exception.GlobalControllerExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserValidator implements Validator {
    private final UserDataJpaRepository repository;

    @Autowired
    private HttpServletRequest request;

    public UserValidator(UserDataJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = ((User) target);
        User dbUser;
        if (StringUtils.hasText(user.getEmail())) {
            dbUser = repository.getByEmail(user.getEmail().toLowerCase());
            checkUser(user, dbUser, errors, "email", GlobalControllerExceptionHandler.EXCEPTION_DUPLICATE_USER_EMAIL);
        }
        dbUser = repository.getByNameCaseInsensitive(user.getName());
        checkUser(user, dbUser, errors, "name", GlobalControllerExceptionHandler.EXCEPTION_DUPLICATE_USER_NAME);
    }

    private void checkUser(User user, User dbUser, Errors errors, String field, String errorMessage) {
        if (dbUser != null) {
            Assert.notNull(request, "HttpServletRequest missed");
            if (request.getMethod().equals("PUT")) {
                // it is ok, if update ourself
                int dbId = dbUser.getId();
                if (user.getId() != null && dbId != user.getId()) {
                    errors.rejectValue(field, null, errorMessage);
                }

                // workaround for update with nullable id user
                String requestURI = request.getRequestURI();
                if (!requestURI.endsWith("/" + dbId) && dbId != SecurityUtil.getAuthUserId()) {
                    errors.rejectValue(field, null, errorMessage);
                }
            } else if (request.getMethod().equals("POST")) {
                errors.rejectValue(field, null, errorMessage);
            }
        }
    }
}
