## ДЗ №4 Т1 Холдинг

### Функциональность

Приложение c реализованной аутентификацией и авторизацией пользователей с использованием Spring Security и JWT.


### Эндпоинты

Примеры запросов ко всем эндпоинтам и проверка крайних случаев есть в postman-коллекций. Также просмотр документации
API доступен через Swagger UI по пути `/swagger-ui/index.html`.

> В postman-коллекции также написаны post-request скрипты, которые подставляют полученные в ходе авторизации jwt-токены
> в заголовки последующих запросов. Это упрощает тестирование и избавляет пользователя от необходимости вручную 
> копировать токен для каждого http-запроса, где он необходим.

Эндпоинты, доступные всем:

- POST `/api/auth/login` - авторизация для уже существующих пользователей
- POST `/api/auth/register` - регистрация нового пользователя

Эндпоинты, требующие в заголовок с JWT-токеном:

- GET `/api/users/{userId}` - получение пользователя по идентификатору (пользователь может получить информацию только о самом себе,
для получения информации о других пользователях нужна роль `ROLE_ADMIN`)
- GET `/api/users` - получение информации о всех пользователях (доступно только с ролью `ROLE_ADMIN`)
- PATCH `/api/users/{userId}` - обновление информации о пользователе (пользователь может обновить информацию только о самом себе,
для обновления информации о других пользователях нужна роль `ROLE_ADMIN`)

### Тестирование

Классы, отвечающие за безопасность (авторизация, аутентификация, генерация JWT-токенов) были покрыты unit-тестами
с использованием **JUnit 5** и **Mockito**.

### Инструкция по запуску

1. Склонируйте репозиторий `git clone https://github.com/isthatkirill/t1-hw4-security.git`

2. Перейдите в директорию с проектом `cd t1-hw4-security`

3. Запустите PostgreSQL локально на компьютере, предварительно установив нужные параметры в `.env` файле или воспользуйтесь заранее подготовленным в `docker-compose.yaml` контейнером, запустив его с помощью команды `docker compose up`.

4. Запустите приложение посредством среды разработки или с помощью команд: `mvn clean package`, `mvn spring-boot:run`.

### Используемые технологии и библиотеки

- Spring Boot
- Spring Data JPA
- Spring Security
- Lombok
- Mapstruct
- PostgreSQL
- Liquibase
- Docker
- JJWT
- Swagger UI
- Mockito
- JUnit 5