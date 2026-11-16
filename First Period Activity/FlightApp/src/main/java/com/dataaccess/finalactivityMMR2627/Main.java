package com.dataaccess.finalactivityMMR2627;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Inicializamos el sistema de logs
        AppLogger.info("FlightApp started.");

        // 1. Comprobar disponibilidad de la base de datos
        if (!DatabaseManager.isDatabaseAvailable()) {
            AppLogger.severe("Database connection failed on startup.");
            System.err.println("Database is not available");
            return;
        }
        AppLogger.info("Database connection verified successfully.");

        // 2. Comprobar si se pide ayuda o no se pasan argumentos
        if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
            AppLogger.info("Help screen displayed.");
            printHelp();
            return;
        }

        // 3. Opción -a, --add <filename.xml>
        if (args[0].equals("-a") || args[0].equals("--add")) {
            if (args.length < 2) {
                AppLogger.warning("Command -a executed without XML file argument.");
                System.err.println("Error: Missing XML file argument.");
                printHelp();
                return;
            }

            String xmlFilePath = args[1];
            AppLogger.info("Starting passenger import from file: " + xmlFilePath);

            try {
                // Leemos y validamos los pasajeros del XML
                List<Passenger> passengers = PassengerXMLParser.parsePassengers(xmlFilePath);

                // Insertamos todos los pasajeros en la base de datos transaccionalmente
                PassengerDAO.insertPassengerTransaction(passengers);

                AppLogger.info("Successfully imported " + passengers.size() + " passengers from " + xmlFilePath);

            } catch (Exception e) {
                AppLogger.severe("Failed to import passengers from " + xmlFilePath + ". Error: " + e.getMessage());
                System.err.println("Error processing passengers: " + e.getMessage());
            }
            return;
        }

        if (args[0].equals("-f") || args[0].equals("--flights")) {
            if (args.length != 4) {
                AppLogger.warning("Malformed flights command.");
                System.err.println("Error: Expected origin, destination, and date (YYYY-MM-DD).");
                printHelp();
                return;
            }

            try {
                String origin = parseIataCode(args[1]);
                String destination = parseIataCode(args[2]);
                LocalDate date = LocalDate.parse(args[3]);
                List<Flight> flights = FlightDAO.findAvailableFlights(origin, destination, date);
                if (flights.isEmpty()) {
                    System.out.println("No available flights found.");
                } else {
                    System.out.printf("%-12s %-25s %-12s %-12s %s%n",
                            "Flight", "Airline", "Departure", "Arrival", "Free seats");
                    for (Flight flight : flights) {
                        System.out.printf("%-12s %-25s %-12s %-12s %d%n",
                                flight.flightCode(), flight.airlineName(), flight.departure(),
                                flight.arrival(), flight.availableSeats());
                    }
                }
                AppLogger.info("Displayed " + flights.size() + " available flights from "
                        + origin + " to " + destination + " on " + date + ".");
            } catch (IllegalArgumentException | DateTimeParseException e) {
                AppLogger.warning("Invalid flights command arguments: " + e.getMessage());
                System.err.println("Error: " + e.getMessage());
                printHelp();
            } catch (Exception e) {
                AppLogger.severe("Failed to retrieve available flights: " + e.getMessage());
                System.err.println("Error retrieving flights: " + e.getMessage());
            }
            return;
        }

        if (args[0].equals("-o") || args[0].equals("--origins")) {
            if (args.length != 1) {
                AppLogger.warning("Malformed origins command.");
                System.err.println("Error: --origins does not accept arguments.");
                printHelp();
                return;
            }

            try {
                List<Airport> origins = FlightDAO.findOrigins();
                if (origins.isEmpty()) {
                    System.out.println("No origin airports found.");
                } else {
                    System.out.printf("%-8s %-40s %s%n", "IATA", "Airport", "City");
                    for (Airport airport : origins) {
                        System.out.printf("%-8s %-40s %s%n",
                                airport.iataCode(), airport.name(), airport.city());
                    }
                }
                AppLogger.info("Displayed " + origins.size() + " origin airports.");
            } catch (Exception e) {
                AppLogger.severe("Failed to retrieve origin airports: " + e.getMessage());
                System.err.println("Error retrieving origin airports: " + e.getMessage());
            }
            return;
        }

        if (args[0].equals("-d") || args[0].equals("--destinations")) {
            if (args.length != 2) {
                AppLogger.warning("Malformed destinations command.");
                System.err.println("Error: Expected origin IATA code (e.g. -d BOM).");
                printHelp();
                return;
            }

            try {
                String origin = parseIataCode(args[1]);
                List<Airport> destinations = FlightDAO.findDestinations(origin);

                if (destinations.isEmpty()) {
                    System.out.println("No destinations found from origin: " + origin);
                } else {
                    System.out.printf("%-8s %-40s %s%n", "IATA", "Airport", "City");
                    for (Airport airport : destinations) {
                        System.out.printf("%-8s %-40s %s%n",
                                airport.iataCode(), airport.name(), airport.city());
                    }
                }
                AppLogger.info("Displayed " + destinations.size() + " destinations from " + origin + ".");
            } catch (IllegalArgumentException e) {
                AppLogger.warning("Invalid destination command arguments: " + e.getMessage());
                System.err.println("Error: " + e.getMessage());
                printHelp();
            } catch (Exception e) {
                AppLogger.severe("Failed to retrieve destinations: " + e.getMessage());
                System.err.println("Error retrieving destinations: " + e.getMessage());
            }
            return;
        }

        if (args[0].equals("-b") || args[0].equals("--buy")) {
            if (args.length != 4) {
                AppLogger.warning("Malformed buy command.");
                System.err.println("Error: Expected customer id, flight id, and date (e.g. -b Y1234567 FL1234 2026-12-15).");
                printHelp();
                return;
            }

            try {
                String customerId = args[1];
                String flightCode = args[2];
                LocalDate travelDate = LocalDate.parse(args[3]);

                int ticketNumber = FlightDAO.buyTicket(customerId, flightCode, travelDate);

                System.out.println("Ticket successfully purchased!");
                System.out.println("  Ticket number : " + ticketNumber);
                System.out.println("  Passenger     : " + customerId);
                System.out.println("  Flight        : " + flightCode);
                System.out.println("  Travel Date   : " + travelDate);

                AppLogger.info("Ticket #" + ticketNumber + " purchased for passenger " + customerId + " on flight " + flightCode + " for " + travelDate + ".");

            } catch (DateTimeParseException e) {
                AppLogger.warning("Invalid date format in buy command: " + args[3]);
                System.err.println("Error: Invalid date format. Please use YYYY-MM-DD.");
                printHelp();
            } catch (Exception e) {
                AppLogger.severe("Failed to purchase ticket: " + e.getMessage());
                System.err.println("Error: " + e.getMessage());
            }
            return;
        }
        // Si el comando no coincide con ninguna opción conocida
        AppLogger.warning("Unknown command received: " + args[0]);
        System.out.println("Unknown command: " + args[0]);
        printHelp();
    }

    private static String parseIataCode(String value) {
        if (!value.matches("[A-Za-z]{3}")) {
            throw new IllegalArgumentException("Airport codes must contain exactly three letters.");
        }
        return value.toUpperCase();
    }

    private static void printHelp() {
        System.out.println("Usage: flights [options]");
        System.out.println("Options:");
        System.out.println("  -h, --help: show this help");
        System.out.println("  -a, --add <filename.xml>: add the passengers in the XML file to the database.");
        System.out.println("  -f, --flights <origin> <destination> <date>: show available flights and free seats (date: YYYY-MM-DD)");
        System.out.println("  -b, --buy <customerId> <flightId> <date>: buy a ticket for a flight in a specific date");
        System.out.println("  -o, --origins: show the origins of the flights");
        System.out.println("  -d, --destinations <origin>: show the destinations of the flights from origin");
    }
}