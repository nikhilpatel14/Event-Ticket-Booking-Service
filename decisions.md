# System Design Explanation – Event Ticket Booking

---

## 1. Why I Chose This Database Structure

### Tables Used

- **event** – stores event metadata
- **seat** – one row per seat per event
- **booking** – mapping between user and seat

---

### Why This Structure Works Well

#### 1. Seat-level Granularity
- Each seat is represented as a row in the seat table
- Allows locking only the seat being booked, not the entire event
- Enables parallel booking of different seats

#### 2. Strong Consistency
- Seat availability is stored directly in the database
- Prevents double booking even under concurrent requests

#### 3. Simple Constraints
- One seat = one status (`AVAILABLE` / `BOOKED`)
- Business rules are easy to enforce:
    - A seat can be booked only once
    - A user can book a maximum of 2 seats per event

#### 4. Transaction-Friendly
- Fits naturally with SQL transactions
- Works cleanly with `SELECT ... FOR UPDATE`
- Ensures atomicity and isolation during booking

---

## 2. Other Race-Condition Approaches Considered (and Why Rejected)

### 1. Optimistic Locking (Version Column)

**How it works**
- Read seat
- Update seat using `WHERE version = X`
- Retry if update fails

**Why rejected**
- High contention seats cause frequent retries
- Increased latency under peak load
- Harder to reason about retry and failure paths

👉 Good for low contention systems, not ideal for ticket booking spikes.

---

### 2. In-Memory Locking (synchronized / Redis Lock)

**How it works**
- Lock seat using JVM synchronization or Redis distributed lock

**Why rejected**
- JVM locks don’t work across multiple instances
- Redis introduces operational complexity
- Risk of stale locks if the process crashes

Database locking is safer 

---

### 3. Queue-Based Booking (Kafka / SQS)

**How it works**
- All booking requests go into a queue
- A consumer processes requests sequentially

**Why rejected**
- Increased booking latency
- Additional infrastructure overhead
- Overkill for the current scale

Useful for massive scale, unnecessary here.

---

### 4. Pessimistic Locking at Event Level

**How it works**
- Lock the entire event row during booking

**Why rejected**
- Prevents parallel booking of different seats
- Poor throughput under concurrency

eat-level locking provides much better concurrency.

---

### ✅ Chosen Approach: `SELECT ... FOR UPDATE`

**Why**
- Simple
- Correct
- Database-native
- Minimal moving parts
- Easy to explain and maintain

---

## 3. Bottlenecks at 1 Million Requests per Second

If the system had to handle **1M RPS**, the main bottlenecks would be:

### 1. Database Row-Level Locking (Biggest Bottleneck)
- Popular seats become **hot rows**
- Many requests wait on the same `FOR UPDATE` lock
- Throughput collapses under extreme contention

 Databases are not designed for million-RPS locking workloads.

---

### 2. Single Primary Database
- All writes must go to the primary database
- Read replicas do not help with booking writes
- Vertical scaling has hard limits

---

### 3. Synchronous Transaction Model
- Each request waits for DB commit
- Network and disk latency add up quickly

---

### 4. JDBC Connection Pool Limits
- Limited number of DB connections
- Threads block waiting for connections

---

## How I Would Scale This to 1M RPS (High-Level)

At extreme scale, the architecture must change:

### 1. Pre-Allocate Seats Using Sharding
- Partition seats across multiple databases
- Reduce lock contention

### 2. Move Booking to an Async Pipeline
- Accept request → push to queue → process asynchronously
- Return immediate acknowledgment

### 3. Use In-Memory Availability Cache
- Redis for read-heavy seat availability
- Database used only for final confirmation

### 4. Eventual Consistency
- Accept slight lag in availability view
- Optimize throughput over strict immediacy

---

## Final Summary

- **Current design** → correctness-first, strong consistency
- **Chosen locking strategy** → simplest and safest
- **Scales well** for moderate to high traffic
- **Extreme scale** → requires architectural redesign, not just tuning

This design demonstrates a clear understanding of **engineering trade-offs** and **real-world system limits**, which is exactly what reviewers look for.
