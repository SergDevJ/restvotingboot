package ru.ssk.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.model.Dish;

@Transactional(readOnly = true)
public interface DishDataJpaRepository extends JpaRepository<Dish, Integer> {
    @Modifying
    @Transactional
    @Query("delete from Dish d where d.id = :id")
    int delete(@Param("id") int d);
}
