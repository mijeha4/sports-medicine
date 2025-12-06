# Описание классов проекта Sports Medicine

## Модели (Entity)

### Athlete
Класс представляет спортсмена. Аннотирован как JPA Entity, таблица "athletes".
- **Поля:**
  - `id` (Long): Уникальный идентификатор.
  - `first_name` (String): Имя.
  - `last_name` (String): Фамилия.
  - `date_of_birth` (LocalDate): Дата рождения.
  - `sport_type` (String): Вид спорта.
  - `phone` (String): Телефон.
  - `registration_date` (LocalDate): Дата регистрации.
- **Методы:** Геттеры и сеттеры для всех полей.
- **Отношения:** Связан с MedicalExamination (один ко многим).

### Doctor
Класс представляет врача. Аннотирован как JPA Entity, таблица "doctors".
- **Поля:**
  - `doctorId` (Long): Уникальный идентификатор врача.
  - `firstName` (String): Имя.
  - `lastName` (String): Фамилия.
  - `specialization` (String): Специализация.
  - `licenseNumber` (String): Номер лицензии.
- **Методы:** Геттеры и сеттеры для всех полей.
- **Отношения:** Связан с MedicalExamination (один ко многим).

### ExaminationType
Класс представляет тип медицинского осмотра. Аннотирован как JPA Entity, таблица "examinationtypes".
- **Поля:**
  - `typeId` (Long): Уникальный идентификатор типа.
  - `typeName` (String): Название типа.
  - `description` (String): Описание.
- **Методы:** Геттеры и сеттеры для всех полей.
- **Отношения:** Связан с MedicalExamination (один ко многим).

### MedicalExamination
Класс представляет медицинский осмотр. Аннотирован как JPA Entity, таблица "medicalexaminations".
- **Поля:**
  - `id` (Long): Уникальный идентификатор осмотра.
  - `athlete` (Athlete): Спортсмен.
  - `doctor` (Doctor): Врач.
  - `examinationType` (ExaminationType): Тип осмотра.
  - `date` (LocalDate): Дата осмотра.
  - `next_date` (LocalDate): Дата следующего осмотра.
  - `conclusion` (String): Заключение.
- **Методы:** Геттеры и сеттеры для всех полей.
- **Отношения:**
  - Many-to-One с Athlete.
  - Many-to-One с Doctor.
  - Many-to-One с ExaminationType.
  - One-to-Many с PhysioIndicator.
  - One-to-Many с Recommendation.

### PhysioIndicator
Класс представляет физиологический показатель. Аннотирован как JPA Entity, таблица "physioindicators".
- **Поля:**
  - `indicatorId` (Long): Уникальный идентификатор показателя.
  - `examination` (MedicalExamination): Связанный осмотр.
  - `indicatorName` (String): Название показателя.
  - `measuredValue` (Double): Измеренное значение.
  - `unit` (String): Единица измерения.
  - `normalMin` (Double): Минимальная норма.
  - `normalMax` (Double): Максимальная норма.
- **Методы:** Геттеры и сеттеры для всех полей.
- **Отношения:** Many-to-One с MedicalExamination.

### Recommendation
Класс представляет рекомендацию. Аннотирован как JPA Entity, таблица "recommendations".
- **Поля:**
  - `recommendationId` (Long): Уникальный идентификатор рекомендации.
  - `examination` (MedicalExamination): Связанный осмотр.
  - `recommendationText` (String): Текст рекомендации.
  - `priority` (String): Приоритет.
  - `status` (String): Статус.
- **Методы:** Геттеры и сеттеры для всех полей.
- **Отношения:** Many-to-One с MedicalExamination.

## Репозитории (Repository)

### AthleteRepository
Интерфейс для работы с Athlete. Расширяет JpaRepository<Athlete, Long>.
- **Методы:**
  - `findAll()`: Получить всех спортсменов.
  - `search(String filter)`: Поиск по имени, фамилии или виду спорта.
  - `save(Athlete)`: Сохранить спортсмена.
  - `deleteById(Long)`: Удалить по ID.

### DoctorRepository
Интерфейс для работы с Doctor. Расширяет JpaRepository<Doctor, Long>.
- **Методы:**
  - `findAll()`: Получить всех врачей.
  - `save(Doctor)`: Сохранить врача.
  - `deleteById(Long)`: Удалить по ID.

### ExaminationTypeRepository
Интерфейс для работы с ExaminationType. Расширяет JpaRepository<ExaminationType, Long>.
- **Методы:**
  - `findAll()`: Получить все типы осмотров.
  - `save(ExaminationType)`: Сохранить тип.
  - `deleteById(Long)`: Удалить по ID.

### MedicalExaminationRepository
Интерфейс для работы с MedicalExamination. Расширяет JpaRepository<MedicalExamination, Long>.
- **Методы:**
  - `findAll()`: Получить все осмотры.
  - `countByAthleteId(Long)`: Подсчитать осмотры по ID спортсмена.
  - `save(MedicalExamination)`: Сохранить осмотр.
  - `deleteById(Long)`: Удалить по ID.

