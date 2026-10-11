# Практическая работа №8

## Тема

Микросервисная система обезличивания персональных данных в выгрузках из реляционных баз данных.

## Краткое описание

Система моделирует создание обезличенной выгрузки из набора данных `customers`. Пользователь выбирает поля, которые должны войти в выгрузку, например `fullName`, `email` и `phone`. Система проверяет допустимость выбранных полей, создаёт задание, выполняет демонстрационное обезличивание и возвращает результат со статусом `READY`.

## Схема проекта

```text
Клиент / Postman
        │
        ▼
Traefik :80 (внешний reverse proxy)
        │
        ▼
API Gateway :8080 (внутри Docker-сети)
 ├── /api/datasets       → Dataset Service
 ├── /api/exports        → Export Service
 └── /api/anonymizations → Anonymizer Service

Export Service ── WebClient + X-Request-ID ──► Dataset Service
      │                                               │
      ▼                                               ▼
 export_db                                       dataset_db
      │
      │ ExportRequested
      ▼
 RabbitMQ: anonymization.events
      │
      ▼
Anonymizer Service
      │
      ▼
anonymizer_db
      │
      │ ExportReady
      └──────────────────────────────────────► Export Service
```

## Контейнеры и их назначение

| Контейнер | Назначение |
|---|---|
| `traefik` | Внешний reverse proxy. Принимает HTTP-запросы клиента на порту `80` и по Docker labels направляет их в `api-gateway`. |
| `api-gateway` | Единая точка входа приложения внутри Docker-сети. Маршрутизирует API-запросы в нужный сервис и передаёт `X-Request-ID`. |
| `dataset-service` | Хранит метаданные доступных наборов данных и разрешённых полей. Проверяет, можно ли включить выбранные поля в выгрузку. Использует базу `dataset_db`. |
| `export-service` | Создаёт и хранит задания на выгрузку, вызывает Dataset Service через `WebClient`, отправляет событие `ExportRequested`, принимает `ExportReady` и меняет статус задания. Предоставляет SSE-поток статусов. Использует базу `export_db`. |
| `anonymizer-service` | Получает событие `ExportRequested`, применяет демонстрационные правила маскирования, сохраняет результат и отправляет событие `ExportReady`. Использует базу `anonymizer_db`. |
| `postgres` | Один контейнер PostgreSQL с тремя логически изолированными базами данных: `dataset_db`, `export_db`, `anonymizer_db`. Каждый сервис работает только со своей базой. |
| `rabbitmq` | Брокер сообщений для асинхронного взаимодействия Export Service и Anonymizer Service через exchange `anonymization.events`. |

## Взаимодействие сервисов

1. Клиент обращается к Traefik на порту `80`; Traefik перенаправляет запрос в API Gateway.
2. API Gateway маршрутизирует запрос к нужному сервису. Export Service получает код набора и список выбранных полей.
3. Export Service синхронно вызывает Dataset Service через `WebClient`, чтобы проверить поля.
4. После успешной проверки Export Service создаёт задание и публикует событие `ExportRequested` в RabbitMQ.
5. Anonymizer Service получает событие, выполняет обезличивание и публикует событие `ExportReady`.
6. Export Service получает `ExportReady`, меняет статус задания на `READY` и сохраняет результат.
