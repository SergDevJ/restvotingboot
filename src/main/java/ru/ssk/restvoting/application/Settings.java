package ru.ssk.restvoting.application;

import java.time.LocalTime;

public class Settings {
    private LocalTime voteLastTime = LocalTime.of(11, 0);

    public LocalTime getVoteLastTime() {
        return voteLastTime;
    }

    public void setVoteLastTime(LocalTime voteLastTime) {
        this.voteLastTime = voteLastTime;
    }
}
