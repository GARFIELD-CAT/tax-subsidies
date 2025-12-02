# Tax Subsidies API

Этот проект предоставляет API для работы с налоговыми субсидиями. Он позволяет получать данные о субсидиях с пагинацией
и сортировкой, фильтровать записи, добавлять/обновлять данные, а также получать некоторые статистические показатели.

## Запуск проекта

1. **Клонируйте репозиторий**:
   ```bash
   git clone https://github.com/GARFIELD-CAT/tax-subsidies.git
   ```

2. **Перейдите в каталог проекта**:
   ```bash
   cd tax-subsidies
   ```

3. **Разверните базу данных Postgres**:
   Убедитесь, что у вас установлен Docker и Docker Compose. Запустите команду:
   ```bash
   docker-compose up -d
   ```

4. **Запустите проект**:
   Убедитесь, что у вас установлен [Maven](https://maven.apache.org/). Запустите проект командой:
   ```bash
   mvn spring-boot:run
   ```

5. **Swagger UI будет доступен по URL**:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## Основные возможности сервиса

- Получение всех записей с пагинацией и сортировкой
- Доступ к отдельным записям по ID
- Создание, обновление и удаление записей
- Фильтрация записей по параметрам (реализовано только для профиля `JpaEngine`)
- Получение среднего значения налоговой субсидии
- Реализована работа с spring профилями. Возможные варианты: `CsvEngine`, `JdbcEngine`, `JpaEngine`
- Реализована защита api с помощью авторизации. Для тестирования доступны пользователи:

 ```json
[
  {
    "name": "user",
    "password": "userPass",
    "role": "USER"
  },
  {
    "name": "admin",
    "password": "adminPass",
    "role": "ADMIN"
  }
]
```

## Использование API

### 1. Получить все записи с пагинацией

- **URL**: `http://localhost:8080/api/tax-subsidies`
- **Метод**: `GET`
- **Параметры**:
    - `page` (int, default: 0) - Номер страницы
    - `size` (int, default: 100) - Количество элементов на странице
    - `sortBy` (String, default: "referenceArea") - Поле для сортировки
    - `sortDir` (String, default: "asc") - Направление сортировки ("asc" или "desc")

**Пример запроса**:

```http
GET http://localhost:8080/api/tax-subsidies?page=0&size=100&sortBy=timePeriod&sortDir=desc
```

### 2. Получить одну запись по ID

- **URL**: `http://localhost:8080/api/tax-subsidies/{id}`
- **Метод**: `GET`

**Пример запроса**:

```http
GET http://localhost:8080/api/tax-subsidies/550e8400-e29b-41d4-a716-446655440000
```

### 3. Добавить новую запись

- **URL**: `http://localhost:8080/api/tax-subsidies`
- **Метод**: `POST`
- **Тело запроса**:

```json
{
  "referenceArea": "Argentina",
  "measure": "Effective average tax rate",
  "unitOfMeasure": "Percentage of taxable income",
  "regime": "Regime 1",
  "timePeriod": 2000,
  "observationValue": 30.55,
  "regimeName": "Software Promotional Regime - ARG"
}
```

**Пример запроса**:

```http
POST http://localhost:8080/api/tax-subsidies
Content-Type: application/json

{
  "referenceArea": "Argentina",
  "measure": "Effective average tax rate",
  "unitOfMeasure": "Percentage of taxable income",
  "regime": "Regime 1",
  "timePeriod": 2000,
  "observationValue": 30.55,
  "regimeName": "Software Promotional Regime - ARG"
}
```

### 4. Обновить существующую запись

- **URL**: `http://localhost:8080/api/tax-subsidies`
- **Метод**: `PUT`
- **Тело запроса** (требуется полный объект с ID):

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "referenceArea": "Argentina",
  "measure": "Effective average tax rate",
  "unitOfMeasure": "Percentage of taxable income",
  "regime": "Regime 1",
  "timePeriod": 2000,
  "observationValue": 30.55,
  "regimeName": "Software Promotional Regime - ARG"
}
```

### 5. Удалить запись по ID

- **URL**: `http://localhost:8080/api/tax-subsidies/{id}`
- **Метод**: `DELETE`

**Пример запроса**:

```http
DELETE http://localhost:8080/api/tax-subsidies/550e8400-e29b-41d4-a716-446655440000
```

### 6. Получить среднее значение налоговой субсидии

- **URL**: `http://localhost:8080/api/tax-subsidies/get-avg-observation-value`
- **Метод**: `GET`

**Пример ответа**:

```http
GET http://localhost:8080/api/tax-subsidies/get-avg-observation-value
```

### 7. Поиск записей по фильтру

- **URL**: `http://localhost:8080/api/tax-subsidies/find-by-filter`
- **Метод**: `GET`
- **Параметры**:
    - `referenceArea` (необязательный) - Регион применения
    - `measure` (необязательный) - Мера поддержки
    - `unitOfMeasure` (необязательный) - Единица измерения
    - `timePeriod` (необязательный) - Временной период

**Пример запроса**:

```http
GET http://localhost:8080/api/tax-subsidies/find-by-filter?referenceArea=Argentina&timePeriod=2000
```

## Технологии

- Java 17
- Spring Boot 3
- Spring Data JPA
- H2 Database (или Postgres в зависимости от профиля)
- OpenCSV (для CSV-реализации)
- Swagger 3 (OpenAPI)
- Maven
- Lombok