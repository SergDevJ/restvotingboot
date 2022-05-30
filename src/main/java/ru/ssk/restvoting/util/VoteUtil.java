package ru.ssk.restvoting.util;

import ru.ssk.restvoting.model.Vote;
import ru.ssk.restvoting.to.VoteTo;

public class VoteUtil {
    public static VoteTo asTo(Vote vote){
        return new VoteTo(vote.getId(), vote.getUserId(), vote.getDate(), vote.getRestaurantId());
    }
}
