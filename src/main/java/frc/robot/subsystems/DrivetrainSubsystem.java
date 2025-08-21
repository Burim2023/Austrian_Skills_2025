package frc.robot.subsystems;

import frc.robot.utilities.LoggingSystem;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.studica.frc.TitanQuad;

/**
 * Simple drivetrain subsystem for testing servo functionality
 */
public class DrivetrainSubsystem {
    
    // Motor channels
    private final int BACK_MOTOR_CHANNEL = 1;
    private final int LEFT_MOTOR_CHANNEL = 2;
    private final int RIGHT_MOTOR_CHANNEL = 0;
    
    // Gyro for rotation measurements
    private final ADXRS450_Gyro gyro;
    public TitanQuad rightMotor;
    public TitanQuad leftMotor;
    public TitanQuad backMotor;
    
    
    // Simple position tracking
    private double xPosition = 0.0;
    private double yPosition = 0.0;
    private double angle = 0.0;
    
    public DrivetrainSubsystem() {
        rightMotor = new TitanQuad(42, RIGHT_MOTOR_CHANNEL);
        leftMotor = new TitanQuad(42, LEFT_MOTOR_CHANNEL);
        backMotor = new TitanQuad(42, BACK_MOTOR_CHANNEL);
        // Initialize gyro
        gyro = new ADXRS450_Gyro();
        calibrateGyro();
        
        LoggingSystem.logInfo("Simplified drivetrain subsystem initialized");


    }
    
    public void calibrateGyro() {
        LoggingSystem.logInfo("Calibrating gyro...");
        gyro.calibrate();
        LoggingSystem.logInfo("Gyro calibration complete");
    }
    
    public void resetOdometry() {
        xPosition = 0.0;
        yPosition = 0.0;
        angle = 0.0;
        gyro.reset();
        LoggingSystem.logInfo("Odometry reset");
    }
    
    /**
     * Called periodically to update systems
     */
    public void periodic() {
        // Update dashboard values
        SmartDashboard.putNumber("Robot X Position", xPosition);
        SmartDashboard.putNumber("Robot Y Position", yPosition);
        SmartDashboard.putNumber("Robot Angle (degrees)", gyro.getAngle());
    }
    
    /**
     * Drive the robot using joystick inputs
     * @param forward Forward/backward speed from left joystick Y (-1.0 to 1.0)
     * @param strafe Left/right speed from left joystick X (-1.0 to 1.0)
     * @param rotation Rotation speed from right joystick X (-1.0 to 1.0)
     */
    public void drive(double forward, double strafe, double rotation) {
        // Apply deadband to inputs (filter out small inputs to prevent drift)
        double deadband = 0.05;
        forward = Math.abs(forward) < deadband ? 0 : forward;
        strafe = Math.abs(strafe) < deadband ? 0 : strafe;
        rotation = Math.abs(rotation) < deadband ? 0 : rotation;
        
        // Calculate motor speeds for holonomic drive
        // For a three-wheel holonomic drive (2 drive wheels + 1 omni wheel)
        double leftSpeed = forward - rotation;
        double rightSpeed = forward + rotation;
        double backSpeed = strafe;
        
        // Normalize speeds to ensure no value exceeds the range [-1.0, 1.0]
        double maxSpeed = Math.max(Math.abs(leftSpeed), Math.max(Math.abs(rightSpeed), Math.abs(backSpeed)));
        if (maxSpeed > 1.0) {
            leftSpeed /= maxSpeed;
            rightSpeed /= maxSpeed;
            backSpeed /= maxSpeed;
        }
        
        // Set motor speeds - note motors may need to be inverted based on physical setup
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        backMotor.set(backSpeed);
        
        // Log motor speeds for debugging
        SmartDashboard.putNumber("Left Motor", leftSpeed);
        SmartDashboard.putNumber("Right Motor", rightSpeed);
        SmartDashboard.putNumber("Back Motor", backSpeed);
        
        LoggingSystem.logInfo("Drive - L: " + String.format("%.2f", leftSpeed) + 
                          " R: " + String.format("%.2f", rightSpeed) + 
                          " B: " + String.format("%.2f", backSpeed));
    }
    
    /**
     * Stop all drivetrain motors
     */
    public void stop() {
        // Stop all motors
        leftMotor.set(0);
        rightMotor.set(0);
        backMotor.set(0);
        
        // Log that motors are stopped
        SmartDashboard.putNumber("Forward Speed", 0);
        SmartDashboard.putNumber("Rotation Speed", 0);
        LoggingSystem.logInfo("Drivetrain motors stopped");
    }
}