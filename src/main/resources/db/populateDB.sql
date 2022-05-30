DELETE FROM menu;
DELETE FROM dishes;
DELETE FROM votes;
DELETE FROM restaurants;
DELETE FROM users;
DELETE FROM user_roles;

ALTER SEQUENCE rest_seq RESTART WITH 1000;
ALTER SEQUENCE menu_seq RESTART WITH 1000;
ALTER SEQUENCE dish_seq RESTART WITH 1000;
ALTER SEQUENCE user_seq RESTART WITH 1000;
ALTER SEQUENCE votes_seq RESTART WITH 10000;


INSERT INTO restaurants (name, address, email) VALUES ('Плакучая ива', 'г.Москва', 'piva@yandex.ru');
INSERT INTO restaurants (name, address, email) VALUES ('лакучая ива', 'г.Сочи', 'nodrink@mail.ru');

INSERT INTO dishes (name, weight) VALUES ('Дичь', 300);
INSERT INTO dishes (name, weight) VALUES ('Борщ', 250);
INSERT INTO dishes (name, weight) VALUES ('Бифстроганов', 220);
INSERT INTO dishes (name, weight) VALUES ('Картофель-фри', 180);
INSERT INTO dishes (name, weight) VALUES ('Спагетти', 235);
INSERT INTO dishes (name, weight) VALUES ('Икра', 50);

INSERT INTO users (name, email, password) VALUES ('Admin', 'admin@mail.ru', '{noop}1111');
INSERT INTO users (name, email, password) VALUES ('User1', 'user1@gmail.com', '{noop}2222');
INSERT INTO users (name, email, password) VALUES ('User2', 'user2@mail.ru', '{noop}3333');
INSERT INTO users (name, email, password) VALUES ('User3', 'user3@mail.ru', '{noop}abcd');

INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1000, '2021-05-01', 1001, 10000);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1000, '2021-05-01', 1002, 20044);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1000, '2021-05-01', 1005, 200050);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1000, now(), 1000, 50000);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1000, now(), 1001, 15033);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1000, now(), 1002, 300003);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1000, now(), 1005, 11005);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1001, '2021-05-01', 1001, 10000);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1001, '2021-05-01', 1002, 20055);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1001, '2021-05-01', 1005, 200044);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1001, now(), 1005, 10007);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1001, now(), 1004, 25000);
INSERT INTO menu (restaurant_id, menu_date, dish_id, price) VALUES (1001, now(), 1003, 515399);

INSERT INTO votes (user_id, vote_date, restaurant_id) VALUES (1000, '2021-05-01', 1000);
INSERT INTO votes (user_id, vote_date, restaurant_id) VALUES (1000, now(), 1001);
INSERT INTO votes (user_id, vote_date, restaurant_id) VALUES (1001, '2021-05-01', 1001);
INSERT INTO votes (user_id, vote_date, restaurant_id) VALUES (1001, now(), 1000);
INSERT INTO votes (user_id, vote_date, restaurant_id) VALUES (1002, '2021-05-01', 1000);
INSERT INTO votes (user_id, vote_date, restaurant_id) VALUES (1001, '2021-05-02', 1000);
INSERT INTO votes (user_id, vote_date, restaurant_id) VALUES (1002, '2021-05-02', 1000);


INSERT INTO user_roles (user_id, role) VALUES (1000, 'USER');
INSERT INTO user_roles (user_id, role) VALUES (1000, 'ADMIN');
INSERT INTO user_roles (user_id, role) VALUES (1001, 'USER');
INSERT INTO user_roles (user_id, role) VALUES (1002, 'USER');
INSERT INTO user_roles (user_id, role) VALUES (1003, 'USER');
