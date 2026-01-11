package com.example.Event_Ticket_Booking.Service;

import com.example.Event_Ticket_Booking.Exceptions.BookingNotFoundException;
import com.example.Event_Ticket_Booking.Exceptions.MaxBookingLimitException;
import com.example.Event_Ticket_Booking.Exceptions.SeatAlreadyBookedException;
import com.example.Event_Ticket_Booking.Exceptions.SeatNotFoundException;
import com.example.Event_Ticket_Booking.Model.Seat;
import com.example.Event_Ticket_Booking.Repository.BookingRepository;
import com.example.Event_Ticket_Booking.Repository.EventRepository;
import com.example.Event_Ticket_Booking.Repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public Long createEvent(String name, int rows, int cols) {
        Long eventId = eventRepository.createEvent(name);

        for (int r = 0; r < rows; r++) {
            char rowChar = (char) ('A' + r);
            for (int c = 1; c <= cols; c++) {
                seatRepository.createSeat(eventId, String.valueOf(rowChar), c);
            }
        }
        return eventId;
    }

    @Transactional
    public void bookSeat(Long eventId, Long userId, String row, int col) {

        Seat seat = seatRepository.findSeatForUpdate(eventId, row, col);
        System.out.println("seat"+seat);
        if (seat == null) {
            throw new SeatNotFoundException(
                    "Seat does not exist  for row " + row + " and col " + col + ": Invalid Seat Number"
            );
        }

        if (!"AVAILABLE".equals(seat.getStatus())) {
            throw new SeatAlreadyBookedException("Seat already booked");
        }

        int count = bookingRepository.countActiveBookings(eventId, userId);
        if (count >= 2) {
            throw new MaxBookingLimitException("User can book max 2 seats");
        }

        seatRepository.updateStatus(seat.getId(), "BOOKED");
        bookingRepository.save(eventId, seat.getId(), userId);
    }

    @Transactional
    public void cancelSeat(Long eventId, Long userId, String row, int col) {

        Seat seat = seatRepository.findSeatForUpdate(eventId, row, col);

        boolean canceled =
                bookingRepository.cancel(eventId, seat.getId(), userId);

        if (!canceled) {
            throw new BookingNotFoundException("No booking found");
        }

        seatRepository.updateStatus(seat.getId(), "AVAILABLE");
    }
}
