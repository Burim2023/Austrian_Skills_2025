package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.utilities.logger.LoggingSystem;

public class ArmSubsystem {
    private Servo servo;
    
    // Servo position settings
    private final double SERVO_INCREMENT = 0.05;
    private double servoPosition = 0.5; // Start at middle position
    
    // Servo port
    private final int SERVO_PORT;

    /**
     * Constructs an ArmSubsystem with the specified servo port
     * @param servoPort The PWM port the servo is connected to
     */
    public ArmSubsystem(int servoPort) {
        SERVO_PORT = servoPort;
        
        try {
            // Initialize servo
            servo = new Servo(SERVO_PORT);
            LoggingSystem.logInfo("Servo initialized on port " + SERVO_PORT);
            
            // Test servo movement
            servo.set(0.0);
            LoggingSystem.logInfo("Servo set to position 0.0");
            servo.set(1.0);
            LoggingSystem.logInfo("Servo set to position 1.0");
            servo.set(0.5);
            LoggingSystem.logInfo("Servo set to position 0.5");
        } catch (Exception e) {
            LoggingSystem.logError("Servo initialization failed: " + e.getMessage());
        }
    }
    
    /**
     * Sets the arm to the middle position
     */
    public void initializeToMiddle() {
        servoPosition = 0.5;
        servo.set(servoPosition);
        SmartDashboard.putNumber("Servo Position", servoPosition);
        LoggingSystem.logInfo("Servo initialized to position: " + servoPosition);
    }
    
    /**
     * Increases the servo position by the increment amount
     */
    public void increasePosition() {
        servoPosition = Math.min(1.0, servoPosition + SERVO_INCREMENT);
        servo.set(servoPosition);
        LoggingSystem.logInfo("Servo position increased to: " + servoPosition);
        updateDashboard();
    }
    
    /**
     * Decreases the servo position by the increment amount
     */
    public void decreasePosition() {
        servoPosition = Math.max(0.0, servoPosition - SERVO_INCREMENT);
        servo.set(servoPosition);
        LoggingSystem.logInfo("Servo position decreased to: " + servoPosition);
        updateDashboard();
    }
    
    /**
     * Updates the SmartDashboard with servo information
     */
    public void updateDashboard() {
        SmartDashboard.putNumber("Servo Position", servoPosition);
        SmartDashboard.putNumber("Servo Raw Value", servo.get());
    }
    
    /**
     * Periodic method to be called regularly
     */
    public void periodic() {
        // Nothing to do here for now
    }
}