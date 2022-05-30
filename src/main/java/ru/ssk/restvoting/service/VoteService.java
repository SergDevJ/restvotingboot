package ru.ssk.restvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.ssk.restvoting.application.Settings;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.model.User;
import ru.ssk.restvoting.model.Vote;
import ru.ssk.restvoting.repository.VoteDataJpaRepository;
import ru.ssk.restvoting.to.ProfileVotingHistoryTo;
import ru.ssk.restvoting.util.SecurityUtil;
import ru.ssk.restvoting.util.exception.TooLateVoteException;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.ssk.restvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    private final String TOO_LATE_VOTE_MSG = "Voting is not possible after %s";

    public static final String FILTER_DEFAULT_START_DATE = "1900-01-01";
    public static final String FILTER_DEFAULT_END_DATE = "2100-01-01";

    private final VoteDataJpaRepository crudRepository;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final Settings systemSettings;

    @Autowired
    public VoteService(VoteDataJpaRepository crudRepository, UserService userService,
                       RestaurantService restaurantService,
                       Settings systemSettings) {
        this.crudRepository = crudRepository;
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.systemSettings = systemSettings;
    }

    public Vote create(Integer restaurantId) {
        Assert.notNull(restaurantId, "Restaurant id must not be null");
        Date voteDate = Date.valueOf(LocalDate.now());
        User user = userService.getReference(SecurityUtil.getAuthUserId());
        Restaurant restaurant = restaurantService.getReference(restaurantId);
        return crudRepository.save(new Vote(user, restaurant, voteDate));
    }

    public void update(Integer voteId, Integer restaurantId) {
        Assert.notNull(voteId, "Vote id must not be null");
        Assert.notNull(restaurantId, "Restaurant id must not be null");
        LocalDateTime voteDateTime = LocalDateTime.now();
        LocalTime voteLastTime = systemSettings.getVoteLastTime();
        if (voteDateTime.toLocalTime().isAfter(voteLastTime)) {
            throw new TooLateVoteException(String.format(TOO_LATE_VOTE_MSG, voteLastTime));
        }
        User user = userService.getReference(SecurityUtil.getAuthUserId());
        Restaurant restaurant = restaurantService.getReference(restaurantId);
        Date voteDate = Date.valueOf(voteDateTime.toLocalDate());
        Vote updated = new Vote(voteId, user, restaurant, voteDate);
        checkNotFoundWithId(crudRepository.save(updated), updated.getId());
    }

    public Vote findToday() {
        return crudRepository.findByUserAndDate(userService.getReference(SecurityUtil.getAuthUserId()),
                Date.valueOf(LocalDate.now())).orElse(null);
    }

    public List<ProfileVotingHistoryTo> getProfileVotingHistory(Date startDate, Date endDate) {
        int userId = SecurityUtil.getAuthUserId();
        if (startDate == null) startDate = Date.valueOf(FILTER_DEFAULT_START_DATE);
        if (endDate == null) endDate = Date.valueOf(FILTER_DEFAULT_END_DATE);
        return crudRepository.getVotingHistory(userId, startDate, endDate);
    }
}
