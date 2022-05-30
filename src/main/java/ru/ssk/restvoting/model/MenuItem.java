package ru.ssk.restvoting.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "menu")
public class MenuItem extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private Restaurant restaurant;

    @Column(name ="menu_date")
    @NotNull
    private java.sql.Date date;

    public Restaurant getRestaurant() {
        return restaurant;
    }
    public Integer getRestaurantId() {
        return restaurant.getId();
    }

    public Date getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    @NotNull
    private Dish dish;

    //https://stackoverflow.com/questions/8148684/what-data-type-to-use-for-money-in-java/43051227#43051227
    /**
     * Keep money in cents
     */
    @Column(name = "price")
    @NotNull
    @Range(min = 1L, max = 100000_00L)
    int price;

    public Dish getDish() {
        return dish;
    }
    public Integer getDishId() {
        return dish.getId();
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public MenuItem() {
    }

    public MenuItem(Integer id, Restaurant restaurant, Date date, Dish dish, int price) {
        super(id);
        this.restaurant = restaurant;
        this.date = date;
        this.dish = dish;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", restaurant=" + restaurant.getId() +
                ", date=" + date +
                ", dish=" + dish.getId() +
                ", price=" + price +
                '}';
    }
}
