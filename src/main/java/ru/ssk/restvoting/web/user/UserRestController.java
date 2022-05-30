package ru.ssk.restvoting.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ssk.restvoting.model.User;
import ru.ssk.restvoting.service.UserService;
import ru.ssk.restvoting.util.validation.ValidationGroup;

import javax.validation.groups.Default;
import java.net.URI;
import java.util.List;

import static ru.ssk.restvoting.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = { UserRestController.REST_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {
    static final String REST_URL = "/rest/admin/users";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;

    @Autowired
    private UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User get(@PathVariable int id) {
        log.info("get with id={}", id);
        return service.get(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id,
          @Validated({ValidationGroup.Password.class, Default.class}) @RequestBody User user)
    {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createWithLocation(
            @Validated({ValidationGroup.Password.class, Default.class}) @RequestBody User user)
    {
        User created = service.create(user);
        log.info("create {} with if={}", created, created.getId());

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete with id={}", id);
        service.delete(id);
    }
}
