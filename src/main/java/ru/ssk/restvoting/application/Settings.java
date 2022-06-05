package ru.ssk.restvoting.application;

import java.time.LocalTime;

public class Settings {
    private LocalTime voteDeadline = LocalTime.of(11, 0);

    public LocalTime getVoteDeadline() {
        return voteDeadline;
    }

    public void setVoteDeadline(LocalTime voteDeadline) {
        this.voteDeadline = voteDeadline;
    }
}
