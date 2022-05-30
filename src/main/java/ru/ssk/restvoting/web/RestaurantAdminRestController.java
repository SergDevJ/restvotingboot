package ru.ssk.restvoting.web;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.service.RestaurantService;
import ru.ssk.restvoting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.ssk.restvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = { RestaurantAdminRestController.REST_URL},
        produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Endpoints for manage Restaurants (for users with admin rights)", produces = "application/json")
public class RestaurantAdminRestController {
    static final String REST_URL = "/rest/admin/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantService service;

    @ApiOperation(value = "Return all restaurants", notes = "Retrieving the collection of restaurants", response = Restaurant[].class)
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Restaurant[].class)})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<Restaurant> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @ApiOperation(value = "Return restaurant object by id", response = Restaurant.class)
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Restaurant.class),
            @ApiResponse(code = 404, message = "Data no found")})
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    Restaurant get(@ApiParam(value = "Id of the retrieving restaurant object. Cannot be empty.", required = true)
            @PathVariable("id") int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @ApiOperation(value = "Update restaurant object by id")
    @ApiResponses({@ApiResponse(code = 204, message = "No response content"),
            @ApiResponse(code = 404, message = "Data no found")})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@ApiParam(value = "Id of the updating restaurant object. Cannot be empty.", required = true)
                       @PathVariable("id") Integer id,
                       @ApiParam(value = "Restaurant object store in database. Cannot be empty.", required = true)
                       @Valid @RequestBody Restaurant restaurant) {
        log.info("update {} with id={}", restaurant, id);
        ValidationUtil.assureIdConsistent(restaurant, id);
        service.update(restaurant);
    }

    @ApiOperation(value = "Create restaurant object", response = ResponseEntity.class)
    @ApiResponses({@ApiResponse(code = 201, message = "Successfully created new restaurant")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> createWithLocation(@ApiParam(value = "Restaurant object store in database. Cannot be empty.", required = true)
                                                         @Valid @RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        Restaurant created = service.create(restaurant);
        log.info("create {} with id={}", created, created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "Delete dish object with specified id")
    @ApiResponses({@ApiResponse(code = 204, message = "No response content")})
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@ApiParam(value = "ID of the restaurant object to be deleted. Cannot be empty.", required = true)
                       @PathVariable("id") int id) {
        log.info("delete with id={}", id);
        service.delete(id);
    }
}
