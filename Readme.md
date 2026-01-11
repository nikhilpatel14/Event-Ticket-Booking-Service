### HOW TO RUN

I have included test cases in test folder , they are not included in docker run , they can be run separately 
mvn run command

Some exceptions :

1. If u try to book a set which is not exist (Seat will not exist when entire event is not present , or seat with an existing event is not present) , api will throw error message.

2. If user try to book more than two seats it will result in error with some response code.

3. If some seat is booked and again booking request sent to same seat then some error message will appear with response code


Flow :
1. Try to create an event first by create api.
2. U will get an event id by create api.
3. Try to book a seat by passing that event with valid row number and col.
4. For checking same seat booking pass same row number and col again.
5. For checking double booking try booking two seats from same user and try booking third.

## Prerequisites
- Docker
- Docker Compose

## Start Application

docker-compose up --build ( in root folder )


## Stop Application
docker-compose down

## Restart Application 
docker-compose down

docker-compose up --build

## Create Event
curl -X POST http://localhost:8080/events/create \
-H "Content-Type: application/json" \
-d '{
"rows": 10,
"cols": 10
}'

## Book Seat 
Value 1 should be replaced with actual Event Id , Row no and col number should be inside row limit and col limit
curl -X POST "http://localhost:8080/1/book?userId=42&row=B&col=7"

# Event Ticket Booking System (Spring Boot)

A backend system built using **Spring Boot + JDBC + PostgreSQL** that allows users to:
- Create events with seats
- Book seats with concurrency safety
- Cancel bookings
- Prevent double booking using database locking

---

## 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring JDBC (`JdbcTemplate`)
- PostgreSQL
- Docker & Docker Compose
- Transaction Management (`@Transactional`)

---

## 🧠 Key Features

- **Pessimistic locking (`SELECT ... FOR UPDATE`)**
- **Atomic seat booking**
- **Max 2 seats per user per event**
- **Graceful error handling with proper HTTP status codes**
- **Rollback-safe transactions**

---


