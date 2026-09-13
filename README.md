# tech-challenge
Technical challenge
## Stack

- Java 21, Spring Boot 3.3.4
- Spring Data JPA + H2 (in-memory)
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito, AssertJ, Spring `@DataJpaTest` / `@SpringBootTest`

## Running it

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 console: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:pricingdb`, user `sa`, empty password)

Run the tests:

```bash
mvn test
```

## API

```
GET /api/v1/prices/applicable?brandId={id}&productId={id}&applicationDate={ISO-8601 LocalDateTime}
```

Example:

```bash
curl "http://localhost:8080/api/v1/prices/applicable?brandId=1&productId=35455&applicationDate=2020-06-14T16:00:00"
```

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```
### Errors

| Scenario | Status |
|---|---|
| No price window covers the requested date/product/brand | 404 |
| Missing required query parameter | 400 |
| Malformed `applicationDate` or non-numeric id | 400 |
| `brandId`/`productId` not positive | 400 |

Curl
```
curl -X 'GET' \
  'http://localhost:8080/api/v1/prices/applicable?brandId=1&productId=35455&applicationDate=2020-06-14T16%3A00%3A00' \
  -H 'accept: */*'
```