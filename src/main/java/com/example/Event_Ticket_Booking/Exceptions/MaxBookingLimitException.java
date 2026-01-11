package com.example.Event_Ticket_Booking.Exceptions;

public class MaxBookingLimitException extends RuntimeException {

    public MaxBookingLimitException(String message) {
        super(message);
    }
}
