package ru.ssk.restvoting.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.ssk.restvoting.util.ValidationUtil;
import ru.ssk.restvoting.util.exception.*;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class GlobalControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    static final String MSG_APP_ERROR = "Application error";
    static final String MSG_DATA_NOT_FOUND = "Data not found";
    static final String MSG_DATA_ERROR = "Data error";
    static final String MSG_VALIDATION_ERROR = "Validation error";
    static final String MSG_WRONG_REQUEST = "Wrong request";

    static final Map<String, HttpStatus> ERRORS = Map.of(
            MSG_APP_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
            MSG_DATA_NOT_FOUND, HttpStatus.UNPROCESSABLE_ENTITY,
            MSG_DATA_ERROR, HttpStatus.CONFLICT,
            MSG_VALIDATION_ERROR, HttpStatus.UNPROCESSABLE_ENTITY,
            MSG_WRONG_REQUEST, HttpStatus.BAD_REQUEST
    );

    public static final String EXCEPTION_DUPLICATE_USER_NAME = "User with this name already exists";
    public static final String EXCEPTION_DUPLICATE_USER_EMAIL = "User with this email already exists";
    public static final String EXCEPTION_DUPLICATE_DISH_NAME_WEIGHT = "Dish with this name and weight already exists";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT_NAME = "Restaurant with this name already exists";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT_ADDRESS = "Restaurant with this address already exists";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT_EMAIL = "Restaurant with this email already exists";
    public static final String EXCEPTION_FKEY_MENU_DISH = "This dish is used in the menu";
    public static final String EXCEPTION_DUPLICATE_MENU_DISH = "This dish already exists in menu";
    public static final String EXCEPTION_DATA_INTEGRITY_VIOLATION = "Violation of data integrity. Perhaps the entry is in use.";

    private static final Map<String, String> CONSTRAINTS_MSG = Map.of(
            "users_unique_name_idx", EXCEPTION_DUPLICATE_USER_NAME,
            "users_unique_email_idx", EXCEPTION_DUPLICATE_USER_EMAIL,
            "dishes_unique_name_weight_idx", EXCEPTION_DUPLICATE_DISH_NAME_WEIGHT,
            "restaurants_unique_name_idx", EXCEPTION_DUPLICATE_RESTAURANT_NAME,
            "restaurants_address_idx", EXCEPTION_DUPLICATE_RESTAURANT_ADDRESS,
            "restaurants_email_idx", EXCEPTION_DUPLICATE_RESTAURANT_EMAIL,
            "menu_dish_id_fkey", EXCEPTION_FKEY_MENU_DISH,
            "menu_unique_rest_date_dish_idx", EXCEPTION_DUPLICATE_MENU_DISH);

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorInfo> dataNotFoundError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, MSG_DATA_NOT_FOUND);
    }

    @ExceptionHandler(TooLateVoteException.class)
    ResponseEntity<ErrorInfo> tooLateVote(HttpServletRequest req, TooLateVoteException e) {
        return logAndGetErrorInfo(req, e, MSG_VALIDATION_ERROR);
    }

    @ExceptionHandler(UserDeleteViolationException.class)
    ResponseEntity<ErrorInfo> userDeleteViolation(HttpServletRequest req, UserDeleteViolationException e) {
        return logAndGetErrorInfo(req, e, MSG_WRONG_REQUEST);
    }

    @ExceptionHandler(UserUpdateViolationException.class)
    ResponseEntity<ErrorInfo> userUpdateViolation(HttpServletRequest req, UserUpdateViolationException e) {
        return logAndGetErrorInfo(req, e, MSG_WRONG_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, PersistenceException.class})
    ResponseEntity<ErrorInfo> dataIntegrityViolation(HttpServletRequest req, Exception e) {
        String rootMessage = ValidationUtil.getRootCause(e).getMessage();
        if (rootMessage != null) {
            for (Map.Entry<String, String> entry : CONSTRAINTS_MSG.entrySet()) {
                if (rootMessage.toLowerCase().contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, MSG_VALIDATION_ERROR, entry.getValue());
                }
            }
        }
        if (e instanceof DataIntegrityViolationException || e.getMessage().toLowerCase().contains("constraintviolationexception")) {
            return logAndGetErrorInfo(req, e, MSG_VALIDATION_ERROR, EXCEPTION_DATA_INTEGRITY_VIOLATION);
        }
        return logAndGetErrorInfo(req, e, MSG_DATA_ERROR);
    }

    @ExceptionHandler(BindException.class)
    ResponseEntity<ErrorInfo> bindError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .distinct()
                .toArray(String[]::new);
        return logAndGetErrorInfo(req, e, MSG_VALIDATION_ERROR, details);
    }

    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorInfo> illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, MSG_VALIDATION_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, MSG_APP_ERROR);
    }

    ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e, String messageText, String... details) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        HttpStatus status = ERRORS.get(messageText);
        log.warn("Error at request  {}: {} (status: {})", req.getRequestURL(), rootCause.toString(), status.toString());
        return ResponseEntity.status(status).
                body(new ErrorInfo(req.getRequestURL(), messageText,
                        details.length != 0 ? details : new String[] {ValidationUtil.getMessage(rootCause)}, status));
    }
}
