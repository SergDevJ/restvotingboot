package ru.ssk.restvoting.to;

import ru.ssk.restvoting.model.HasId;

import java.sql.Date;

public interface MenuItemDisplay extends HasId {
    Integer getDishId();
    String getName();
    Integer getWeight();
    Float getPrice();
    Date getDate();
}
