package ru.ssk.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.model.User;


@Transactional(readOnly = true)
public interface UserDataJpaRepository extends JpaRepository<User, Integer> {
    User getByEmail(String email);

    @Transactional
    @Modifying
    @Query("delete from User u where u.id = :id")
    int delete(@Param("id") int id);

    @Query("select u from User u where trim(upper(u.name)) = trim(upper(:name))")
    User getByNameCaseInsensitive(@Param("name") String name);
}
