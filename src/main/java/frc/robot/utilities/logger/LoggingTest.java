package frc.robot.utilities.logger;

public class LoggingTest {
    public static void main(String[] args) {
        System.out.println("Starting logging test...");

        // Setup logging - this will redirect System.out and System.err to the log file
        LoggingSystem.setupLogging();

        // Test various log types
        LoggingSystem.logInfo("This is an info message");
        LoggingSystem.logWarn("This is a warning message");
        LoggingSystem.logError("This is an error message");

        // Test mode-specific logs
        LoggingSystem.logAutonomous("Autonomous mode started");
        LoggingSystem.logTeleop("Teleop mode active");
        LoggingSystem.logTest("Test mode running");
        LoggingSystem.logDisabled("Robot disabled");

        // Test regular System.out.println (should also go to log file now)
        System.out.println("This is a regular System.out.println message - should be in log file");
        System.err.println("This is a regular System.err.println message - should be in log file");

        // Close logging
        LoggingSystem.closeLogging();

        // Restore original streams to see console output again
        LoggingSystem.restoreOriginalStreams();
        System.out.println("Logging test completed! Check the robot.log file in your home directory.");

        // Print the log file location
        String logPath;
        if (new java.io.File("/home/pi").exists()) {
            logPath = "/home/pi/robot.log";
        } else {
            logPath = System.getProperty("user.home") + "/robot.log";
        }
        System.out.println("Log file location: " + logPath);
    }
}
