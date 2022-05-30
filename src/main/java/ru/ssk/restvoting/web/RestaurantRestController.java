package ru.ssk.restvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.service.RestaurantService;
import ru.ssk.restvoting.to.RestaurantVoteTo;

import java.util.List;

@RestController
@RequestMapping(value = { RestaurantRestController.REST_URL},
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    static final String REST_URL = "/rest/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<Restaurant> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @GetMapping(value = "/with-vote")
    @ResponseStatus(HttpStatus.OK)
    List<RestaurantVoteTo> getAllWithVote() {
        log.info("getAllWithVote");
        return service.getAllWithUserVote();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    Restaurant get(@PathVariable("id") int id) {
        log.info("get {}", id);
        return service.get(id);
    }
}
