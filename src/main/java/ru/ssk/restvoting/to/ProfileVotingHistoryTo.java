package ru.ssk.restvoting.to;

public interface ProfileVotingHistoryTo {
    Integer getRestaurantId();
    String getRestaurantName();
    java.sql.Date getVoteDate();
}
