Login Processing Services
Author: Keti Duracoska


The solution consists of two Spring Boot microservices:

1.login-processing-service - consumes login events from Kafka, calls the tracking service and publishes the tracking result.

2.track-login-service - exposes a secured REST endpoint and persists login events in PostgreSQL.

Kafka Topics:

1.customer-login

Input topic containing customer login events.

Example:

{
  "customerId": "550e8400-e29b-41d4-a716-446655440007",
  "username": "testuser",
  "client": "web",
  "timestamp": "2026-10-05T10:00:00Z",
  "messageId": "550e8400-e29b-41d4-a716-446655440000",
  "customerIp": "127.0.0.1"
}

2.login-tracking-result

Output topic containing the original login event information together with the tracking result.

Example:

{
  "customerId": "550e8400-e29b-41d4-a716-446655440007",
  "username": "testuser",
  "client": "web",
  "timestamp": "2026-10-05T10:00:00Z",
  "messageId": "550e8400-e29b-41d4-a716-446655440000",
  "customerIp": "127.0.0.1",
  "result": "successful"
}

REST API:

track-login-service exposes: POST /api/v1/login. The endpoint accepts a login event and stores it in PostgreSQL. The service uses HTTP Basic Authentication.

Retry Handling

If track-login-service does not return HTTP 200, login-processing-service retries the REST request three times. After the final failed attempt, the result is unsuccessful.

Database

PostgreSQL is used by track-login-service. The database schema is managed using Liquibase. The main table is: login_event with message_id as the primary key.

Kafka Retry & Dead Letter Topic

Failed Kafka messages are retried 3 times with a 3-second fixed backoff between attempts. If processing still fails after all retries, the message is published to a Dead Letter Topic.


