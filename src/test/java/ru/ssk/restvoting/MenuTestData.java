package ru.ssk.restvoting;

import ru.ssk.restvoting.model.Dish;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.to.MenuItemTo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static ru.ssk.restvoting.model.AbstractBaseEntity.START_SEQ;

public class MenuTestData {
    public static final int MENU_ITEM1_ID = START_SEQ;
    public static final int TODAY_MENU_ITEM1_ID = START_SEQ + 3;
    public static final int NOT_FOUND_ID = START_SEQ + 100;

    public static final int RESTAURANT_ID = START_SEQ;
    public static final int DISH_ID = START_SEQ;
    private static final Date MENU_DATE = java.sql.Date.valueOf("2021-05-01");
    private static final Date MENU_DATE_TODAY = Date.valueOf(LocalDate.now());
    public static final MenuItemTo todayMenuItemTo = new MenuItemTo(START_SEQ, RESTAURANT_ID, MENU_DATE_TODAY, DISH_ID, 500F);
    public static final MenuItemTo doubleNewTodayMenuItemTo = new MenuItemTo(RESTAURANT_ID, MENU_DATE_TODAY, DISH_ID, 500F);
    public static final MenuItemTo menuItemTo = new MenuItemTo(START_SEQ, RESTAURANT_ID, Date.valueOf("2021-05-01"), DISH_ID + 1, 100F);

    public static final MenuItemDisplayImpl todayMenuItem1 = new MenuItemDisplayImpl(TODAY_MENU_ITEM1_ID, DISH_ID, "Дичь", 300, 500F, MENU_DATE_TODAY);
    public static final MenuItemDisplayImpl todayMenuItem2 = new MenuItemDisplayImpl(TODAY_MENU_ITEM1_ID + 1, DISH_ID + 1, "Борщ", 250, 150.33F, MENU_DATE_TODAY);
    public static final MenuItemDisplayImpl todayMenuItem3 = new MenuItemDisplayImpl(TODAY_MENU_ITEM1_ID + 2, DISH_ID + 2, "Бифстроганов", 220, 3000.03F, MENU_DATE_TODAY);
    public static final MenuItemDisplayImpl todayMenuItem4 = new MenuItemDisplayImpl(TODAY_MENU_ITEM1_ID + 3, DISH_ID + 5, "Икра", 50, 110.05F, MENU_DATE_TODAY);
    public static final List<MenuItemDisplayImpl> todayMenu = List.of(todayMenuItem3, todayMenuItem2, todayMenuItem1, todayMenuItem4);

    public static final MenuItemDisplayImpl menuItem1 = new MenuItemDisplayImpl(MENU_ITEM1_ID, DISH_ID + 1, "Борщ", 250, 100F, MENU_DATE);
    public static final MenuItemDisplayImpl menuItem2 = new MenuItemDisplayImpl(MENU_ITEM1_ID + 1, DISH_ID + 2, "Бифстроганов", 220, 200.44F, MENU_DATE);
    public static final MenuItemDisplayImpl menuItem3 = new MenuItemDisplayImpl(MENU_ITEM1_ID + 2, DISH_ID + 5, "Икра", 50, 2000.5F, MENU_DATE);
    public static final List<MenuItemDisplayImpl> anyDateMenu = List.of(menuItem2, menuItem1, menuItem3);

    public static MenuItemTo getNew() {
        return new MenuItemTo(null, RESTAURANT_ID, MENU_DATE, START_SEQ + 3, 150.5F);
    }

    public static MenuItemTo createToFromMenuItem(Integer id, Restaurant restaurant, Date date, Dish dish, int price) {
        return new MenuItemTo(id, restaurant.getId(), date, dish.getId(), price/100F);
    }

    public static MenuItemTo getUpdated() {
        return new MenuItemTo(MENU_ITEM1_ID, todayMenuItemTo.getRestaurantId(), todayMenuItemTo.getDate(), todayMenuItemTo.getDishId() + 3, todayMenuItemTo.getPrice() + 5.55F);
    }

    public static MenuItemTo getDuplicatedDish() {
        return new MenuItemTo(MENU_ITEM1_ID, RESTAURANT_ID, MENU_DATE_TODAY, START_SEQ + 1, 100F);
    }
}