### PhysioIndicatorRepository
Интерфейс для работы с PhysioIndicator. Расширяет JpaRepository<PhysioIndicator, Long>.
- **Методы:**
  - `findAll()`: Получить все показатели.
  - `countByExaminationId(Long)`: Подсчитать показатели по ID осмотра.
  - `deleteByExaminationId(Long)`: Удалить по ID осмотра.
  - `save(PhysioIndicator)`: Сохранить показатель.
  - `deleteById(Long)`: Удалить по ID.

### RecommendationRepository
Интерфейс для работы с Recommendation. Расширяет JpaRepository<Recommendation, Long>.
- **Методы:**
  - `findAll()`: Получить все рекомендации.
  - `countByExaminationId(Long)`: Подсчитать рекомендации по ID осмотра.
  - `deleteByExaminationId(Long)`: Удалить по ID осмотра.
  - `save(Recommendation)`: Сохранить рекомендацию.
  - `deleteById(Long)`: Удалить по ID.

## Сервисы (Service)

### AthleteService
Сервис для управления спортсменами. Использует AthleteRepository, MedicalExaminationRepository, PhysioIndicatorRepository, RecommendationRepository.
- **Методы:**
  - `findAll()`: Получить всех спортсменов.
  - `searchAthletes(String)`: Поиск спортсменов.
  - `saveAthlete(Athlete)`: Сохранить спортсмена.
  - `deleteAthlete(Long)`: Удалить спортсмена.
  - `getAthleteDependencies(Long)`: Получить зависимости (осмотры, показатели, рекомендации).
  - `cascadeDeleteAthlete(Long)`: Каскадное удаление спортсмена и связанных данных.

### DoctorService
Сервис для управления врачами. Использует DoctorRepository.
- **Методы:**
  - `findAll()`: Получить всех врачей.
  - `saveDoctor(Doctor)`: Сохранить врача.
  - `deleteDoctor(Long)`: Удалить врача.

### ExaminationTypeService
Сервис для управления типами осмотров. Использует ExaminationTypeRepository.
- **Методы:**
  - `findAll()`: Получить все типы.
  - `saveExaminationType(ExaminationType)`: Сохранить тип.
  - `deleteExaminationType(Long)`: Удалить тип.

### MedicalExaminationService
Сервис для управления осмотрами. Использует MedicalExaminationRepository, AthleteRepository, DoctorRepository, ExaminationTypeRepository.
- **Методы:**
  - `findAll()`: Получить все осмотры.
  - `saveMedicalExamination(MedicalExamination)`: Сохранить осмотр.
  - `deleteMedicalExamination(Long)`: Удалить осмотр.

### PhysioIndicatorService
Сервис для управления показателями. Использует PhysioIndicatorRepository, MedicalExaminationRepository.
- **Методы:**
  - `findAll()`: Получить все показатели.
  - `savePhysioIndicator(PhysioIndicator)`: Сохранить показатель.
  - `deletePhysioIndicator(Long)`: Удалить показатель.

### RecommendationService
Сервис для управления рекомендациями. Использует RecommendationRepository, MedicalExaminationRepository.
- **Методы:**
  - `findAll()`: Получить все рекомендации.
  - `saveRecommendation(Recommendation)`: Сохранить рекомендацию.
  - `deleteRecommendation(Long)`: Удалить рекомендацию.

## UI (Vaadin)

### MainView
Основной вид приложения. Использует MainLayout.

### MainLayout
Макет для навигации.

### DashboardView
Вид дашборда. Использует AthleteService.

### AthletesView
Вид управления спортсменами. Использует AthleteService.

### DoctorsView
Вид управления врачами. Использует DoctorService.

### MedicalExaminationsView
Вид управления осмотрами. Использует MedicalExaminationService.

### ExaminationsView
Вид управления типами осмотров. Использует ExaminationTypeService.

### PhysioIndicatorsView
Вид управления показателями. Использует PhysioIndicatorService.

### RecommendationsView
Вид управления рекомендациями. Использует RecommendationService.

### AddAthleteDialog
Диалог добавления спортсмена. Использует AthleteService.

### AddDoctorDialog
Диалог добавления врача. Использует DoctorService.

### AddMedicalExaminationDialog
Диалог добавления осмотра. Использует MedicalExaminationService.

### AddExaminationDialog
Диалог добавления типа осмотра. Использует ExaminationTypeService.

### AddPhysioIndicatorDialog
Диалог добавления показателя. Использует PhysioIndicatorService.

### AddRecommendationDialog
Диалог добавления рекомендации. Использует RecommendationService.

### CascadeDeleteDialog
Диалог каскадного удаления. Использует AthleteService.

### ConfirmDialog
Диалог подтверждения.

## Главный класс

### SportsMedicineAppApplication
Точка входа приложения. Содержит метод main для запуска Spring Boot приложения. Запускает MainView.
