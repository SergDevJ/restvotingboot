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
import ru.ssk.restvoting.model.Dish;
import ru.ssk.restvoting.service.DishService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.ssk.restvoting.util.ValidationUtil.assureIdConsistent;
import static ru.ssk.restvoting.util.ValidationUtil.checkNew;

@RestController
@Api(description = "Endpoints for manage Dishes (for users with admin rights)", produces = "application/json")
@RequestMapping(value = { DishRestController.REST_URL},
        produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController {
    static final String REST_URL = "/rest/admin/dishes";
    private static final Logger log = LoggerFactory.getLogger(DishRestController.class);

    @Autowired
    private DishService service;

    @ApiOperation(value = "Return all dishes", notes = "Retrieving the collection of dishes", response = Dish[].class)
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Dish[].class)})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Dish> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @ApiOperation(value = "Return dish object by id", response = Dish.class)
    @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Dish.class),
            @ApiResponse(code = 404, message = "Data no found")})
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Dish get(@ApiParam(value = "Id of the retrieving dish object. Cannot be empty.", required = true)
                    @PathVariable("id") int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @ApiOperation(value = "Update dish object by id")
    @ApiResponses({@ApiResponse(code = 204, message = "No response content"),
            @ApiResponse(code = 404, message = "Data no found")})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@ApiParam(value = "Id of the updating dish object. Cannot be empty.", required = true)
                       @PathVariable Integer id,
                       @ApiParam(value = "Dish object store in database. Cannot be empty.", required = true)
                       @Valid @RequestBody Dish dish) {
        log.info("update {} with id={}", dish, id);
        assureIdConsistent(dish, id);
        service.update(dish);
    }

    @ApiOperation(value = "Create dish object", response = ResponseEntity.class)
    @ApiResponses({@ApiResponse(code = 201, message = "Successfully created new dish")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Dish> createWithLocation(@ApiParam(value = "Dish object store in database. Cannot be empty.", required = true)
                                                   @Valid @RequestBody Dish dish) {
        checkNew(dish);
        log.info("creating {}", dish);
        Dish created = service.create(dish);
        log.info("created {} with id={}", created, created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "Delete dish object with specified id")
    @ApiResponses({@ApiResponse(code = 204, message = "No response content")})
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@ApiParam(value = "ID of the dish object to be deleted. Cannot be empty.", required = true)
                       @PathVariable int id) {
        log.info("delete with id={}", id);
        service.delete(id);
    }
}
