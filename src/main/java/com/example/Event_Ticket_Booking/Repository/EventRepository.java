package com.example.Event_Ticket_Booking.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long createEvent(String name) {
        return jdbcTemplate.queryForObject(
                "INSERT INTO event(name) VALUES (?) RETURNING id",
                Long.class,
                name
        );
    }
}

