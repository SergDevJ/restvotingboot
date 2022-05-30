package ru.ssk.restvoting;

import ru.ssk.restvoting.model.AbstractBaseEntity;
import ru.ssk.restvoting.model.Restaurant;

import java.util.List;

public class RestaurantTestData {
    public static final int RESTAURANT1_ID = AbstractBaseEntity.START_SEQ;
    public static final int RESTAURANT2_ID = RESTAURANT1_ID + 1;
    public static final int NOT_FOUND_ID = AbstractBaseEntity.START_SEQ + 100;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Плакучая ива", "piva@yandex.ru", "г.Москва");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT1_ID + 1, "лакучая ива", "nodrink@mail.ru", "г.Сочи");
    public static final List<Restaurant> restaurants = List.of(restaurant2, restaurant1);

    public static Restaurant getNew() {
        return new Restaurant(null, "Новый ресторан", "new@mail.ru", "г.Коломна");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Обновленный ресторан", "updated@yandex.ru", "г.Новосибирск");
    }
}
