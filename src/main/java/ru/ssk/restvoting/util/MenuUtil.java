package ru.ssk.restvoting.util;

import ru.ssk.restvoting.model.MenuItem;
import ru.ssk.restvoting.to.MenuItemTo;

public class MenuUtil {
    public static MenuItemTo asTo(MenuItem menuItem) {
        if (menuItem == null) return null;
        return new MenuItemTo(menuItem.getId(), menuItem.getRestaurantId(), menuItem.getDate(), menuItem.getDishId(), menuItem.getPrice() / 100.0F);
    }
}
