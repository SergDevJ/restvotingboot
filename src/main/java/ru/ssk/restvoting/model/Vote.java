package ru.ssk.restvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "votes")
public class Vote extends AbstractBaseEntity {
    public void setUser(User user) {
        this.user = user;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "vote_date")
    @NotNull
    private java.sql.Date date;

    public Vote(Integer id, User user, Restaurant restaurant, Date date) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
    }

    public Vote(User user, Restaurant restaurant, Date date) {
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
    }

    public Vote() {
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public Integer getUserId() {
        return user.getId();
    }

    @JsonIgnore
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Integer getRestaurantId() {
        return restaurant.getId();
    }

    public Date getDate() {
        return date;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return String.format("%s [userId=%s, date=%s, restaurantId=%s]", super.toString(), user.getId(), date, restaurant.getId());
    }

}
