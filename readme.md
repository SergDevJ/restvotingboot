### Voting system for deciding where to have lunch.
* 2 types of users: administrator and regular users
* Users can register, vote for the restaurant, manage their profile and view their voting history
* Admin can create / edit / delete users.
* Admin maintains a list of dishes for the menu
* Admin can input a restaurant and it's lunch menu of the day
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
  - If it is before 11:00 we assume that he changed his mind.
  - If it is after 11:00 then it is too late, vote can't be changed
* For each restaurant in the daily menu, the dish can be specified only once. The dish cannot be deleted if used in the menu.
* Voting history and menus are stored, filtering by date is possible.

_Work with the system is implemented via REST interface with basic authorization.
The time until which voting is possible is set in the "app-settings.properties" settings file. The voting date and time is defined as the current date and time on the server.
Spring caching is used - caching a request for a list of restaurants, menu of the current day when voting.
The "Admin", "User1" and "User2" users cannot be deleted and modified. When registering / editing a user, the uniqueness of the name (case-insensitive) and email is checked.
The portion of the service layer and of the REST interface is covered with JUnit tests using Spring MVC Test and Spring Security Test._

### To run the application
To run the application (and package the source code if necessary) you need to execute: _mvn spring-boot:run_

And then hit this url from a web browser (application is deployed in context 'restvoting'):
_http://localhost:8080/restvoting_

### Restaurant voting system API documentation is available via Swagger:
- To launch Swagger UI  hit url _http://localhost:8080/restvoting/swagger-ui/index.html_ from a web browser
- To access 'swagger.json' hit _http://localhost:8080/restvoting/v2/api-docs_  

<br><br>
### __curl samples__ (application is deployed in context 'restvoting'):
_Built-in users accounts: Admin (1111), User1 (2222), User2 (3333), User3 (abcd)_

### _Users_:

#### get All Users
`curl -s http://localhost:8080/restvoting/rest/admin/users -u Admin:1111`

#### get User 1001
`curl -s http://localhost:8080/restvoting/rest/admin/users/1001 -u Admin:1111`

#### get User not found
`curl -s -v http://localhost:8080/restvoting/rest/admin/users/2000 -u Admin:1111`

#### get Voting history
`curl -s "http://localhost:8080/restvoting/rest/profile/voting-history?start_date=2021-05-01&end_date=2021-05-02" -u User1:2222`

#### register User
`curl -s -i -X POST -d '{"name":"NewUser","email":"new_user@mail.ru","password":"abcd"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/profile/register`

#### create User
`curl -s -i -X POST -d '{"name":"NewUser2","email":"new_user2@mail.ru","password":"abcd"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/admin/users -u Admin:1111`

#### update User profile -  with user edit restrictions error
`curl -s -i -X PUT -d '{"name":"UpdatedUser","email":"new_mail@mail.ru","password":"psw1"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/profile -u User1:2222`

#### update User 1003
`curl -s -i -X PUT -d '{"name":"UpdatedUser3","email":"new_mail2@mail.ru","password":"psw2"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/admin/users/1003 -u Admin:1111`

#### update User profile
`curl -s -i -X PUT -d '{"name":"User3","email":"mail3@mail.ru","password":"abcd"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/profile -u UpdatedUser3:psw2`

#### update User - with validate error
`curl -s -i -X PUT -d '{"name":"User3","email":"mail3@mail.ru","password":"123"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/admin/users/1002 -u Admin:1111`

#### delete User - with error 403 Forbidden
`curl -s -X DELETE http://localhost:8080/restvoting/rest/admin/users/1002 -u User3:psw3`

#### delete User (profile)
`curl -s -X DELETE http://localhost:8080/restvoting/rest/profile -u NewUser:abcd`

#### delete User 1005
`curl -s -X DELETE http://localhost:8080/restvoting/rest/admin/users/1005 -u Admin:1111`



### _Restaurants_:

#### create Restaurant
`curl -s -i -X POST -d '{"name":"New restaurant","email":"new_restaurant@mail.ru","address":"New York"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/admin/restaurants -u Admin:1111`

#### update Restaurant
`curl -s -i -X PUT -d '{"id":"1002","name":"Updated restaurant","email":"new_restaurant@mail.ru","address":"New York"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/admin/restaurants/1002 -u Admin:1111`

#### delete Restaurant
`curl -s -X DELETE http://localhost:8080/restvoting/rest/admin/restaurants/1002 -u Admin:1111`

#### get All Restaurants
`curl -s http://localhost:8080/restvoting/rest/restaurants -u Admin:1111`

#### get All Restaurants with user vote
`curl -s http://localhost:8080/restvoting/rest/restaurants/with-vote -u User2:3333`

#### get Restaurant 1001
`curl -s http://localhost:8080/restvoting/rest/restaurants/1001 -u Admin:1111`



### _Voting_:

#### Vote (first time)
`curl -s -X POST http://localhost:8080/restvoting/rest/votes?restaurantId=1000 -u User3:abcd`

#### Vote again on the same day (after 11:00 - with validation error)
`curl -s -X PUT http://localhost:8080/restvoting/rest/votes/10005?restaurantId=1001 -u User3:abcd`

#### Get vote for today
`curl -s http://localhost:8080/restvoting/rest/votes/today -u User3:abcd`



### _Menu_:

#### Get menu for voting (cacheable)
`curl -s "http://localhost:8080/restvoting/rest/restaurant-menu/1000/voting" -u User3:abcd`

#### Get today menu
`curl -s "http://localhost:8080/restvoting/rest/restaurant-menu/1000" -u User3:abcd`

#### Get menu history
`curl -s "http://localhost:8080/restvoting/rest/restaurant-menu/1000/history?date=2021-05-01" -u User1:2222`


### _Dishes_:

#### get All Dishes
`curl -s http://localhost:8080/restvoting/rest/admin/dishes -u Admin:1111`

#### get Dish 1000
`curl -s http://localhost:8080/restvoting/rest/admin/dishes/1000 -u Admin:1111`

#### get Dish not found
`curl -s http://localhost:8080/restvoting/rest/admin/dishes/10000 -u Admin:1111`

#### update Dish 1000
`curl -s -i -X PUT -d '{"name":"Updated dish","weight":1234}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/admin/dishes/1000 -u Admin:1111`

#### update Dish - with validate error
`curl -s -i -X PUT -d '{"name":"U","weight":12345}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/admin/dishes/1000 -u Admin:1111`

#### create Dish
`curl -s -i -X POST -d '{"name":"Created dish","weight":999}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/admin/dishes -u Admin:1111`

#### create Dish - with validate error
`curl -s -i -X POST -d '{"name":"Created dish","weight":1}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoting/rest/admin/dishes -u Admin:1111`

#### delete Dish 1006
`curl -s -X DELETE http://localhost:8080/restvoting/rest/admin/dishes/1006 -u Admin:1111`

#### delete Dish - with data integrity error
`curl -s -X DELETE http://localhost:8080/restvoting/rest/admin/dishes/1002 -u Admin:1111`