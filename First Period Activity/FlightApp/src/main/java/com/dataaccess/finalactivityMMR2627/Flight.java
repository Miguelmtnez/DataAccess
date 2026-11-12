package com.dataaccess.finalactivityMMR2627;

import java.time.LocalTime;

public record Flight(
        String flightCode,
        String airlineName,
        LocalTime departure,
        LocalTime arrival,
        long availableSeats) {
}
