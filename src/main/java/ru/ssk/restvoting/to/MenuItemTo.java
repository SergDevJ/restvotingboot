package ru.ssk.restvoting.to;

import ru.ssk.restvoting.model.HasId;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.sql.Date;

public class MenuItemTo implements HasId {
    private Integer id;

    @NotNull
    private Integer restaurantId;

    private java.sql.Date date;

    @NotNull
    private Integer dishId;

    @DecimalMin("0.009")
    @DecimalMax("100000")
    @NotNull
    private Float price;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MenuItemTo() {
    }

    public MenuItemTo(Integer id, Integer restaurantId, Date date, Integer dishId, Float price) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.date = date;
        this.dishId = dishId;
        this.price = price;
    }

    public MenuItemTo(Integer restaurantId, Date date, Integer dishId, Float price) {
        this.restaurantId = restaurantId;
        this.date = date;
        this.dishId = dishId;
        this.price = price;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MenuItemTo{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", date=" + date +
                ", dishId=" + dishId +
                ", price=" + price +
                '}';
    }
}
