package com.example.Event_Ticket_Booking.Controller;

import com.example.Event_Ticket_Booking.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/events")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/test")
    public String test() {
        return "running fike";
    }

    @PostMapping("/create")
    public Long createEvent(@RequestBody Map<String, Integer> req) {
        return bookingService.createEvent(
                "Event",
                req.get("rows"),
                req.get("cols")
        );
    }


    @PostMapping("/{eventId}/book")
    public String book(
            @PathVariable Long eventId,
            @RequestParam Long userId,
            @RequestParam String row,
            @RequestParam int col
    ) {
        bookingService.bookSeat(eventId, userId, row, col);
        return "Seat booked";
    }

    @PostMapping("/{eventId}/cancel")
    public String cancel(
            @PathVariable Long eventId,
            @RequestParam Long userId,
            @RequestParam String row,
            @RequestParam int col
    ) {
        bookingService.cancelSeat(eventId, userId, row, col);
        return "Seat canceled";
    }
}
