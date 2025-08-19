package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.utilities.LoggingSystem;

/**
 * Subsystem for controlling a gripper mechanism using a servo
 */
public class GripperSubsystem {
    private Servo servo;
    
    // Expanded range for better movement visibility
    private final double GRIPPER_OPEN_POSITION = 0.0;   // Full open (0.0)
    private final double GRIPPER_CLOSED_POSITION = 1.0; // Full closed (1.0)
    private boolean isGripperOpen = true;               // Start with gripper open
    
    // Servo port
    private final int SERVO_PORT;
    
    // Manual position control
    private double currentPosition = GRIPPER_OPEN_POSITION;
    private final double POSITION_INCREMENT = 0.1;

    /**
     * Constructs a GripperSubsystem with the specified servo port
     * @param servoPort The PWM port the gripper servo is connected to
     */
    public GripperSubsystem(int servoPort) {
        SERVO_PORT = servoPort;
        
        try {
            // Initialize servo
            servo = new Servo(SERVO_PORT);
            LoggingSystem.logInfo("Gripper servo initialized on port " + SERVO_PORT);
            
            //functions to Test if Servo works right
            
            // Test servo with full range movement
            //testServo();
            
            // Start with the gripper open
            //openGripper();

        } catch (Exception e) {
            LoggingSystem.logError("Gripper servo initialization failed: " + e.getMessage());
        }
    }
    
    /**
     * Test servo by moving through full range of motion
     */
    private void testServo() {
        try {
            // Move to fully closed position
            servo.set(1.0);
            LoggingSystem.logInfo("Testing gripper - setting to position 1.0");
            Thread.sleep(500);
            
            // Move to fully open position
            servo.set(0.0);
            LoggingSystem.logInfo("Testing gripper - setting to position 0.0");
            Thread.sleep(500);
            
            // Move to middle position
            servo.set(0.5);
            LoggingSystem.logInfo("Testing gripper - setting to position 0.5");
        } catch (InterruptedException e) {
            LoggingSystem.logError("Servo test interrupted: " + e.getMessage());
        }
    }
    
    /**
     * Opens the gripper
     */
    public void openGripper() {
        try {
            servo.set(GRIPPER_OPEN_POSITION);
            currentPosition = GRIPPER_OPEN_POSITION;
            isGripperOpen = true;
            LoggingSystem.logInfo("Gripper opened - set to position " + GRIPPER_OPEN_POSITION);
            updateDashboard();
        } catch (Exception e) {
            LoggingSystem.logError("Failed to open gripper: " + e.getMessage());
        }
    }
    
    /**
     * Closes the gripper
     */
    public void closeGripper() {
        try {
            servo.set(GRIPPER_CLOSED_POSITION);
            currentPosition = GRIPPER_CLOSED_POSITION;
            isGripperOpen = false;
            LoggingSystem.logInfo("Gripper closed - set to position " + GRIPPER_CLOSED_POSITION);
            updateDashboard();
        } catch (Exception e) {
            LoggingSystem.logError("Failed to close gripper: " + e.getMessage());
        }
    }
    
    /**
     * Manually increase gripper position
     */
    public void increasePosition() {
        currentPosition = Math.min(1.0, currentPosition + POSITION_INCREMENT);
        servo.set(currentPosition);
        isGripperOpen = (currentPosition < 0.5);
        LoggingSystem.logInfo("Gripper position increased to: " + currentPosition);
        updateDashboard();
    }
    
    /**
     * Manually decrease gripper position
     */
    public void decreasePosition() {
        currentPosition = Math.max(0.0, currentPosition - POSITION_INCREMENT);
        servo.set(currentPosition);
        isGripperOpen = (currentPosition < 0.5);
        LoggingSystem.logInfo("Gripper position decreased to: " + currentPosition);
        updateDashboard();
    }
    
    /**
     * Toggles the gripper state (open to closed or closed to open)
     */
    public void toggleGripper() {
        if (isGripperOpen) {
            closeGripper();
        } else {
            openGripper();
        }
    }
    
    /**
     * Updates the SmartDashboard with gripper information
     */
    public void updateDashboard() {
        SmartDashboard.putBoolean("Gripper Open", isGripperOpen);
        SmartDashboard.putNumber("Gripper Position", servo.get());
        SmartDashboard.putNumber("Gripper Target Position", currentPosition);
    }
    
    /**
     * Periodic method to be called regularly
     */
    public void periodic() {
        // Nothing to do here for now
    }
    
    /**
     * @return true if the gripper is open, false otherwise
     */
    public boolean isOpen() {
        return isGripperOpen;
    }
}