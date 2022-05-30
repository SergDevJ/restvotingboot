package ru.ssk.restvoting.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.MenuItemDisplayImpl;
import ru.ssk.restvoting.MenuTestData;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.service.RestaurantService;
import ru.ssk.restvoting.util.exception.NotFoundException;
import ru.ssk.restvoting.web.json.JsonUtil;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.ssk.restvoting.RestaurantTestData.*;
import static ru.ssk.restvoting.TestUtil.*;
import static ru.ssk.restvoting.UserTestData.admin;
import static ru.ssk.restvoting.UserTestData.user;

class RestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantAdminRestController.REST_URL;
    private static final String REST_MENU_URL = MenuRestController.REST_URL;

    @Autowired
    private ApplicationContext context;

    private RestaurantService service;

    @PostConstruct
    public void init() {
        service = context.getBean(RestaurantService.class);
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                        .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(restaurants, Restaurant.class));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + RESTAURANT1_ID)
                        .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(restaurant1, Restaurant.class));
    }

    @Test
    void getForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + RESTAURANT1_ID)
                        .with(userHttpBasic(user)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + NOT_FOUND_ID)
                        .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "/" + RESTAURANT1_ID)
                        .with(userHttpBasic(admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated)))
                .andDo(print());
        Assertions.assertThat(service.get(RESTAURANT1_ID)).usingRecursiveComparison().isEqualTo(updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .with(userHttpBasic(admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print());
        Restaurant actual = readFromJson(result, Restaurant.class);
        int newId = actual.getId();
        newRestaurant.setId(newId);
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(newRestaurant);
        Assertions.assertThat(service.get(newId)).usingRecursiveComparison().isEqualTo(newRestaurant);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + RESTAURANT1_ID)
                        .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(RESTAURANT1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + NOT_FOUND_ID)
                        .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getTodayMenu() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REST_MENU_URL + "/" + RESTAURANT1_ID)
                        .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        List<MenuItemDisplayImpl> actual = readValuesFromMvcResult(result, MenuItemDisplayImpl.class);

//        Since when converting a parameter of type java.sql.Date
//        from JSON in readValuesFromMvcResult(), the 'hours' field is filled
//        with the value of the current time zone offset, and the equals()
//        method in the java.sql.Date class is not overridden and takes into account
//        time in milliseconds when comparing, then for a correct comparison,
//        one more conversion is needed to reset the 'hours' field.
        actual = actual.stream().map(m -> new MenuItemDisplayImpl(m.getId(), m.getDishId(), m.getName(), m.getWeight(), m.getPrice(), java.sql.Date.valueOf(m.getDate().toString()))).collect(Collectors.toList());
        assertThat(actual).usingFieldByFieldElementComparator().containsExactlyElementsOf(MenuTestData.todayMenu);
    }

    @Test
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, null, "email", null);
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid))
                        .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(RESTAURANT1_ID, null, "Email", "a");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "/" + RESTAURANT1_ID)
                        .with(userHttpBasic(admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Restaurant clone = new Restaurant(null, restaurant1.getName(), restaurant1.getEmail(), restaurant1.getAddress());
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(clone))
                        .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Restaurant invalid = new Restaurant(RESTAURANT1_ID, restaurant2.getName(), restaurant2.getEmail(), restaurant2.getAddress());
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "/" + RESTAURANT1_ID)
                        .with(userHttpBasic(admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}