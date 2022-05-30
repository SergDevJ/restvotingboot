package ru.ssk.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.model.MenuItem;
import ru.ssk.restvoting.to.MenuItemDisplay;

import java.util.List;

@Transactional(readOnly = true)
public interface MenuDataJpaRepository extends JpaRepository<MenuItem, Integer> {
    @Modifying
    @Transactional
    @Query("delete from MenuItem as m where m.id = :id")
    int delete(@Param("id") int id);

    @Query(value = "select m.id as id, d.id as dishId, d.name as name, d.weight as weight, " +
            "((cast(m.price as double)) / 100.0) as price, " +
            "m.date as date " +
            "from MenuItem m inner join Dish d on m.dish.id = d.id " +
            "and m.restaurant.id =:restaurantId and m.date = :date " +
            "order by d.name")
    List<MenuItemDisplay> getAllForDisplay(@Param("restaurantId") int restaurantId,
                                           @Param("date") java.sql.Date date);
}
