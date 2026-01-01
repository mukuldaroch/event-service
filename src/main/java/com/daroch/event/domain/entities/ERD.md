## 1. events

Stores the main event data.

```text
events
------
id (UUID, PK)
organizer_id (UUID)           -- from JWT (Keycloak)
title (VARCHAR)
description (TEXT)
status (VARCHAR)              -- DRAFT, PUBLISHED, CANCELLED
start_time (TIMESTAMP)
end_time (TIMESTAMP)
location (VARCHAR)
created_at (TIMESTAMP)
updated_at (TIMESTAMP)
```

## 2. event_metadata

Extra descriptive info that may grow over time.

```text
event_metadata
--------------
event_id (UUID, PK, FK -> events.id)
category (VARCHAR)
tags (VARCHAR / JSON)
cover_image_url (VARCHAR)
language (VARCHAR)
```

## 3. event_settings

Controls behavior, not content.

```text
event_settings
--------------
event_id (UUID, PK, FK -> events.id)
is_public (BOOLEAN)
allow_registrations (BOOLEAN)
max_capacity (INT)
requires_approval (BOOLEAN)
```

## 4. event_status_history

Tracks lifecycle transitions.

```text
event_status_history
--------------------
id (UUID, PK)
event_id (UUID)
old_status (VARCHAR)
new_status (VARCHAR)
changed_by (UUID)             -- organizer_id
changed_at (TIMESTAMP)
```

Why:

- Debugging
- Auditing

This table saves careers.

## 5. event_outbox

Used for async communication later (Kafka).

```text
event_outbox
------------
id (UUID, PK)
event_id (UUID)
event_type (VARCHAR)          -- EVENT_PUBLISHED, EVENT_UPDATED
payload (JSON)
status (VARCHAR)              -- NEW, SENT, FAILED
created_at (TIMESTAMP)
```

Why:

- Enables reliable event publishing
- No tight coupling with Ticket / Registration services
- This is **how microservices scale**
