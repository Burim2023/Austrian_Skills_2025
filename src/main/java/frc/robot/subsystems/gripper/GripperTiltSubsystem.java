package frc.robot.subsystems.gripper;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.utilities.logger.LoggingSystem;

/**
 * Subsystem for controlling the tilt angle of the gripper mechanism using a servo
 */
public class GripperTiltSubsystem {
    private Servo servo;
    
    // Tilt position constants
    private final double TILT_UP_POSITION = 0.0;      // Fully tilted up
    private final double TILT_MIDDLE_POSITION = 0.5;  // Middle position
    private final double TILT_DOWN_POSITION = 1.0;    // Fully tilted down
    
    // Servo port
    private final int SERVO_PORT;
    
    // Manual position control
    private double currentPosition = TILT_MIDDLE_POSITION;
    private final double POSITION_INCREMENT = 0.05;

    /**
     * Constructs a GripperTiltSubsystem with the specified servo port
     * @param servoPort The PWM port the tilt servo is connected to
     */
    public GripperTiltSubsystem(int servoPort) {
        SERVO_PORT = servoPort;
        
        try {
            // Initialize servo
            servo = new Servo(SERVO_PORT);
            LoggingSystem.logInfo("Gripper tilt servo initialized on port " + SERVO_PORT);
            
            //functions to Test if Servo works right 
            
            // Test servo with full range movement
            //testServo();
            
            // Start with the tilt in middle position
            //setToMiddle();
        } catch (Exception e) {
            LoggingSystem.logError("Gripper tilt servo initialization failed: " + e.getMessage());
        }
    }
    
    /**
     * Test servo by moving through full range of motion
     */
    private void testServo() {
        try {
            // Move to fully down position
            servo.set(TILT_DOWN_POSITION);
            LoggingSystem.logInfo("Testing gripper tilt - setting to down position " + TILT_DOWN_POSITION);
            Thread.sleep(500);
            
            // Move to fully up position
            servo.set(TILT_UP_POSITION);
            LoggingSystem.logInfo("Testing gripper tilt - setting to up position " + TILT_UP_POSITION);
            Thread.sleep(500);
            
            // Move to middle position
            servo.set(TILT_MIDDLE_POSITION);
            LoggingSystem.logInfo("Testing gripper tilt - setting to middle position " + TILT_MIDDLE_POSITION);
        } catch (InterruptedException e) {
            LoggingSystem.logError("Gripper tilt servo test interrupted: " + e.getMessage());
        }
    }
    
    /**
     * Tilts the gripper up
     */
    public void tiltUp() {
        try {
            servo.set(TILT_UP_POSITION);
            currentPosition = TILT_UP_POSITION;
            LoggingSystem.logInfo("Gripper tilted up - set to position " + TILT_UP_POSITION);
            updateDashboard();
        } catch (Exception e) {
            LoggingSystem.logError("Failed to tilt gripper up: " + e.getMessage());
        }
    }
    
    /**
     * Tilts the gripper down
     */
    public void tiltDown() {
        try {
            servo.set(TILT_DOWN_POSITION);
            currentPosition = TILT_DOWN_POSITION;
            LoggingSystem.logInfo("Gripper tilted down - set to position " + TILT_DOWN_POSITION);
            updateDashboard();
        } catch (Exception e) {
            LoggingSystem.logError("Failed to tilt gripper down: " + e.getMessage());
        }
    }
    
    /**
     * Sets the gripper tilt to the middle position
     */
    public void setToMiddle() {
        try {
            servo.set(TILT_MIDDLE_POSITION);
            currentPosition = TILT_MIDDLE_POSITION;
            LoggingSystem.logInfo("Gripper tilt set to middle - position " + TILT_MIDDLE_POSITION);
            updateDashboard();
        } catch (Exception e) {
            LoggingSystem.logError("Failed to set gripper tilt to middle: " + e.getMessage());
        }
    }
    
    /**
     * Manually increase tilt position (tilts downward)
     */
    public void increasePosition() {
        currentPosition = Math.min(1.0, currentPosition + POSITION_INCREMENT);
        servo.set(currentPosition);
        LoggingSystem.logInfo("Gripper tilt position increased to: " + currentPosition);
        updateDashboard();
    }
    
    /**
     * Manually decrease tilt position (tilts upward)
     */
    public void decreasePosition() {
        currentPosition = Math.max(0.0, currentPosition - POSITION_INCREMENT);
        servo.set(currentPosition);
        LoggingSystem.logInfo("Gripper tilt position decreased to: " + currentPosition);
        updateDashboard();
    }
    
    /**
     * Updates the SmartDashboard with tilt information
     */
    public void updateDashboard() {
        SmartDashboard.putNumber("Gripper Tilt Position", servo.get());
        SmartDashboard.putNumber("Gripper Tilt Target", currentPosition);
    }
    
    /**
     * Periodic method to be called regularly
     */
    public void periodic() {
        // Nothing to do here for now
    }
}