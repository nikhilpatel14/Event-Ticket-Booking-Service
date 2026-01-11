package com.example.Event_Ticket_Booking.Exceptions;

public class SeatNotFoundException extends RuntimeException {

    public SeatNotFoundException(String message) {
        super(message);
    }
}
