package com.dataaccess.finalactivityMMR2627;

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

        // Si el comando no coincide con ninguna opción conocida
        AppLogger.warning("Unknown command received: " + args[0]);
        System.out.println("Unknown command: " + args[0]);
        printHelp();
    }

    private static void printHelp() {
        System.out.println("Usage: flights [options]");
        System.out.println("Options:");
        System.out.println("  -h, --help: show this help");
        System.out.println("  -a, --add <filename.xml>: add the passengers in the XML file to the database.");
        System.out.println("  -f, --flights <origin> <destination> <date>: show the flights available from origin to destination");
        System.out.println("  -b, --buy <customerId> <flightId> <date>: buy a ticket for a flight in a specific date");
        System.out.println("  -o, --origins: show the origins of the flights");
        System.out.println("  -d, --destinations <origin>: show the destinations of the flights from origin");
    }
}