package frc.robot.subsystems;


import frc.robot.utilities.LoggingSystem;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * Simple drivetrain subsystem for testing servo functionality
 */
public class DrivetrainSubsystem {
    
    // Motor channels
    private final int ROTATION_MOTOR_CHANNEL = 0;
    private final int LEFT_MOTOR_CHANNEL = 1;
    private final int RIGHT_MOTOR_CHANNEL = 2;
    
    // Gyro for rotation measurements
    private final ADXRS450_Gyro gyro;
    
    // Simple position tracking
    private double xPosition = 0.0;
    private double yPosition = 0.0;
    private double angle = 0.0;
    
    public DrivetrainSubsystem() {
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
     * Drive the robot using simple speed values
     * @param vx Forward velocity (m/s)
     * @param omega Rotational velocity (rad/s)
     */
    public void drive(double vx, double omega) {
        // In a real implementation, this would set motor speeds
        // For now, just display values on dashboard
        SmartDashboard.putNumber("Forward Speed", vx);
        SmartDashboard.putNumber("Rotation Speed", omega);
        
        // Log motor speeds for debugging
        SmartDashboard.putNumber("Left Motor", vx);
        SmartDashboard.putNumber("Right Motor", vx);
        SmartDashboard.putNumber("Rotation Motor", omega);
    }
    
    /**
     * Stop all drivetrain motors
     */
    public void stop() {
        // In a real implementation, this would stop motors
        SmartDashboard.putNumber("Forward Speed", 0);
        SmartDashboard.putNumber("Rotation Speed", 0);
    }
}