package ru.ssk.restvoting;

import ru.ssk.restvoting.to.MenuItemDisplay;

import java.beans.ConstructorProperties;
import java.sql.Date;

public class MenuItemDisplayImpl implements MenuItemDisplay {
    private Integer id;
    private Integer dishId;
    private String name;
    private Float price;
    private Integer weight;
    private Date date;

    @ConstructorProperties({"id", "dishId", "name", "weight", "price", "date"})
    public MenuItemDisplayImpl(Integer id, Integer dishId, String name, Integer weight, Float price, Date date) {
        this.id = id;
        this.dishId = dishId;
        this.name = name;
        this.weight = weight;
        this.price = price;
        this.date = date;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getDishId() {
        return dishId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getWeight() {
        return weight;
    }

    @Override
    public Float getPrice() {
        return price;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "MenuItemDisplayImpl{" +
                "id=" + id +
                ", dishId='" + dishId +
                ", name='" + name +
                ", price=" + price +
                ", weight=" + weight +
                ", date=" + date +
                '}';
    }
}
