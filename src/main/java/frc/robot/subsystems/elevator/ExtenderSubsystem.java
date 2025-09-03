package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.utilities.logger.LoggingSystem;

/**
 * Subsystem for controlling an extender mechanism using a servo with incremental control
 */
public class ExtenderSubsystem {
    private Servo servo;

    // Safe operation range for testing and fine control
    private final double EXTENDER_RETRACTED_POSITION = 0.0;
    private final double EXTENDER_EXTENDED_POSITION = 1.0; // Full range for testing
    private final double EXTENDER_INCREMENT = 0.1;         // Small movements for testing

    // Servo port
    private final int SERVO_PORT;

    // Current position control
    private double currentPosition = EXTENDER_RETRACTED_POSITION;

    /**
     * Constructs an ExtenderSubsystem with the specified servo port
     */
    public ExtenderSubsystem(int servoPort) {
        SERVO_PORT = servoPort;

        try {
            // Initialize servo
            servo = new Servo(SERVO_PORT);
            LoggingSystem.logInfo("Extender servo initialized on port " + SERVO_PORT);

            // Set to retracted position and update internal state
            currentPosition = EXTENDER_RETRACTED_POSITION;
            servo.set(currentPosition);

            LoggingSystem.logInfo("Extender initialized to retracted position: " + currentPosition);
        } catch (Exception e) {
            LoggingSystem.logError("Extender servo initialization failed: " + e.getMessage());
        }
    }

    /**
     * Extend the extender incrementally (START button)
     */
    public void extend() {
        double next = Math.min(EXTENDER_EXTENDED_POSITION, currentPosition + EXTENDER_INCREMENT);
        if (next != currentPosition) {
            currentPosition = next;
            servo.set(currentPosition);
            LoggingSystem.logInfo("Extender extending - position: " + currentPosition);
        }
        updateDashboard();
    }

    /**
     * Retract the extender incrementally (BACK button)
     */
    public void retract() {
        double next = Math.max(EXTENDER_RETRACTED_POSITION, currentPosition - EXTENDER_INCREMENT);
        if (next != currentPosition) {
            currentPosition = next;
            servo.set(currentPosition);
            LoggingSystem.logInfo("Extender retracting - position: " + currentPosition);
        }
        updateDashboard();
    }

    /**
     * Go to fully extended position (A button)
     */
    public void goToExtended() {
        currentPosition = EXTENDER_EXTENDED_POSITION;
        servo.set(currentPosition);
        LoggingSystem.logInfo("Extender moved to fully extended position: " + currentPosition);
        updateDashboard();
    }

    /**
     * Go to fully retracted position (B button)
     */
    public void goToRetracted() {
        currentPosition = EXTENDER_RETRACTED_POSITION;
        servo.set(currentPosition);
        LoggingSystem.logInfo("Extender moved to fully retracted position: " + currentPosition);
        updateDashboard();
    }

    /**
     * Get current extender position
     */
    public double getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Check if extender is fully retracted
     */
    public boolean isFullyRetracted() {
        return currentPosition <= EXTENDER_RETRACTED_POSITION;
    }

    /**
     * Check if extender is fully extended
     */
    public boolean isFullyExtended() {
        return currentPosition >= EXTENDER_EXTENDED_POSITION;
    }

    /**
     * Updates the SmartDashboard with extender information
     */
    public void updateDashboard() {
        SmartDashboard.putBoolean("Extender Fully Retracted", isFullyRetracted());
        SmartDashboard.putBoolean("Extender Fully Extended", isFullyExtended());
        SmartDashboard.putNumber("Extender Position", servo.get());
        SmartDashboard.putNumber("Extender Target Position", currentPosition);
        SmartDashboard.putNumber("Extender Retracted Limit", EXTENDER_RETRACTED_POSITION);
        SmartDashboard.putNumber("Extender Extended Limit", EXTENDER_EXTENDED_POSITION);
    }

    /**
     * Periodic method to be called regularly
     */
    public void periodic() {
        updateDashboard();
    }

    /**
     * Set extender to middle position for initialization
     */
    public void initializeToMiddle() {
        currentPosition = (EXTENDER_RETRACTED_POSITION + EXTENDER_EXTENDED_POSITION) / 2;
        servo.set(currentPosition);
        LoggingSystem.logInfo("Extender initialized to middle position: " + currentPosition);
        updateDashboard();
    }
}