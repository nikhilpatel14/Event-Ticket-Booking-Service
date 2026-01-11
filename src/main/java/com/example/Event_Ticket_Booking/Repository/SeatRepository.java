package com.example.Event_Ticket_Booking.Repository;

import com.example.Event_Ticket_Booking.Model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SeatRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createSeat(Long eventId, String row, int col) {
        jdbcTemplate.update(
                "INSERT INTO seat(event_id, seat_row, seat_col, status) VALUES (?, ?, ?, 'AVAILABLE')",
                eventId, row, col
        );
    }

    public Seat findSeatForUpdate(Long eventId, String row, int col) {
        try {
            return jdbcTemplate.queryForObject(
                    """
                    SELECT * FROM seat
                    WHERE event_id = ? AND seat_row = ? AND seat_col = ?
                    FOR UPDATE
                    """,
                    new BeanPropertyRowMapper<>(Seat.class),
                    eventId, row, col
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    public void updateStatus(Long seatId, String status) {
        jdbcTemplate.update(
                "UPDATE seat SET status = ? WHERE id = ?",
                status, seatId
        );
    }
}
