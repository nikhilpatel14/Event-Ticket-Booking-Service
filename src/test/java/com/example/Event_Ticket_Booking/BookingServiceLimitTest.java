package com.example.Event_Ticket_Booking;

import com.example.Event_Ticket_Booking.Exceptions.MaxBookingLimitException;
import com.example.Event_Ticket_Booking.Service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BookingServiceLimitTest {

    @Autowired
    private BookingService bookingService;

    @Test
    void shouldNotAllowUserToBookMoreThanTwoSeats() {

        Long eventId = bookingService.createEvent("Movie", 1, 3);
        Long userId = 201L;

        bookingService.bookSeat(eventId, userId, "A", 1);
        bookingService.bookSeat(eventId, userId, "A", 2);

        assertThrows(
                MaxBookingLimitException.class,
                () -> bookingService.bookSeat(eventId, userId, "A", 3)
        );
    }
}