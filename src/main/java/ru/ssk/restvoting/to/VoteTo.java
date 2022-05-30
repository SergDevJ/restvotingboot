package ru.ssk.restvoting.to;

import ru.ssk.restvoting.model.AbstractBaseEntity;

import java.sql.Date;

public class VoteTo extends AbstractBaseEntity {
    private Integer userId;
    private java.sql.Date date;
    private Integer restaurantId;

    public VoteTo(Integer userId, Date date, Integer restaurantId) {
        this.userId = userId;
        this.date = date;
        this.restaurantId = restaurantId;
    }

    public VoteTo(Integer id, Integer userId, Date date, Integer restaurantId) {
        super(id);
        this.userId = userId;
        this.date = date;
        this.restaurantId = restaurantId;
    }
}
