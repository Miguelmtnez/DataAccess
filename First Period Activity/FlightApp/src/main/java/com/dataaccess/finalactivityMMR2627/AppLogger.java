package com.dataaccess.finalactivityMMR2627;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {

    private static final Logger LOGGER = Logger.getLogger("FlightAppLogger");
    private static boolean isInitialized = false;

    public static void init() {
        if (isInitialized) {
            return;
        }

        try {
            FileHandler fileHandler = new FileHandler("flightapp.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
            LOGGER.setUseParentHandlers(false);

            isInitialized = true;
        } catch (IOException e) {
            System.err.println("Could not initialize log file: " + e.getMessage());
        }
    }

    public static void info(String message) {
        init();
        LOGGER.info(message);
    }

    public static void warning(String message) {
        init();
        LOGGER.warning(message);
    }

    public static void severe(String message) {
        init();
        LOGGER.severe(message);
    }
}