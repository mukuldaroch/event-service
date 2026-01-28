# event.dto.request

1. ** CreateEventRequest.java**

| Column1         | Column2        |
| --------------- | -------------- |
| String          | name           |
| String          | venue          |
| EventStatusEnum | status         |
| LocalDateTime   | eventStartDate |
| LocalDateTime   | eventEndDate   |
| LocalDateTime   | salesStartDate |
| LocalDateTime   | salesEndDate   |

2. ** UpdateEventRequest.java**

| Column1         | Column2        |
| --------------- | -------------- |
| UUID            | eventId        |
| String          | name           |
| String          | venue          |
| EventStatusEnum | status         |
| LocalDateTime   | eventStartDate |
| LocalDateTime   | eventEndDate   |
| LocalDateTime   | salesStartDate |
| LocalDateTime   | salesEndDate   |

# event.dto.response

1. ** CreateEventResponse.java**

| Column1         | Column2        |
| --------------- | -------------- |
| UUID            | eventId        |
| String          | name           |
| String          | venue          |
| EventStatusEnum | status         |
| LocalDateTime   | eventStartDate |
| LocalDateTime   | eventEndDate   |
| LocalDateTime   | salesStartDate |
| LocalDateTime   | salesEndDate   |
| LocalDateTime   | createdAt      |
| LocalDateTime   | updatedAt      |

2. ** EventResponse.java**

| Column1         | Column2        |
| --------------- | -------------- |
| UUID            | eventId        |
| String          | name           |
| String          | venue          |
| EventStatusEnum | status         |
| LocalDateTime   | eventStartDate |
| LocalDateTime   | eventEndDate   |
| LocalDateTime   | salesStartDate |
| LocalDateTime   | salesEndDate   |
| LocalDateTime   | createdAt      |
| LocalDateTime   | updatedAt      |

3. ** PublishedEventResponse.java**

| Column1       | Column2        |
| ------------- | -------------- |
| UUID          | eventId        |
| String        | name           |
| String        | venue          |
| LocalDateTime | eventStartDate |
| LocalDateTime | eventEndDate   |

4. ** UpdateEventResponse.java**

| Column1         | Column2        |
| --------------- | -------------- |
| UUID            | eventId        |
| String          | name           |
| String          | venue          |
| EventStatusEnum | status         |
| LocalDateTime   | eventStartDate |
| LocalDateTime   | eventEndDate   |
| LocalDateTime   | salesStartDate |
| LocalDateTime   | salesEndDate   |
| LocalDateTime   | createdAt      |
| LocalDateTime   | updatedAt      |
