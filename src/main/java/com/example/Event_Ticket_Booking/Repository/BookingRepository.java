package com.example.Event_Ticket_Booking.Repository;

import org.apache.tomcat.Jar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countActiveBookings(Long eventId, Long userId) {
        return jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*) FROM booking
                WHERE event_id = ? AND user_id = ? AND status = 'BOOKED'
                """,
                Integer.class,
                eventId, userId
        );
    }

    public void save(Long eventId, Long seatId, Long userId) {
        jdbcTemplate.update(
                "INSERT INTO booking(event_id, seat_id, user_id, status) VALUES (?, ?, ?, 'BOOKED')",
                eventId, seatId, userId
        );
    }

    public boolean cancel(Long eventId, Long seatId, Long userId) {
        return jdbcTemplate.update(
                """
                UPDATE booking
                SET status = 'CANCELED'
                WHERE event_id = ? AND seat_id = ? AND user_id = ? AND status = 'BOOKED'
                """,
                eventId, seatId, userId
        ) > 0;
    }
}

