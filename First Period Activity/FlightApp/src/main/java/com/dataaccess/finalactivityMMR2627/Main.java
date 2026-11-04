package com.dataaccess.finalactivityMMR2627;

public class Main {
    public static void main(String[] args) {
        if (!DatabaseManager.isDatabaseAvailable()) {
            System.err.println("Database is not available");
            return;
        }

        if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
            printHelp();
            return;
        }

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
}p