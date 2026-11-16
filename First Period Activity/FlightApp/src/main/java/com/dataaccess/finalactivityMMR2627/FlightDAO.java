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
    private static final String DESTINATIONS_SQL =
            "SELECT DISTINCT dest.iata, dest.name, c.name AS city " +
            "FROM flights f " +
            "JOIN airports src ON src.code = f.source " +
            "JOIN airports dest ON dest.code = f.destination " +
            "JOIN cities c ON c.code = dest.city " +
            "WHERE src.iata = ? " +
            "ORDER BY dest.iata";    
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

        public static List<Airport> findDestinations(String originIata) throws Exception {
        List<Airport> destinations = new ArrayList<>();
        Connection connection = DatabaseManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DESTINATIONS_SQL)) {
            statement.setString(1, originIata);

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    destinations.add(new Airport(
                            results.getString("iata"),
                            results.getString("name"),
                            results.getString("city")));
                }
            }
        }
        return destinations;
    }
    public static int buyTicket(String customerId, String flightCode, LocalDate travelDate) throws Exception {
        Connection connection = DatabaseManager.getConnection();

        // 1. Comprobar si el pasajero existe en la base de datos
        String checkPassengerSql = "SELECT COUNT(*) FROM passengers WHERE passportno = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkPassengerSql)) {
            stmt.setString(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new Exception("Passenger with passport '" + customerId + "' does not exist.");
                }
            }
        }

        // 2. Comprobar regla del PDF: El pasajero no puede tener otro billete activo para la misma fecha
        String checkExistingTicketSql =
                "SELECT COUNT(*) FROM tickets " +
                        "WHERE passportno = ? AND date_of_travel = ? AND date_of_cancellation IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(checkExistingTicketSql)) {
            stmt.setString(1, customerId);
            stmt.setDate(2, Date.valueOf(travelDate));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new Exception("Passenger already has an active ticket for date " + travelDate + ".");
                }
            }
        }

        // 3. Comprobar regla del PDF: Verificar que el vuelo existe y que quedan asientos libres
        String checkSeatsSql =
                "SELECT (p.total_seats - COUNT(t.ticket_number) FILTER (WHERE t.date_of_cancellation IS NULL)) AS free_seats " +
                        "FROM flights f " +
                        "JOIN planes p ON p.plane_id = f.plane " +
                        "LEFT JOIN tickets t ON t.flight_code = f.flight_code AND t.date_of_travel = ? " +
                        "WHERE f.flight_code = ? " +
                        "GROUP BY p.total_seats";
        try (PreparedStatement stmt = connection.prepareStatement(checkSeatsSql)) {
            stmt.setDate(1, Date.valueOf(travelDate));
            stmt.setString(2, flightCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new Exception("Flight '" + flightCode + "' does not exist.");
                }
                long freeSeats = rs.getLong("free_seats");
                if (freeSeats <= 0) {
                    throw new Exception("No available seats for flight " + flightCode + " on date " + travelDate + ".");
                }
            }
        }

        // 4. Si todas las comprobaciones son válidas, insertamos el billete y recuperamos el ticket_number generado
        String insertTicketSql =
                "INSERT INTO tickets (date_of_booking, date_of_travel, date_of_cancellation, passportno, flight_code, price) " +
                        "VALUES (CURRENT_DATE, ?, NULL, ?, ?, 0.0) RETURNING ticket_number";
        try (PreparedStatement stmt = connection.prepareStatement(insertTicketSql)) {
            stmt.setDate(1, Date.valueOf(travelDate));
            stmt.setString(2, customerId);
            stmt.setString(3, flightCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ticket_number");
                } else {
                    throw new Exception("Failed to retrieve generated ticket number.");
                }
            }
        }
    }
}
