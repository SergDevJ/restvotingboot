package ru.ssk.restvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.model.Vote;
import ru.ssk.restvoting.repository.RestaurantDataJpaRepository;
import ru.ssk.restvoting.repository.VoteDataJpaRepository;
import ru.ssk.restvoting.to.RestaurantVoteTo;
import ru.ssk.restvoting.util.SecurityUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ssk.restvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    private final Sort SORT_EMAIL = Sort.by(Sort.Direction.ASC, "email");
    private final RestaurantDataJpaRepository crudRepository;
    private final VoteDataJpaRepository voteRepository;
    private final UserService userService;

    @Autowired
    public RestaurantService(RestaurantDataJpaRepository crudRepository, VoteDataJpaRepository voteRepository, UserService userService) {
        this.crudRepository = crudRepository;
        this.voteRepository = voteRepository;
        this.userService = userService;
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return crudRepository.findAll(SORT_EMAIL);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not be null");
        Assert.notNull(restaurant.getId(), "Restaurant id must not be null");
        crudRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not be null");
        return crudRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudRepository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(crudRepository.findById(id).orElse(null), id);
    }

    public Restaurant getReference(Integer id) {
        return crudRepository.getById(id);
    }

    public List<RestaurantVoteTo> getAllWithUserVote() {
        return getAllWithUserVote(SecurityUtil.getAuthUserId(), java.sql.Date.valueOf(LocalDate.now()));
    }

    public List<RestaurantVoteTo> getAllWithUserVote(int userId, java.sql.Date voteDate) {
        List<Restaurant> restaurants = getAll();
        Vote findVote = voteRepository.findByUserAndDate(userService.getReference(userId), voteDate).orElse(null);
        if (findVote == null) {
            return restaurants.stream().
                    map(r -> new RestaurantVoteTo(r.getId(), r.getName(), r.getEmail(), r.getAddress(), null)).
                    collect(Collectors.toList());
        } else {
            return restaurants.stream().
                    map(r -> new RestaurantVoteTo(r.getId(), r.getName(), r.getEmail(), r.getAddress(),
                            r.getId().equals(findVote.getRestaurant().getId()) ? findVote.getId() : null)).
                    collect(Collectors.toList());
        }
    }
}
