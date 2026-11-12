package com.dataaccess.finalactivityMMR2627;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class FlightDAO {
    private static final String AVAILABLE_FLIGHTS_SQL =
            "SELECT * FROM available_flights_mmr(?, ?, ?)";
    private static final String ORIGINS_SQL =
            "SELECT DISTINCT a.iata, a.name, c.name AS city " +
            "FROM flights f " +
            "JOIN airports a ON a.code = f.source " +
            "JOIN cities c ON c.code = a.city " +
            "ORDER BY a.iata";

    private FlightDAO() {
    }

    public static List<Flight> findAvailableFlights(String origin, String destination, LocalDate date)
            throws Exception {
        List<Flight> flights = new ArrayList<>();
        Connection connection = DatabaseManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(AVAILABLE_FLIGHTS_SQL)) {
            statement.setString(1, origin);
            statement.setString(2, destination);
            statement.setDate(3, Date.valueOf(date));

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    flights.add(new Flight(
                            results.getString("flight_code"),
                            results.getString("airline_name"),
                            results.getTime("departure").toLocalTime(),
                            results.getTime("arrival").toLocalTime(),
                            results.getLong("available_seats")));
                }
            }
        }
        return flights;
    }

    public static List<Airport> findOrigins() throws Exception {
        List<Airport> airports = new ArrayList<>();
        Connection connection = DatabaseManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(ORIGINS_SQL);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                airports.add(new Airport(
                        results.getString("iata"),
                        results.getString("name"),
                        results.getString("city")));
            }
        }
        return airports;
    }
}
