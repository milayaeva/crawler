##  HTTP API

### Получение заголовков страниц (`<title>`) по URL-списку
**Метод:** `POST /api/title`  
**Формат запроса:** `application/json`  
**Описание:**  
Отправьте список URL, и сервис вернёт заголовки (`<title>`) страниц.

**Запуск приложения:**
`sbt compile`
`sbt run`

---

### Пример запроса:
```json
{
  "urls": [
    "https://scala-lang.org",
    "https://http4s.org"
  ]
}
```

---

### Пример ответа:
```json
{
  "https://scala-lang.org": {
    "success": true,
    "message": "The Scala Programming Language"
  },
  "https://http4s.org": {
    "success": true,
    "message": "http4s"
  }
}
```