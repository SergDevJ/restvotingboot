package ru.ssk.restvoting.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import ru.ssk.restvoting.AbstractTest;
import ru.ssk.restvoting.MenuItemDisplayImpl;
import ru.ssk.restvoting.MenuTestData;
import ru.ssk.restvoting.model.MenuItem;
import ru.ssk.restvoting.to.MenuItemDisplay;
import ru.ssk.restvoting.to.MenuItemTo;
import ru.ssk.restvoting.util.exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MenuServiceTest extends AbstractTest {
    @Autowired
    private ApplicationContext context;

    MenuService menuService;

    @PostConstruct
    public void init() {
        menuService = context.getBean(MenuService.class);
    }

    @Test
    void getTodayMenu() {
        List<MenuItemDisplay> actual = menuService.getAll(MenuTestData.RESTAURANT_ID, null).stream().
                map(mi -> new MenuItemDisplayImpl(mi.getId(), mi.getDishId(), mi.getName(), mi.getWeight(), mi.getPrice(), mi.getDate())).
                collect(Collectors.toList());
        assertThat(actual).usingFieldByFieldElementComparator().containsExactlyElementsOf(MenuTestData.todayMenu);
    }

    @Test
    void getMenuHistory() {
        List<MenuItemDisplay> actual = menuService.getAll(MenuTestData.RESTAURANT_ID, Date.valueOf("2021-05-01")).stream().
                map(mi -> new MenuItemDisplayImpl(mi.getId(), mi.getDishId(), mi.getName(), mi.getWeight(), mi.getPrice(), mi.getDate())).
                collect(Collectors.toList());
        assertThat(actual).usingFieldByFieldElementComparator().containsExactlyElementsOf(MenuTestData.anyDateMenu);
    }

    @Test
    void getTo() {
        MenuItemTo actual = menuService.getTo(MenuTestData.MENU_ITEM1_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(MenuTestData.menuItemTo);
    }

    @Test
    void getToNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> menuService.getTo(MenuTestData.NOT_FOUND_ID));
    }

    @Test
    void update() {
        MenuItemTo updated = MenuTestData.getUpdated();
        menuService.update(updated);
        MenuItemTo mi = menuService.getTo(updated.getId());
        MenuItemTo actual = new MenuItemTo(mi.getId(), mi.getRestaurantId(), mi.getDate(), mi.getDishId(), mi.getPrice());
        assertThat(actual).usingRecursiveComparison().isEqualTo(updated);
    }

    @Test
    void create() {
        MenuItem created = menuService.create(MenuTestData.getNew());
        MenuItemTo createdTo = MenuTestData.createToFromMenuItem(created.getId(), created.getRestaurant(), created.getDate(), created.getDish(), created.getPrice());
        int newId = created.getId();
        MenuItemTo newMenuItemTo = MenuTestData.getNew();
        newMenuItemTo.setId(newId);
        assertThat(createdTo).usingRecursiveComparison().isEqualTo(newMenuItemTo);
        assertThat(menuService.getTo(newId)).usingRecursiveComparison().isEqualTo(newMenuItemTo);
    }

    @Test
    void delete() {
        menuService.delete(MenuTestData.TODAY_MENU_ITEM1_ID);
        Assertions.assertThrows(NotFoundException.class, () -> menuService.getTo(MenuTestData.TODAY_MENU_ITEM1_ID));
    }

    @Test
    void deleteNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> menuService.delete(MenuTestData.NOT_FOUND_ID));
    }

    @Test
    void updateDuplicatedDish() {
        MenuItemTo duplicatedDish = MenuTestData.getDuplicatedDish();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> menuService.update(duplicatedDish));
    }

    @Test
    void createWithException() {
        Assertions.assertThrows(PersistenceException.class, () -> menuService.create(MenuTestData.doubleNewTodayMenuItemTo));
        validateCause(SQLIntegrityConstraintViolationException.class, "menu_unique_rest_date_dish_idx", () -> menuService.create(MenuTestData.doubleNewTodayMenuItemTo));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> menuService.create(MenuTestData.todayMenuItemTo));
        Assertions.assertThrows(IllegalArgumentException.class, () -> menuService.create(new MenuItemTo(MenuTestData.RESTAURANT_ID, null, null, 100F)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> menuService.create(new MenuItemTo(null, null, MenuTestData.DISH_ID, 100F)));
    }
}