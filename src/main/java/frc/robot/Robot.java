package frc.robot;

import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.ExtenderSubsystem;
import frc.robot.subsystems.Gamepad;
import frc.robot.subsystems.GripperSubsystem;
import frc.robot.subsystems.GripperTiltSubsystem;
import frc.robot.utilities.LoggingSystem;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
    
    // ===== HARDWARE CONFIGURATION =====
    private final int ARM_SERVO_PORT = 18;
    private final int GRIPPER_SERVO_PORT = 19;
    private final int EXTENDER_SERVO_PORT = 20;
    private final int GRIPPER_TILT_SERVO_PORT = 21;
    
    // ===== SUBSYSTEMS =====
    private DrivetrainSubsystem drivetrain;
    private ArmSubsystem arm;
    private GripperSubsystem gripper;
    private ExtenderSubsystem extender;
    private GripperTiltSubsystem gripperTilt;
    
    // ===== CONTROLLER =====
    private Joystick controller;
    
    // ===== SPEED CONSTANTS =====
    private final double MAX_DRIVE_SPEED = 0.10; // m/s
    private final double MAX_ROTATION_SPEED = 1.0; // rad/s
    
    // ===== BUTTON STATE TRACKING =====
    private boolean prevLeftBumperState = false;
    private boolean prevRightBumperState = false;
    private boolean prevXButtonState = false;
    private boolean prevYButtonState = false;
    private boolean prevLeftStickButtonState = false;
    private boolean prevRightStickButtonState = false;
    private boolean prevBackButtonState = false;
    private boolean prevStartButtonState = false;
    private boolean prevAButtonState = false;
    private boolean prevBButtonState = false;
    private double prevExtenderInput = 0.0;
    
    // ===== ROBOT INITIALIZATION =====
    @Override
    public void robotInit() {
        LoggingSystem.setupLogging();
        LoggingSystem.logInfo("Robot initialization started");
    }

    // ===== TELEOP MODE =====
    @Override
    public void teleopInit() {
        LoggingSystem.logTeleop("Teleop mode started");
        SmartDashboard.putString("Robot Mode", "Teleop");
        
        try {
            initializeController();
            initializeSubsystems();
            setInitialPositions();
            
            LoggingSystem.logInfo("All robot components initialized for teleop mode");
        } catch (Exception e) {
            LoggingSystem.logError("Error during teleop initialization: " + e.getMessage());
        }
    }

    @Override
    public void teleopPeriodic() {
        // Get all controller inputs
        getControllerInputs();
        
        // Control each subsystem
        controlArm();
        controlGripper();
        controlGripperTilt();
        controlExtender();
        controlDrivetrain();
        
        // Update all subsystems
        updateSubsystems();
        
        // Update button states for next cycle
        updateButtonStates();
    }

    // ===== AUTONOMOUS MODE =====
    @Override
    public void autonomousInit() {
        LoggingSystem.logInfo("Autonomous mode started");
        SmartDashboard.putString("Robot Mode", "Autonomous");
        
        try {
            initializeController();
            initializeSubsystems();
            drivetrain.resetOdometry();
            
            LoggingSystem.logInfo("All robot components initialized for autonomous mode");
        } catch (Exception e) {
            LoggingSystem.logError("Error during autonomous initialization: " + e.getMessage());
        }
    }
    
    @Override
    public void autonomousPeriodic() {
        updateSubsystems();
    }

    // ===== DISABLED MODE =====
    @Override
    public void disabledInit() {
        LoggingSystem.logInfo("Disabled mode activated");
        SmartDashboard.putString("Robot Mode", "Disabled");
        
        try {
            if (drivetrain != null) {
                drivetrain.stop();
            }
        } catch (Exception e) {
            LoggingSystem.logError("Error during disabled initialization: " + e.getMessage());
        }
    }
    
    @Override
    public void disabledPeriodic() {
        // Nothing to do when disabled
    }

    // ===== INITIALIZATION METHODS =====
    private void initializeController() {
        controller = new Joystick(0);
        LoggingSystem.logInfo("Studica Multicontroller initialized on port 0");
    }
    
    private void initializeSubsystems() {
        arm = new ArmSubsystem(ARM_SERVO_PORT);
        gripper = new GripperSubsystem(GRIPPER_SERVO_PORT);
        extender = new ExtenderSubsystem(EXTENDER_SERVO_PORT);
        gripperTilt = new GripperTiltSubsystem(GRIPPER_TILT_SERVO_PORT);
        drivetrain = new DrivetrainSubsystem();
    }
    
    private void setInitialPositions() {
        arm.initializeToMiddle();
        // NO automatic gripper movement - let user control it
        // gripper.manualOpen(); // Only call if needed
    }

    // ===== CONTROLLER INPUT METHODS =====
    private void getControllerInputs() {
        // Check controller connection
        boolean controllerConnected = Math.abs(controller.getRawAxis(0)) <= 1.0;
        SmartDashboard.putBoolean("Controller Connected", controllerConnected);

        // Display all button states on dashboard
        SmartDashboard.putBoolean("Left Bumper", controller.getRawButton(Gamepad.LEFT_BUMPER));
        SmartDashboard.putBoolean("Right Bumper", controller.getRawButton(Gamepad.RIGHT_BUMPER));
        SmartDashboard.putBoolean("X Button", controller.getRawButton(Gamepad.BUTTON_X));
        SmartDashboard.putBoolean("Y Button", controller.getRawButton(Gamepad.BUTTON_Y));
        SmartDashboard.putBoolean("A Button", controller.getRawButton(Gamepad.BUTTON_A));
        SmartDashboard.putBoolean("B Button", controller.getRawButton(Gamepad.BUTTON_B));
        SmartDashboard.putBoolean("Left Stick Button", controller.getRawButton(Gamepad.LEFT_STICK_BUTTON));
        SmartDashboard.putBoolean("Right Stick Button", controller.getRawButton(Gamepad.RIGHT_STICK_BUTTON));
        SmartDashboard.putBoolean("Back Button", controller.getRawButton(Gamepad.BACK_BUTTON));
        SmartDashboard.putBoolean("Start Button", controller.getRawButton(Gamepad.START_BUTTON));
        SmartDashboard.putNumber("Left Trigger", controller.getRawAxis(Gamepad.LEFT_TRIGGER));
        SmartDashboard.putNumber("Right Trigger", controller.getRawAxis(Gamepad.RIGHT_TRIGGER));
        SmartDashboard.putNumber("D-Pad POV", controller.getPOV());

        // Display right stick Y value
        SmartDashboard.putNumber("Right Stick Y", controller.getRawAxis(Gamepad.RIGHT_ANALOG_Y));
    }

    // ===== SUBSYSTEM CONTROL METHODS =====
    private void controlArm() {
        // Left/Right Bumpers control arm rotation
        boolean leftBumperPressed = controller.getRawButton(Gamepad.LEFT_BUMPER);
        boolean rightBumperPressed = controller.getRawButton(Gamepad.RIGHT_BUMPER);
        
        if (leftBumperPressed && !prevLeftBumperState) {
            arm.decreasePosition();
            LoggingSystem.logInfo("Left bumper pressed - arm rotating down");
        }
        
        if (rightBumperPressed && !prevRightBumperState) {
            arm.increasePosition();
            LoggingSystem.logInfo("Right bumper pressed - arm rotating up");
        }
    }
    
    private void controlGripper() {
        // X button opens gripper incrementally, Y button closes gripper incrementally
        boolean xButtonPressed = controller.getRawButton(Gamepad.BUTTON_X);
        boolean yButtonPressed = controller.getRawButton(Gamepad.BUTTON_Y);

        // X button: close gripper by one increment
        if (xButtonPressed && !prevXButtonState) {
            gripper.decreasePosition();
            LoggingSystem.logInfo("X button pressed - closing gripper incrementally");
        }

        // Y button: open gripper by one increment
        if (yButtonPressed && !prevYButtonState) {
            gripper.increasePosition();
            LoggingSystem.logInfo("Y button pressed - opening gripper incrementally");
        }
    }
    
    private void controlGripperTilt() {
        // Stick buttons control gripper tilt
        boolean leftStickButtonPressed = controller.getRawButton(Gamepad.LEFT_STICK_BUTTON);
        boolean rightStickButtonPressed = controller.getRawButton(Gamepad.RIGHT_STICK_BUTTON);
        
        if (leftStickButtonPressed && !prevLeftStickButtonState) {
            gripperTilt.decreasePosition();
            LoggingSystem.logInfo("Left stick button pressed - tilting gripper up");
        }
        
        if (rightStickButtonPressed && !prevRightStickButtonState) {
            gripperTilt.increasePosition();
            LoggingSystem.logInfo("Right stick button pressed - tilting gripper down");
        }
    }
    
    /**
     * Extender control:
     * - start button to extend, Back button to retract (incremental)
     * - A button: go to fully extended
     * - B button: go to fully retracted
     */
    private void controlExtender() {
        boolean startButtonPressed = controller.getRawButton(Gamepad.START_BUTTON);
        boolean backButtonPressed = controller.getRawButton(Gamepad.BACK_BUTTON);

        // START button: extend incrementally
        if (startButtonPressed && !prevStartButtonState) {
            extender.extend();
            LoggingSystem.logInfo("Start button pressed - extending extender incrementally");
        }

        // BACK button: retract incrementally
        if (backButtonPressed && !prevBackButtonState) {
            extender.retract();
            LoggingSystem.logInfo("Back button pressed - retracting extender incrementally");
        }

        boolean aButtonPressed = controller.getRawButton(Gamepad.BUTTON_A);
        boolean bButtonPressed = controller.getRawButton(Gamepad.BUTTON_B);

        if (aButtonPressed && !prevAButtonState) {
            extender.goToExtended();
            LoggingSystem.logInfo("A button pressed - extender to full extension");
        }

        if (bButtonPressed && !prevBButtonState) {
            extender.goToRetracted();
            LoggingSystem.logInfo("B button pressed - extender to full retraction");
        }
    }
    
    private void controlDrivetrain() {
        // Analog sticks control drivetrain
        double forwardSpeed = -controller.getRawAxis(Gamepad.LEFT_ANALOG_Y) * MAX_DRIVE_SPEED;
        double rotationSpeed = controller.getRawAxis(Gamepad.RIGHT_ANALOG_X) * MAX_ROTATION_SPEED;
        
        drivetrain.drive(forwardSpeed, rotationSpeed);
    }

    // ===== UPDATE METHODS =====
    private void updateSubsystems() {
        drivetrain.periodic();
        arm.periodic();
        gripper.periodic();
        extender.periodic();
        gripperTilt.periodic();
    }
    
    private void updateButtonStates() {
        prevLeftBumperState = controller.getRawButton(Gamepad.LEFT_BUMPER);
        prevRightBumperState = controller.getRawButton(Gamepad.RIGHT_BUMPER);
        prevXButtonState = controller.getRawButton(Gamepad.BUTTON_X);
        prevYButtonState = controller.getRawButton(Gamepad.BUTTON_Y);
        prevLeftStickButtonState = controller.getRawButton(Gamepad.LEFT_STICK_BUTTON);
        prevRightStickButtonState = controller.getRawButton(Gamepad.RIGHT_STICK_BUTTON);
        prevBackButtonState = controller.getRawButton(Gamepad.BACK_BUTTON);
        prevStartButtonState = controller.getRawButton(Gamepad.START_BUTTON);
        prevAButtonState = controller.getRawButton(Gamepad.BUTTON_A);
        prevBButtonState = controller.getRawButton(Gamepad.BUTTON_B);
    }
}