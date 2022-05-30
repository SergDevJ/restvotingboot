package ru.ssk.restvoting.to;

import ru.ssk.restvoting.model.AbstractEmailEntity;

public class RestaurantVoteTo extends AbstractEmailEntity {
    private final String address;
    private final Integer voteId;

    public RestaurantVoteTo(Integer id, String name, String email, String address, Integer voteId) {
        super(id, name, email);
        this.address = address;
        this.voteId = voteId;
    }

    public Integer getVoteId() {
        return voteId;
    }

    public String getAddress() {
        return address;
    }
}
