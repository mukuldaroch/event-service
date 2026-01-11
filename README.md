# Event Service — EventForge Microservice

The **Event Service** is a core **microservice** within the EventForge ecosystem responsible for managing the **event lifecycle**. It provides backend APIs for **creating, updating, publishing, and retrieving events**, and acts as the source of truth for event-related data.

---

## Features

- **Event Management:** Create, update, and retrieve events.
- **Event Lifecycle:** Supports draft, published, and closed event states.
- **Public Events:** Exposes published events for discovery and ticketing.
- **Sales Window Control:** Manages event start/end dates and ticket sale periods.

![Event Service Diagram](docs/assets/event-service.jpg)

---

## Database Design

The **Event Service database** stores all event-related metadata and lifecycle state.
It does **not manage tickets directly**, but provides event references used by other services like Ticket and Payment.

Key entities:

- `Event`: Core event details such as name, venue, description, and dates.
- `EventStatus`: Represents the lifecycle state of an event (DRAFT, PUBLISHED, CLOSED).

[ EventForge Full Architecture Design](https://miro.com/app/board/uXjVGVq5l3U=/?moveToWidget=3458764653985736600&cot=14)

---

## API Endpoints

Events

| Method     | Endpoint             | Description            |
| ---------- | -------------------- | ---------------------- |
| **POST**   | `/events`            | Create a new event     |
| **GET**    | `/events/{event_id}` | Retrieve event details |
| **PATCH**  | `/events/{event_id}` | Update event details   |
| **DELETE** | `/events/{event_id}` | Delete an event        |

Public Events

| Method  | Endpoint                       | Description                 |
| ------- | ------------------------------ | --------------------------- |
| **GET** | `/events/published`            | List all published events   |
| **GET** | `/events/published/{event_id}` | Get published event details |

---

## 🧩 Tech Stack

- **Backend:** Spring Boot (Java 17+)
- **Build Tool:** Gradle
- **Database:** PostgreSQL
- **Containerization:** Docker
- **Deployment:** Dockerized microservice setup (Planned)

---

## ⚙️ Running Locally

Requirements:

- Java 17+
- Gradle (wrapper included)
- PostgreSQL running locally

Steps:

```bash
# 1️⃣ Clone repo
git clone https://github.com/mukuldaroch/event-service.git
cd event-service

# Run database & dependencies
docker compose up -d

# Build project
./gradlew clean build

# Run microservice
./gradlew clean bootrun
```

---

## 🤝 Contributing

Open issues, suggest features, or submit PRs — all contributions are welcome.
Help strengthen the **event foundation** of the EventForge platform.

---

## 👨‍💻 Author

- [@Mukul Daroch](https://github.com/mukuldaroch)
