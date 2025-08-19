package frc.robot.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.File;

public class LoggingSystem {
    // ANSI Color codes
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String YELLOW = "\033[33m";
    public static final String GREEN = "\033[32m";
    public static final String CYAN = "\033[36m";
    public static final String BLUE = "\033[34m";
    public static final String PURPLE = "\033[35m";
    public static final String WHITE = "\033[37m";

    // Text formatting
    public static final String BOLD = "\033[1m";
    public static final String BOLD_RES = "\033[22m";
    public static final String ITALICS = "\033[3m";
    public static final String UNDERLINE = "\033[4m";

    // Mode info class
    public static class ModeInfo {
        public String color;
        public String name;

        public ModeInfo(String color, String name) {
            this.color = color;
            this.name = name;
        }
    }

    // Initialize with default mode
    public static ModeInfo lastMode = new ModeInfo(RESET, "[INIT]");

    // Log file streams
    private static PrintStream logFile;
    private static PrintStream originalOut;
    private static PrintStream originalErr;

    // Get current timestamp with milliseconds
    private static String currentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return now.format(formatter);
    }

    // Setup logging to redirect output to file
    public static void setupLogging() {
        try {
            // Store original streams
            originalOut = System.out;
            originalErr = System.err;

            // Try different log file locations based on the environment
            String logPath;
            if (new File("/home/pi").exists()) {
                logPath = "/home/pi/robot.log";
            } else {
                // Fallback for development/testing on Windows
                logPath = System.getProperty("user.home") + "/robot.log";
            }

            // Create log file and append to it
            logFile = new PrintStream(new FileOutputStream(logPath, true));

            // Redirect System.out and System.err to log file only
            System.setOut(logFile);
            System.setErr(logFile);

            // Log the start of logging (this will now go to the file)
            System.out.println("[" + currentTime() + "] " + BOLD + GREEN + "[INFO] " + RESET
                    + "Logging system initialized - " + logPath);

        } catch (IOException e) {
            // Use original err to report this error since we might not have logging set up
            // yet
            if (originalErr != null) {
                originalErr.println("Failed to setup logging: " + e.getMessage());
            } else {
                System.err.println("Failed to setup logging: " + e.getMessage());
            }
        }
    }

    // Method to restore original streams (useful for debugging)
    public static void restoreOriginalStreams() {
        if (originalOut != null) {
            System.setOut(originalOut);
        }
        if (originalErr != null) {
            System.setErr(originalErr);
        }
    }

    // Close logging resources
    public static void closeLogging() {
        try {
            if (logFile != null) {
                logFile.close();
            }
        } catch (Exception e) {
            System.err.println("Failed to close logging: " + e.getMessage());
        }
    }

    // Default log methods
    public static void logInfo(String message) {
        System.out.println("[" + currentTime() + "] " + BOLD + GREEN + "[INFO] " + RESET + message);
    }

    public static void logWarn(String message) {
        System.out.println("[" + currentTime() + "] " + YELLOW + "[WARN] " + RESET + message);
    }

    public static void logError(String message) {
        System.err.println("[" + currentTime() + "] " + RED + "[ERROR] " + RESET + message);
    }

    // Mode-specific log methods
    public static void logAutonomous(String message) {
        System.out.println("[" + currentTime() + "] " + PURPLE + "[AUTONOMOUS] " + RESET + message);
    }

    public static void logTeleop(String message) {
        System.out.println("[" + currentTime() + "] " + CYAN + "[TELEOP] " + RESET + message);
    }

    public static void logTest(String message) {
        System.out.println("[" + currentTime() + "] " + YELLOW + "[TEST] " + RESET + message);
    }

    public static void logDisabled(String message) {
        System.out.println("[" + currentTime() + "] " + lastMode.color + lastMode.name + RESET + message);
    }
}