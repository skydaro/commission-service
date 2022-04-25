# commission-service

## Description
REST API with an endpoint for transaction commission calculation.

## Running the app
```bash
$ docker-compose up -build
```

## Endpoint

 - Http Method: POST
 - Header: content-type: application/json
 - Endpoint: /api/v1/commission 
 - By default http://localhost:8080/api/v1/commission
 - Swagger available at http://localhost:8080/swagger-ui.html

## Example Request body
```bash
{
  "date": "2022-01-01",
  "amount": "200.00",
  "currency": "EUR",
  "client_id": 42
}
```

## Example response
```bash
{
    "amount":"0.05",
    "currency":"EUR"
}
```
## Database
PostgreSQL database available with default configuration:
```bash
host: localhost
port: 5432
username: postgres
password: example
```
