package ru.ssk.restvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ssk.restvoting.model.User;
import ru.ssk.restvoting.service.UserService;
import ru.ssk.restvoting.service.VoteService;
import ru.ssk.restvoting.to.ProfileVotingHistoryTo;
import ru.ssk.restvoting.util.SecurityUtil;
import ru.ssk.restvoting.util.ValidationUtil;
import ru.ssk.restvoting.util.validation.ValidationGroup;
import ru.ssk.restvoting.web.user.AuthUser;
import ru.ssk.restvoting.web.user.UserValidator;

import javax.validation.groups.Default;
import java.net.URI;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

import static ru.ssk.restvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = { ProfileRestController.REST_URL},
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestController {
    private static final Logger log = LoggerFactory.getLogger(ProfileRestController.class);
    static final String REST_URL = "/rest/profile";

    @Autowired
    private UserService userService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @GetMapping(value = "/personal-data")
    @ResponseStatus(HttpStatus.OK)
    public User get() {
        log.info("get");
        AuthUser user = SecurityUtil.getAuthUser();
        Objects.requireNonNull(user, "No authenticated user");
        return user.getUser();
    }

    @GetMapping(value = "/voting-history")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileVotingHistoryTo> getVotingHistory(
            @RequestParam(value = "start_date", required = false, defaultValue = VoteService.FILTER_DEFAULT_START_DATE) @Nullable Date startDate,
            @RequestParam(value = "end_date", required = false, defaultValue = VoteService.FILTER_DEFAULT_END_DATE) @Nullable Date endDate) {
        log.info("get voting history with start_date = {} and end_date = {}", startDate, endDate);
        return voteService.getProfileVotingHistory(startDate, endDate);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(
            @Validated({ValidationGroup.Password.class, Default.class}) @RequestBody User user) {
        checkNew(user);
        User created = userService.create(user);
        log.info("create {} with id={}", created, created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @Validated({ValidationGroup.Password.class, Default.class}) @RequestBody User user) {
        int authUserId = SecurityUtil.getAuthUserId();
        log.info("update {} with id={}", user, authUserId);
        ValidationUtil.assureIdConsistent(user, authUserId);
        userService.update(user);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        int authUserId = SecurityUtil.getAuthUserId();
        log.info("delete with id={}", authUserId);
        userService.delete(authUserId);
    }
}
