package ru.ssk.restvoting.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssk.restvoting.model.Dish;
import ru.ssk.restvoting.service.DishService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DishUtil {
    private static DishService service;

    @Autowired
    private DishUtil(DishService service) {
        DishUtil.service = service;
    }

    public static Map<Integer, String> getDishList() {
        List<Dish> list = service.getAll();
        Map<Integer, String> result = new LinkedHashMap<>();
        list.forEach(d -> result.put(d.getId(), d.getName()));
        return result;
    }
}
