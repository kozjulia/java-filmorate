## Это репозиторий проекта "Социальная сеть для оценки фильмов"
#### Бэкенд filmorate

Это сервис, который будет работать с фильмами и оценками пользователей, а также возвращать __топ__ фильмов, рекомендованных к просмотру
-------

Приложение **умеет** делать следующее:
1. Хранит фильмы
2. Хранит пользователей
3. Создаёт и обновляет фильмы
4. Создаёт и обновляет пользователей
5. Выводит список всех фильмов
6. Выводит список всех пользователей
7. Получает каждый фильм и данные о пользователях по их уникальному идентификатору
8. Выводит список всех жанров и рейтингов МПА фильмов
9. Получает каждый жанр и рейтинг МПА фильмов по их уникальному идентификатору
8. Добавляет в друзья другого пользователя
9.  Удаляет из друзей другого пользователя
10. Выводит список пользователей, являющихся друзьями пользователя
11. Выводит список друзей, общих с другим пользователем
12. Пользователь ставит лайк фильму
13. Пользователь удаляет лайк с фильма
14. Возвращает список из первых фильмов по количеству лайков
15. Выводит топ-N фильмов по количеству лайков
16. Реализована функциональность «Фильмы по режиссёрам», которая предполагает добавление к фильму информации о 
его режиссёре
17. Реализована функциональность «Отзывы» на фильмы
18. Реализована функциональность «Поиск» по названию фильмов и по режиссёру
19. Реализована функциональность «Общие фильмы» - вывод общих с другом фильмов с сортировкой по их популярности
20. Реализована функциональность «Рекомендации» - простая рекомендательная система для фильмов
21. Реализована функциональность «Лента событий» - возможность просмотра последних событий на платформе


Приложение написано на **Java**, использует **Spring Boot** и **Maven**, 
API соответствует **REST**, данные хранятся в БД **H2**. 
Тестовое покрытие кода 37%. Пример кода:
```java
public class Main {
    public static void main(String[] args) {
    }
}
```
------

## Описание модели данных
_____

![Диаграмма баз данных проекта](/filmorate_dbs.png)

`users`

Содержит данные о пользователях
Таблица включает такие поля:
1. первичный ключ `user_id` - идентификатор пользователя
2. `user_name` - имя для отображения пользователя
3. `email` - электронная почта пользователя
4. `login` - логин пользователя
5. `birthday` - дата рождения пользователя

`films`

Содержит данные о фильмах
Таблица включает такие поля:
1. первичный ключ `film_id` - идентификатор фильма
2. `film_name` - название фильма
3. `description` - описание фильма
4. `release_date` - дата релиза фильма
5. `duration` - продолжительность фильма
6. `rate` - рейтинг фильма
7. внешний ключ `rating_mpa_id` (отсылает к таблице `rating_mpa`) - 
   идентификатор рейтинга Ассоциации кинокомпаний (МРА)

`friends`

Содержит данные о друзьях
Таблица включает такие поля:
1. составной первичный ключ `request_friend_id` (отсылает к таблице `users`) - 
   идентификатор пользователя, который отправил запрос на добавление другого пользователя в друзья
2. составной первичный ключ `response_friend_id` (отсылает к таблице `users`) - 
   идентификатор пользователя, которому отправили запрос на добавление в друзья

`likes`

Содержит данные о пользователях и фильмах, которые они лайкнули
Таблица включает такие поля:
1. составной первичный ключ `user_id` (отсылает к таблице `users`) - идентификатор пользователя, который лайнул фильм
2. составной первичный ключ `film_id` (отсылает к таблице `films`) - идентификатор фильма, который лайкнул пользователь

`genres`

Содержит данные о жанрах фильмов
Таблица включает такие поля:
1. первичный ключ `genre_id` - идентификатор жанра
2. `genre_name` - название жанра
   
   Например, такие:
   
   Комедия,
   
   Драма,
   
   Мультфильм,
   
   Триллер,
   
   Документальный,
   
   Боевик.

`film_genre`

Содержит данные о фильмах и жанрах, которые могут быть у них
Таблица включает такие поля:
1. составной первичный ключ `film_id` (отсылает к таблице `films`) - идентификатор фильма
2. составной первичный ключ `genre_id` (отсылает к таблице `genres`) - идентификатор жанра

`rating_mpa`

Содержит данные о рейтинге Ассоциации кинокомпаний (МРА)
Таблица включает такие поля:
1. первичный ключ `rating_mpa_id` - идентификатор рейтинга
2. `rating_mpa_name` - название рейтинга
   
   Значения могут быть следующими:

   `G` — у фильма нет возрастных ограничений,

   `PG` — детям рекомендуется смотреть фильм с родителями,

   `PG-13` — детям до 13 лет просмотр не желателен,

   `R` — лицам до 17 лет просматривать фильм можно только в присутствии взрослого,

   `NC-17` — лицам до 18 лет просмотр запрещён.

`directors`

Содержит данные о режиссёрах фильмов
Таблица включает такие поля:
1. первичный ключ `director_id` - идентификатор режиссёра
2. `director_name` - название режиссёра

`film_director`

Содержит данные о фильмах и режиссёрах, которые могут быть у них
Таблица включает такие поля:
1. составной первичный ключ `film_id` (отсылает к таблице `films`) - идентификатор фильма
2. составной первичный ключ `director_id` (отсылает к таблице `directors`) - идентификатор режиссёра

`reviews`

Содержит данные об отзывах на фильмы
Таблица включает такие поля:
1. первичный ключ `review_id` - идентификатор отзыва
2. `review_content` - описание отзыва
3. `is_positive` - является ли отзыв позитивным
4. внешний ключ `user_id` (отсылает к таблице `users`) - идентификатор пользователя, который оставил отзыв о фильме
5. внешний ключ `film_id` (отсылает к таблице `films`) - идентификатор фильма
6. `useful` - рейтинг полезности 

`feeds`

Содержит данные о событиях
Таблица включает такие поля:
1. первичный ключ `event_id` - идентификатор события
2. внешний ключ `user_id` (отсылает к таблице `users`) - идентификатор пользователя, который совершил событие
3. `timestamp` - отметка времени, когда совершено событие
4. `event_type` - одно из значений LIKE, REVIEW или FRIEND
5. `operation` - одно из значениий REMOVE, ADD, UPDATE
6. `entity_id` - идентификатор сущности, с которой произошло событие



##### Примеры запросов для основных операций моего приложения:
```sql
-- получение всех фильмов
SELECT *
FROM films;

-- получение всех пользователей
SELECT *
FROM users;

--  топ 10 наиболее популярных фильмов
SELECT f.film_id,
       f.film_name,
       COUNT(l.like_id) AS count_likes
FROM films AS f
LEFT OUTER JOIN likes AS l ON f.film_id = l.film.id
GROUP BY f.film_id
ORDER BY count_likes DESC
LIMIT 10;

-- список общих друзей с другим пользователем
SELECT f1.response_friend_id
FROM friends AS f1
WHERE f1.request_friend_id = 1
  AND f1.response_friend_id IN
    (SELECT response_friend_id
     FROM friends
     WHERE request_friend_id = 3);
 
```
------
