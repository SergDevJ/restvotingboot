package ru.ssk.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantDataJpaRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("delete from Restaurant r where r.id = :id")
    int delete(@Param("id") int id);
}
