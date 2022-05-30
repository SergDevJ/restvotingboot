package ru.ssk.restvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ssk.restvoting.model.MenuItem;
import ru.ssk.restvoting.service.MenuService;
import ru.ssk.restvoting.to.MenuItemTo;
import ru.ssk.restvoting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;

import static ru.ssk.restvoting.util.ValidationUtil.assureIdConsistent;


@RestController
@RequestMapping(value = { MenuItemAdminRestController.REST_URL},
        produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuItemAdminRestController {
    static final String REST_URL = "/rest/admin/menu";
    private static final Logger log = LoggerFactory.getLogger(MenuItemAdminRestController.class);

    @Autowired
    MenuService service;

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MenuItemTo getTo(@PathVariable("id") int id) {
        log.info("getTo {}", id);
        return service.getTo(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer id,
                       @Valid @RequestBody MenuItemTo menuItemTo) {
        log.info("update {} with id={}", menuItemTo, id);
        assureIdConsistent(menuItemTo, id);
        service.update(menuItemTo);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> createWithLocation(@Valid @RequestBody MenuItemTo menuItemTo) {
        ValidationUtil.checkNew(menuItemTo);
        MenuItem created = service.create(menuItemTo);
        log.info("create {} with id={}", created, created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        log.info("delete with id={}", id);
        service.delete(id);
    }
}
