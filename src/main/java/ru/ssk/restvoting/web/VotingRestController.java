package ru.ssk.restvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ssk.restvoting.model.Vote;
import ru.ssk.restvoting.repository.VoteDataJpaRepository;
import ru.ssk.restvoting.service.UserService;
import ru.ssk.restvoting.service.VoteService;
import ru.ssk.restvoting.to.TodayVoteTo;
import ru.ssk.restvoting.to.VoteTo;
import ru.ssk.restvoting.util.SecurityUtil;
import ru.ssk.restvoting.util.VoteUtil;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static ru.ssk.restvoting.web.VotingRestController.REST_URL;

@RestController
@RequestMapping(value = { REST_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
public class VotingRestController {
    static final String REST_URL = "/rest/votes";
    private static final Logger log = LoggerFactory.getLogger(VotingRestController.class);
    private final VoteService service;
    private final UserService userService;
    private final VoteDataJpaRepository voteRepository;

    @Autowired
    public VotingRestController(VoteService service, UserService userService, VoteDataJpaRepository voteRepository) {
        this.service = service;
        this.userService = userService;
        this.voteRepository = voteRepository;
    }

    @GetMapping(value = "/today")
    @ResponseStatus(HttpStatus.OK)
    public TodayVoteTo getToday() {
        Optional<TodayVoteTo> vote = voteRepository.getToday(userService.getReference(SecurityUtil.getAuthUserId()),
                Date.valueOf(LocalDate.now()));
        return vote.orElse(null);
    }

    @PostMapping
    public ResponseEntity<Vote> createWithLocation(@RequestParam("restaurantId") Integer restaurantId) {
        Vote created = service.create(restaurantId);
        log.info("created {} with id={}", created, created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer id,
                       @RequestParam("restaurantId") Integer restaurantId) {
        log.info("update vote with id={} with restaurantId={}", id, restaurantId);
        service.update(id, restaurantId);
    }
}
