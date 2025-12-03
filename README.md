# Sports Medicine Management System

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.11-green)

Веб-приложение для управления данными спортивной медицины. Предоставляет интерфейс для работы с атлетами, врачами, медицинскими осмотрами и статистикой. Использует современные технологии для обеспечения надежности и удобства использования.

## Стек технологий

- **Backend**: Spring Boot 3.4.11, Spring Data JPA
- **Frontend**: Vaadin 24.7.14
- **Database**: PostgreSQL
- **Build Tool**: Maven

## Ключевой функционал

- Управление атлетами (CRUD операции, поиск и фильтрация)
- Управление врачами
- Медицинские осмотры и типы осмотров
- Физиологические показатели
- Рекомендации
- Статистика и графики (Vaadin Charts)
- Каскадное удаление данных
- Валидация форм

## Предварительные требования

- JDK 21
- Maven 3.6+
- PostgreSQL 12+

## Настройка базы данных

1. Установите и запустите PostgreSQL.
2. Создайте базу данных `sports_medicine_control`.
3. Выполните скрипт `sports_medicine_control.sql` для создания схемы.
4. (Опционально) Выполните `sample_data.sql` для загрузки тестовых данных.

Настройте `src/main/resources/application.properties`:

```properties
# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/sports_medicine_control
spring.datasource.username=<your_username>
spring.datasource.password=<your_password>
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA configuration
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server configuration
server.port=8080
```

## Инструкция по запуску

1. Клонируйте репозиторий.
2. Настройте базу данных как описано выше.
3. Запустите приложение:

```bash
mvn spring-boot:run
```

Приложение будет доступно по адресу `http://localhost:8080`.
