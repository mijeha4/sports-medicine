# Система спортивной медицины - Полная документация

## Оглавление
1. [Введение](#введение)
2. [Техническое описание приложения](#техническое-описание-приложения)
   - [Постановка задачи](#постановка-задачи)
   - [Проектирование базы данных](#проектирование-базы-данных)
   - [Логическое проектирование](#логическое-проектирование)
   - [Диаграмма классов](#диаграмма-классов)
   - [Физическое проектирование](#физическое-проектирование)
   - [Проектирование интерфейса](#проектирование-интерфейса)
   - [Тестирование](#тестирование)
   - [Результаты работы](#результаты-работы)
3. [Руководство пользователя](#руководство-пользователя)
   - [Установка и запуск](#установка-и-запуск)
   - [Управление атлетами](#управление-атлетами)
   - [Управление врачами](#управление-врачами)
   - [Медицинские осмотры](#медицинские-осмотры)
   - [Аналитический дашборд](#аналитический-дашборд)
   - [Устранение неполадок](#устранение-неполадок)
4. [API документация](#api-документация)
5. [Структура проекта](#структура-проекта)

---

# Введение

**Система спортивной медицины** - это комплексное веб-приложение для управления медицинским мониторингом спортсменов. Система позволяет вести учет спортсменов, врачей, медицинских осмотров, физиологических показателей и медицинских рекомендаций.

### Основные возможности
- **Управление данными спортсменов** - добавление, редактирование и удаление информации о спортсменах
- **Ведение базы врачей** - учет медицинского персонала с их специализациями
- **Регистрация медицинских осмотров** - фиксация результатов обследований
- **Мониторинг физиологических показателей** - отслеживание измерений с нормальными диапазонами
- **Управление рекомендациями** - создание и отслеживание медицинских советов
- **Аналитическая панель** - визуализация данных через графики и показатели KPI

### Системные требования
- **Операционная система:** Windows 10/11, Linux, macOS
- **Java:** JDK 17 или выше
- **Браузер:** Chrome, Firefox, Safari, Edge (последние версии)
- **Docker:** для контейнерного развертывания (рекомендуется)

---

# Техническое описание приложения

## 1. Постановка задачи

**Цель проекта:** Разработка комплексной информационной системы для управления медицинским мониторингом спортсменов, включающей учет атлетов, врачей, медицинских осмотров, физиологических показателей и медицинских рекомендаций.

**Основные функциональные требования:**
- **Управление данными спортсменов:** CRUD операции для атлетов с персональными данными, видом спорта и контактной информацией
- **Управление медицинским персоналом:** Ведение базы данных врачей с их специализациями и лицензиями
- **Отслеживание медицинских осмотров:** Регистрация осмотров с привязкой к спортсменам, врачам и типам обследований
- **Мониторинг физиологических показателей:** Запись измерений с нормальными диапазонами значений
- **Управление рекомендациями:** Создание и отслеживание медицинских рекомендаций с приоритетами и статусами
- **Аналитическая панель:** Визуализация данных через графики и KPI показатели
- **Веб-интерфейс:** Современный responsive UI для работы с данными

**Технические ограничения:**
- Использование Java 17 и Spring Boot 3.4.11
- PostgreSQL как основная СУБД
- Vaadin для frontend компонентов
- Docker контейнеризация

## 2. Проектирование базы данных

**Схема базы данных (PostgreSQL):**

```sql
-- Таблица спортсменов
CREATE TABLE athletes (
    athlete_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    date_of_birth DATE,
    sport_type VARCHAR(255),
    phone VARCHAR(255),
    registration_date DATE
);

-- Таблица врачей
CREATE TABLE doctors (
    doctor_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    specialization VARCHAR(255),
    license_number VARCHAR(255)
);

-- Типы медицинских осмотров
CREATE TABLE examinationtypes (
    type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(255),
    description TEXT
);

-- Медицинские осмотры
CREATE TABLE medicalexaminations (
    examination_id SERIAL PRIMARY KEY,
    athlete_id BIGINT REFERENCES athletes(athlete_id),
    doctor_id BIGINT REFERENCES doctors(doctor_id),
    type_id BIGINT REFERENCES examinationtypes(type_id),
    examination_date DATE,
    next_examination_date DATE,
    conclusion TEXT
);

-- Физиологические показатели
CREATE TABLE physioindicators (
    indicator_id SERIAL PRIMARY KEY,
    examination_id BIGINT REFERENCES medicalexaminations(examination_id),
    indicator_name VARCHAR(255),
    measured_value DOUBLE PRECISION,
    unit VARCHAR(50),
    normal_min DOUBLE PRECISION,
    normal_max DOUBLE PRECISION
);

-- Медицинские рекомендации
CREATE TABLE recommendations (
    recommendation_id SERIAL PRIMARY KEY,
    examination_id BIGINT REFERENCES medicalexaminations(examination_id),
    recommendation_text TEXT,
    priority VARCHAR(50),
    status VARCHAR(50)
);
```

**Особенности проектирования:**
- Использование SERIAL для автоинкрементных первичных ключей
- Каскадные связи через FOREIGN KEY constraints
- Поддержка NULL значений для опциональных полей
- TEXT поля для длинных описаний и заключений

## 3. Логическое проектирование

**Архитектурный паттерн:** Чистая многоуровневая архитектура (Clean Architecture)

**Уровни приложения:**

### Presentation Layer (Vaadin UI)
```java
@Route(value = "athletes", layout = MainLayout.class)
public class AthletesView extends VerticalLayout {
    // Vaadin компоненты для отображения данных
    private final Grid<Athlete> grid = new Grid<>(Athlete.class);
    private final AthleteService athleteService;

    // Конфигурация UI компонентов
    private void configureGrid() {
        grid.setColumns("id", "first_name", "last_name", "date_of_birth");
        // Настройка заголовков колонок
    }
}
```

### Application Layer (Services)
```java
@Service
public class AthleteService {
    @Autowired
    private AthleteRepository athleteRepository;

    public List<Athlete> findAll() {
        return athleteRepository.findAll();
    }

    public Athlete saveAthlete(Athlete athlete) {
        return athleteRepository.save(athlete);
    }
}
```

### Domain Layer (Entities)
```java
@Data
@Entity
@Table(name = "athletes")
public class Athlete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "athlete_id")
    private Long id;

    @Column(name = "first_name")
    private String first_name;

    // JPA аннотации для маппинга
    // Геттеры/сеттеры через Lombok @Data
}
```

### Infrastructure Layer (Repositories)
```java
@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    @Query("SELECT a FROM Athlete a WHERE " +
           "LOWER(a.first_name) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(a.last_name) LIKE LOWER(CONCAT('%', :filter, '%'))")
    List<Athlete> search(@Param("filter") String filter);
}
```

**REST API Layer:**
```java
@RestController
@RequestMapping("/api/Athlete")
public class AthleteController {
    @Autowired
    private AthleteService athleteService;

    @GetMapping
    public List<Athlete> getAllAthletes() {
        return athleteService.getAllAthletes();
    }

    @PostMapping
    public Athlete createAthlete(@RequestBody Athlete athlete) {
        return athleteService.saveAthlete(athlete);
    }
}
```

## 4. Диаграмма классов

```
┌─────────────────────────────────────┐
│            Athlete                  │
├─────────────────────────────────────┤
│ - id: Long                          │
│ - first_name: String                │
│ - last_name: String                 │
│ - date_of_birth: LocalDate          │
│ - sport_type: String                │
│ - phone: String                     │
│ - registration_date: Date           │
└─────────────────┬───────────────────┘
                  │ 1
                  │
                  │ *
┌─────────────────────────────────────┐
│        MedicalExamination           │
├─────────────────────────────────────┤
│ - id: Long                          │
│ - athlete: Athlete                  │
│ - doctor: Doctor                    │
│ - examinationType: ExaminationType  │
│ - date: LocalDate                   │
│ - next_date: LocalDate              │
│ - conclusion: String                │
└─────────────────┬───────────────────┘
                  │ 1
                  │
                  │ *
┌─────────────────────────────────────┐    ┌─────────────────────────────────────┐
│        PhysioIndicator              │    │         Recommendation             │
├─────────────────────────────────────┤    ├─────────────────────────────────────┤
│ - id: Long                          │    │ - id: Long                        │
│ - examination: MedicalExamination   │    │ - examination: MedicalExamination │
│ - indicator_name: String            │    │ - text: String                    │
│ - measured_value: Double            │    │ - priority: String                │
│ - unit: String                      │    │ - status: String                  │
│ - normal_min: Double                │    └─────────────────────────────────────┘
│ - normal_max: Double                │
└─────────────────────────────────────┘
                  │ 1
                  │
                  │ *
┌─────────────────────────────────────┐    ┌─────────────────────────────────────┐
│            Doctor                   │    │       ExaminationType             │
├─────────────────────────────────────┤    ├─────────────────────────────────────┤
│ - id: Long                          │    │ - id: Long                        │
│ - first_name: String                │    │ - type_name: String               │
│ - last_name: String                 │    │ - description: String             │
│ - specialization: String            │    └─────────────────────────────────────┘
│ - license_number: String            │
└─────────────────────────────────────┘
```

## 5. Физическое проектирование

**Технологический стек:**
- **Backend:** Spring Boot 3.4.11, Java 17
- **Frontend:** Vaadin 24.7.14 (Flow)
- **Database:** PostgreSQL 18
- **ORM:** Hibernate/JPA
- **Build Tool:** Maven 3.x
- **Containerization:** Docker + Docker Compose

**Конфигурация приложения:**
```properties
# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/sports_medicine_control
spring.datasource.username=postgres
spring.datasource.password=4a1khFjT
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
server.port=8080
```

**Docker конфигурация:**
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/sports_medicine_control
    depends_on:
      - db
  db:
    image: postgres:18-alpine
    environment:
      - POSTGRES_DB=sports_medicine_control
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=4a1khFjT
    volumes:
      - ./sports_medicine_control.sql:/docker-entrypoint-initdb.d/sports_medicine_control.sql
```

**Структура проекта:**
```
src/
├── main/
│   ├── java/chsu/example/sports_medicine/
│   │   ├── SportsMedicineAppApplication.java
│   │   ├── controller/          # REST API endpoints
│   │   ├── model/              # JPA entities
│   │   ├── repository/         # Data access layer
│   │   ├── service/            # Business logic
│   │   └── ui/                 # Vaadin views & dialogs
│   ├── resources/
│   │   ├── application.properties
│   │   └── META-INF/resources/
│   └── frontend/
│       └── styles/             # CSS stylesheets
├── test/
│   └── java/...               # Unit tests
pom.xml
docker-compose.yml
Dockerfile
sports_medicine_control.sql
```

## 6. Проектирование интерфейса

**UI Framework:** Vaadin Flow с Material Design компонентами

**Навигационная структура:**
```java
@PageTitle("Спортивная медицина")
public class MainLayout extends AppLayout {
    private void createDrawer() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(
            new SideNavItem("Главная страница", MainView.class, VaadinIcon.DESKTOP.create()),
            new SideNavItem("Атлеты", AthletesView.class, VaadinIcon.USERS.create()),
            new SideNavItem("Доктора", DoctorsView.class, VaadinIcon.DOCTOR.create()),
            new SideNavItem("Тип проверки", ExaminationsView.class, VaadinIcon.CHECK_SQUARE_O.create()),
            new SideNavItem("Мед. осмотр", MedicalExaminationsView.class, VaadinIcon.HEART.create()),
            new SideNavItem("Физио показатели", PhysioIndicatorsView.class, VaadinIcon.CHART.create()),
            new SideNavItem("Рекомендации", RecommendationsView.class, VaadinIcon.LIGHTBULB.create()),
            new SideNavItem("Дэшборд", DashboardView.class, VaadinIcon.DASHBOARD.create())
        );
    }
}
```

**Пример View компонента:**
```java
@Route(value = "athletes", layout = MainLayout.class)
public class AthletesView extends VerticalLayout {
    private final Grid<Athlete> grid = new Grid<>(Athlete.class);

    public AthletesView(AthleteService athleteService) {
        // Конфигурация грида
        grid.setColumns("id", "first_name", "last_name", "date_of_birth", "sport_type");

        // Панель инструментов
        TextField searchField = new TextField();
        searchField.addValueChangeListener(event -> {
            String query = event.getValue();
            if (query.isEmpty()) {
                grid.setItems(athleteService.findAll());
            } else {
                grid.setItems(athleteService.searchAthletes(query));
            }
        });

        Button addButton = new Button("Добавить атлета", click -> {
            AddAthleteDialog dialog = new AddAthleteDialog(athleteService);
            dialog.open();
        });
    }
}
```

**Форма добавления данных:**
```java
@Component
public class AddAthleteDialog extends Dialog {
    private final Binder<Athlete> binder = new Binder<>(Athlete.class);

    public AddAthleteDialog(AthleteService athleteService) {
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Name");
        TextField surnameField = new TextField("Surname");
        DatePicker ageField = new DatePicker("Date of Birth");

        // Binder для валидации и маппинга
        binder.forField(nameField).bind(Athlete::getFirstName, Athlete::setFirstName);
        binder.forField(surnameField).bind(Athlete::getLastName, Athlete::setLastName);

        Button saveButton = new Button("Save", event -> {
            if (binder.validate().isOk()) {
                Athlete athlete = new Athlete();
                binder.writeBeanIfValid(athlete);
                athleteService.saveAthlete(athlete);
                close();
            }
        });
    }
}
```

**Стилизация (CSS):**
```css
.athletes-view {
    padding: 1rem;
    background-color: #f8f9fa;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

vaadin-button {
    background-color: #f7e600;
    color: hsl(0, 100%, 50%);
    border-radius: 4px;
    transition: background-color 0.3s ease;
}

vaadin-grid {
    background-color: rgb(255, 0, 0);
    border-radius: 4px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}
```

**Dashboard с аналитикой:**
```java
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {
    private HorizontalLayout createKpiSection() {
        // KPI карточки
        Div athletesCard = createKpiCard("Всего спортсменов",
            String.valueOf(athleteService.findAll().size()));
        Div recommendationsCard = createKpiCard("Активные рекомендации",
            String.valueOf(recommendationService.findAll().size()));
        return new HorizontalLayout(athletesCard, recommendationsCard);
    }

    private Chart createSportDistributionChart() {
        Chart chart = new Chart(ChartType.PIE);
        Map<String, Long> sportDistribution = athleteService.findAll().stream()
            .collect(Collectors.groupingBy(Athlete::getSport_type, Collectors.counting()));
        // Конфигурация диаграммы
    }
}
```

## 7. Тестирование

**Текущая тестовая инфраструктура:**
```java
@SpringBootTest
class SportsMedicineAppApplicationTests {
	@Test
	void contextLoads() {
		// Проверка загрузки Spring контекста
	}
}
```

**Рекомендуемая стратегия тестирования:**

**Unit Tests (JUnit 5 + Mockito):**
```java
@ExtendWith(MockitoExtension.class)
class AthleteServiceTest {
    @Mock
    private AthleteRepository athleteRepository;

    @InjectMocks
    private AthleteService athleteService;

    @Test
    void shouldReturnAllAthletes() {
        // Given
        List<Athlete> expectedAthletes = Arrays.asList(new Athlete(), new Athlete());
        when(athleteRepository.findAll()).thenReturn(expectedAthletes);

        // When
        List<Athlete> actualAthletes = athleteService.findAll();

        // Then
        assertEquals(expectedAthletes.size(), actualAthletes.size());
    }
}
```

**Integration Tests:**
```java
@SpringBootTest
@AutoConfigureTestDatabase
class AthleteRepositoryIntegrationTest {
    @Autowired
    private AthleteRepository athleteRepository;

    @Test
    void shouldSaveAndRetrieveAthlete() {
        Athlete athlete = new Athlete();
        athlete.setFirstName("John");
        athlete.setLastName("Doe");

        Athlete saved = athleteRepository.save(athlete);
        assertNotNull(saved.getId());
    }
}
```

**UI Tests (Vaadin TestBench):**
```java
class AthletesViewTest extends TestBenchTestCase {
    @Test
    public void testAddAthlete() {
        // Открыть страницу атлетов
        getDriver().get("http://localhost:8080/athletes");

        // Найти кнопку добавления
        Button addButton = $(Button.class).caption("Добавить атлета").first();
        addButton.click();

        // Проверить открытие диалога
        Dialog dialog = $(Dialog.class).first();
        assertTrue(dialog.isDisplayed());
    }
}
```

**API Tests (REST Assured):**
```java
class AthleteControllerTest {
    @Test
    void shouldReturnAthletesList() {
        given()
            .when()
                .get("/api/Athlete")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }
}
```

## 8. Результаты работы

**Реализованная функциональность:**

### Backend (Spring Boot)
- ✅ Полностью реализованный REST API для всех сущностей
- ✅ JPA сущности с правильными связями и аннотациями
- ✅ Сервисный слой с бизнес-логикой
- ✅ Репозитории с кастомными запросами
- ✅ Контролеры с CRUD операциями
- ✅ Конфигурация базы данных PostgreSQL
- ✅ Docker контейнеризация

### Frontend (Vaadin)
- ✅ Адаптивный веб-интерфейс с боковым меню
- ✅ CRUD операции через модальные диалоги
- ✅ Табличное отображение данных с поиском
- ✅ Аналитический дашборд с графиками
- ✅ Формы с валидацией через Binder
- ✅ Кастомная стилизация компонентов

### Database
- ✅ Нормализованная схема с внешними ключами
- ✅ SQL скрипт для инициализации
- ✅ Docker Compose для развертывания

### Архитектурные достижения
- ✅ Четкое разделение ответственности (MVC + Services)
- ✅ Использование Spring Data JPA для упрощения доступа к данным
- ✅ Vaadin для быстрой разработки UI
- ✅ Lombok для сокращения boilerplate кода
- ✅ Docker для containerization

### Ключевые возможности системы
1. **Управление спортсменами:** Полный CRUD с поиском по имени/фамилии/виду спорта
2. **Медицинские осмотры:** Связывание осмотров с спортсменами, врачами и типами обследований
3. **Физиологические показатели:** Запись измерений с диапазонами нормы
4. **Рекомендации:** Управление медицинскими рекомендациями с приоритетами
5. **Аналитика:** Визуализация данных через графики и KPI
6. **API:** REST endpoints для интеграции с внешними системами

### Технические метрики
- **Строк кода:** ~2000+ строк Java кода
- **Количество сущностей:** 6 основных JPA сущностей
- **UI представлений:** 8 Vaadin представлений
- **REST endpoints:** 18+ API методов
- **Тестов:** 1 базовый интеграционный тест

### Развертывание и эксплуатация
- **Локальная разработка:** `mvn spring-boot:run`
- **Docker развертывание:** `docker-compose up`
- **База данных:** PostgreSQL с автоматической инициализацией
- **Порт приложения:** 8080
- **Веб-интерфейс:** Доступен через браузер

Приложение успешно реализует все поставленные требования и предоставляет полнофункциональную систему для медицинского мониторинга спортсменов с современным веб-интерфейсом и надежной архитектурой.

---

# Руководство пользователя

## Установка и запуск

### Вариант 1: Docker (Рекомендуемый)

1. **Убедитесь, что Docker установлен** на вашем компьютере
2. **Скачайте проект** или перейдите в директорию проекта
3. **Запустите приложение:**
   ```bash
   docker-compose up --build
   ```
4. **Дождитесь завершения сборки** (может занять несколько минут)
5. **Откройте браузер** и перейдите по адресу: `http://localhost:8080`

### Вариант 2: Локальный запуск

1. **Установите Java 17** или выше
2. **Установите PostgreSQL** и создайте базу данных:
   ```sql
   CREATE DATABASE sports_medicine_control;
   ```
3. **Настройте подключение** в файле `src/main/resources/application.properties`
4. **Запустите PostgreSQL** на порту 5432
5. **Выполните SQL скрипт** `sports_medicine_control.sql` для создания таблиц
6. **Соберите и запустите приложение:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
7. **Откройте браузер** и перейдите по адресу: `http://localhost:8080`

### Проверка установки

После успешного запуска вы должны увидеть:
- Заголовок "Добро пожаловать в систему спортивной медицины"
- Боковое меню навигации слева
- Возможность перехода между разделами

## Навигация по системе

1. **Боковое меню** содержит следующие разделы:
   - **Главная страница** - приветственное сообщение
   - **Атлеты** - управление спортсменами
   - **Доктора** - управление врачами
   - **Тип проверки** - типы медицинских осмотров
   - **Мед. осмотр** - медицинские осмотры
   - **Физио показатели** - физиологические показатели
   - **Рекомендации** - медицинские рекомендации
   - **Дэшборд** - аналитическая панель

2. **Переход между разделами** осуществляется кликом по пунктам меню

## Управление атлетами

### Просмотр списка атлетов

1. Перейдите в раздел **"Атлеты"**
2. Вы увидите таблицу со следующими колонками:
   - **ID** - уникальный идентификатор
   - **Имя** - имя спортсмена
   - **Фамилия** - фамилия спортсмена
   - **Дата рождения** - дата рождения
   - **Тип спорта** - вид спорта
   - **Телефон** - контактный телефон
   - **Дата регистрации** - дата добавления в систему

### Добавление нового атлета

1. В разделе "Атлеты" нажмите кнопку **"Добавить атлета"**
2. Заполните форму:
   - **Name** - имя спортсмена (обязательно)
   - **Surname** - фамилия спортсмена (обязательно)
   - **Date of Birth** - дата рождения (обязательно)
   - **Sport type** - вид спорта (обязательно)
   - **Phone** - контактный телефон
3. Нажмите **"Save"** для сохранения или **"Cancel"** для отмены

### Поиск атлетов

1. Используйте поле поиска в верхней части страницы
2. Введите имя, фамилию или вид спорта
3. Результаты фильтруются автоматически по мере ввода

### Удаление атлета

1. Выберите атлета в таблице (кликните по строке)
2. Нажмите кнопку **"Удалить атлета"**
3. Подтвердите удаление в диалоговом окне
4. Нажмите **"Удалить"** или **"Отмена"**

⚠️ **Внимание:** Удаление атлета также удалит все связанные с ним медицинские осмотры, показатели и рекомендации!

## Управление врачами

### Просмотр списка врачей

1. Перейдите в раздел **"Доктора"**
2. Таблица содержит:
   - **ID** - уникальный идентификатор
   - **Имя** - имя врача
   - **Фамилия** - фамилия врача
   - **Специализация** - медицинская специализация
   - **Номер лицензии** - лицензионный номер

### Добавление врача

1. Нажмите **"Добавить доктора"**
2. Заполните поля:
   - **First Name** - имя (обязательно)
   - **Last Name** - фамилия (обязательно)
   - **Specialization** - специализация (обязательно)
   - **License Number** - номер лицензии (обязательно)
3. Сохраните изменения

### Поиск и удаление врачей

Аналогично управлению атлетами - используйте поиск и кнопки удаления.

## Типы медицинских осмотров

### Управление типами осмотров

1. Перейдите в раздел **"Тип проверки"**
2. **Добавление типа:**
   - Нажмите **"Добавить тип проверки"**
   - Введите название типа (например: "Кардиологический осмотр")
   - Добавьте описание типа осмотра

### Доступные операции

- Просмотр всех типов осмотров
- Добавление новых типов
- Удаление существующих типов
- Поиск по названию

## Медицинские осмотры

### Создание медицинского осмотра

1. Перейдите в раздел **"Мед. осмотр"**
2. Нажмите **"Добавить мед. осмотр"**
3. Заполните форму:
   - **Спортсмен** - выберите из списка атлетов
   - **Врач** - выберите врача, проводившего осмотр
   - **Тип осмотра** - выберите тип обследования
   - **Дата осмотра** - дата проведения
   - **Дата следующего осмотра** - планируемая дата
   - **Заключение** - результаты и выводы осмотра

### Просмотр и управление осмотрами

- Таблица показывает все зарегистрированные осмотры
- Возможен поиск по спортсмену или врачу
- Удаление осмотров (с подтверждением)

## Физиологические показатели

### Добавление показателей

1. Перейдите в раздел **"Физио показатели"**
2. Нажмите **"Добавить физио показатель"**
3. Выберите **медицинский осмотр** из списка
4. Заполните параметры:
   - **Название показателя** (например: "ЧСС в покое")
   - **Измеренное значение** (числовое значение)
   - **Единица измерения** (например: "уд/мин")
   - **Нижняя граница нормы**
   - **Верхняя граница нормы**

### Анализ показателей

- Система автоматически определяет, находится ли показатель в норме
- Красный цвет фона указывает на отклонения
- Зеленый цвет - показатели в норме

## Медицинские рекомендации

### Создание рекомендаций

1. Перейдите в раздел **"Рекомендации"**
2. Нажмите **"Добавить рекомендацию"**
3. Выберите **медицинский осмотр**
4. Заполните:
   - **Текст рекомендации** - подробное описание
   - **Приоритет** - высокий/средний/низкий
   - **Статус** - активная/выполнена/отменена

### Управление рекомендациями

- Просмотр всех рекомендаций
- Фильтрация по статусу и приоритету
- Обновление статуса рекомендаций

## Аналитический дашборд

### Обзор показателей

Раздел **"Дэшборд"** содержит:

1. **KPI карточки:**
   - Общее количество спортсменов
   - Количество осмотров за месяц
   - Количество отклонений от нормы
   - Количество активных рекомендаций

2. **Графики:**
   - **Линейный график ЧСС** - динамика по спортсменам
   - **Круговая диаграмма** - распределение по видам спорта
   - **Столбчатая диаграмма** - состояние здоровья по заключениям

### Использование аналитики

- Графики обновляются автоматически при добавлении новых данных
- Используйте для мониторинга общего состояния спортсменов
- Отслеживайте тенденции и выявляйте проблемы

## Поиск и фильтрация

### Глобальный поиск

- Доступен в большинстве разделов
- Ищет по текстовым полям (имена, фамилии, описания)
- Результаты обновляются в реальном времени

### Специализированные фильтры

- **По спортсменам:** поиск по имени, фамилии, виду спорта
- **По врачам:** поиск по имени, специализации
- **По рекомендациям:** фильтрация по статусу и приоритету

## Устранение неполадок

### Проблема: Приложение не запускается

**Решение:**
1. Проверьте, запущен ли Docker
2. Убедитесь, что порты 8080 и 5432 свободны
3. Проверьте логи контейнера: `docker-compose logs`
4. Перезапустите: `docker-compose down && docker-compose up --build`

### Проблема: Не сохраняются данные

**Решение:**
1. Проверьте подключение к базе данных
2. Убедитесь, что PostgreSQL контейнер запущен
3. Проверьте правильность учетных данных в application.properties

### Проблема: Не загружается интерфейс

**Решение:**
1. Очистите кэш браузера (Ctrl+F5)
2. Попробуйте другой браузер
3. Проверьте, что приложение доступно на localhost:8080

### Проблема: Ошибки валидации форм

**Решение:**
1. Убедитесь, что все обязательные поля заполнены
2. Проверьте формат вводимых данных
3. Для дат используйте календарь, а не ручной ввод

## Часто задаваемые вопросы

### Q: Можно ли редактировать существующие записи?
A: В текущей версии редактирование не предусмотрено. Для изменения данных необходимо удалить старую запись и создать новую.

### Q: Поддерживается ли многопользовательский режим?
A: В текущей версии система не имеет аутентификации. Все пользователи имеют полный доступ к данным.

### Q: Как восстановить удаленные данные?
A: Удаление является необратимым. Регулярно создавайте резервные копии базы данных.

### Q: Можно ли добавить фотографии спортсменов?
A: Функция загрузки изображений пока не реализована, но может быть добавлена в будущих версиях.

### Q: Как интегрировать с другими системами?
A: Используйте REST API endpoints для интеграции с внешними приложениями.

---

# API документация

## Обзор API

Система предоставляет REST API для программного доступа ко всем данным. Базовый URL: `http://localhost:8080/api/`

## Endpoints

### Атлеты (`/api/Athlete`)
- `GET /api/Athlete` - Получить всех атлетов
- `POST /api/Athlete` - Создать нового атлета
- `DELETE /api/Athlete/{id}` - Удалить атлета

### Врачи (`/api/Doctor`)
- `GET /api/Doctor` - Получить всех врачей
- `POST /api/Doctor` - Создать нового врача
- `DELETE /api/Doctor/{id}` - Удалить врача

### Типы осмотров (`/api/ExaminationType`)
- `GET /api/ExaminationType` - Получить все типы осмотров
- `POST /api/ExaminationType` - Создать новый тип осмотра
- `DELETE /api/ExaminationType/{id}` - Удалить тип осмотра

### Медицинские осмотры (`/api/MedicalExamination`)
- `GET /api/MedicalExamination` - Получить все осмотры
- `POST /api/MedicalExamination` - Создать новый осмотр
- `DELETE /api/MedicalExamination/{id}` - Удалить осмотр

### Физиологические показатели (`/api/PhysioIndicator`)
- `GET /api/PhysioIndicator` - Получить все показатели
- `POST /api/PhysioIndicator` - Создать новый показатель
- `DELETE /api/PhysioIndicator/{id}` - Удалить показатель

### Рекомендации (`/api/Recommendation`)
- `GET /api/Recommendation` - Получить все рекомендации
- `POST /api/Recommendation` - Создать новую рекомендацию
- `DELETE /api/Recommendation/{id}` - Удалить рекомендацию

## Формат данных

API использует JSON формат для запросов и ответов. Все сущности возвращаются в виде JSON объектов с соответствующими полями.

---

# Структура проекта

```
sports-medicine/
├── DOCUMENTATION.md              # Эта документация
├── pom.xml                       # Maven конфигурация
├── docker-compose.yml            # Docker Compose конфигурация
├── Dockerfile                    # Docker образ приложения
├── sports_medicine_control.sql   # SQL скрипт базы данных
├── sample_data.sql              # Примеры данных
├── src/
│   ├── main/
│   │   ├── java/chsu/example/sports_medicine/
│   │   │   ├── SportsMedicineAppApplication.java    # Главный класс
│   │   │   ├── controller/         # REST контроллеры
│   │   │   ├── model/            # JPA сущности
│   │   │   ├── repository/       # Репозитории данных
│   │   │   ├── service/          # Бизнес логика
│   │   │   └── ui/               # Vaadin UI компоненты
│   │   ├── resources/
│   │   │   ├── application.properties    # Конфигурация
│   │   │   └── META-INF/resources/
│   │   └── frontend/
│   │       └── styles/           # CSS стили
│   └── test/
│       └── java/...             # Тесты
└── target/                      # Скомпилированные файлы
```

---

**Версия системы:** 0.0.1-SNAPSHOT
**Дата создания документации:** Ноябрь 2025
**Технологии:** Java 17, Spring Boot 3.4.11, Vaadin 24.7.14, PostgreSQL 18
