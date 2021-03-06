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
import ru.ssk.restvoting.util.exception.VoteAlreadyExistException;
import ru.ssk.restvoting.util.exception.VoteDeadlineException;
import ru.ssk.restvoting.util.exception.VoteNotFoundForUpdateException;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.ssk.restvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    private final String VOTE_DEADLINE_MSG = "Voting is not possible after %s";
    private final String VOTE_ALREADY_EXIST_MSG = "Vote for today already exists";
    private final String VOTE_NOT_FOUND_MSG = "Vote not found for update";

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
        if (findToday() != null) {
            throw new VoteAlreadyExistException(VOTE_ALREADY_EXIST_MSG);
        }
        Assert.notNull(restaurantId, "Restaurant id must not be null");
        Date voteDate = Date.valueOf(LocalDate.now());
        User user = userService.getReference(SecurityUtil.getAuthUserId());
        Restaurant restaurant = restaurantService.getReference(restaurantId);
        return crudRepository.save(new Vote(user, restaurant, voteDate));
    }

    public void update(Integer voteId, Integer restaurantId) {
        Assert.notNull(voteId, "Vote id must not be null");
        Vote todayVote = findToday();
        if (todayVote == null || !todayVote.getId().equals(voteId)) {
            throw new VoteNotFoundForUpdateException(VOTE_NOT_FOUND_MSG);
        }
        Assert.notNull(restaurantId, "Restaurant id must not be null");
        LocalDateTime voteDateTime = LocalDateTime.now();
        LocalTime voteDeadline = systemSettings.getVoteDeadline();
        if (voteDateTime.toLocalTime().isAfter(voteDeadline)) {
            throw new VoteDeadlineException(String.format(VOTE_DEADLINE_MSG, voteDeadline));
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
