package ru.ssk.restvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.ssk.restvoting.model.Dish;
import ru.ssk.restvoting.repository.DishDataJpaRepository;

import java.util.List;

import static ru.ssk.restvoting.util.ValidationUtil.checkNotFoundWithId;


@Service
public class DishService {
    private final DishDataJpaRepository crudRepository;
    private final Sort SORT_NAME = Sort.by("name");

    public DishService(@Autowired DishDataJpaRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public List<Dish> getAll() {
        return crudRepository.findAll(SORT_NAME);
    }

    public Dish get(int id) {
        return checkNotFoundWithId(crudRepository.findById(id).orElse(null), id);
    }

    public void update(Dish dish) {
        Assert.notNull(dish, "Dish must not be null");
        Assert.notNull(dish.getId(), "Dish id must not be null");
        checkNotFoundWithId(crudRepository.save(dish), dish.getId());
    }

    public Dish create(Dish dish) {
        Assert.notNull(dish, "Dish must not be null");
        return crudRepository.save(dish);
    }

    public void delete(int id) {
        checkNotFoundWithId(crudRepository.delete(id) != 0, id);
    }

    public Dish getReference(Integer id) {
        return crudRepository.getById(id);
    }
}
