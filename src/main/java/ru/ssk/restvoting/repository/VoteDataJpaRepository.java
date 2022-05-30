package ru.ssk.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.model.User;
import ru.ssk.restvoting.model.Vote;
import ru.ssk.restvoting.to.ProfileVotingHistoryTo;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteDataJpaRepository extends JpaRepository<Vote, Integer> {
    @Query(value = "select r.id as restaurantId, r.name as restaurantName, v.date as voteDate " +
            "from Vote v inner join Restaurant r on v.restaurant.id = r.id " +
            "where v.user.id = :user_id and v.date between :start_date and :end_date " +
            "order by v.date desc")
    List<ProfileVotingHistoryTo> getVotingHistory(@Param("user_id") int userId,
                                                  @Param("start_date") Date startDate,
                                                  @Param("end_date") Date endDate);

    @Query("select v from Vote v where v.user = :user and v.date = :date")
    Optional<Vote> findByUserAndDate(@Param("user") User user, @Param("date") Date date);
}
