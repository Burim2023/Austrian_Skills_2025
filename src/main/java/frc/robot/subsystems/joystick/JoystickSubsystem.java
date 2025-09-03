package frc.robot.subsystems.joystick;

import frc.robot.constants.Constants;
import frc.robot.utilities.logger.LoggingSystem;
import frc.robot.subsystems.joystick.Gamepad;
import frc.robot.subsystems.elevator.ArmSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.elevator.ExtenderSubsystem;
import frc.robot.subsystems.gripper.GripperSubsystem;
import frc.robot.subsystems.gripper.GripperTiltSubsystem;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class JoystickSubsystem{

    private Joystick controller;
    
        // ===== SUBSYSTEMS =====
        private DrivetrainSubsystem drivetrain;
        private ArmSubsystem arm;
        private GripperSubsystem gripper;
        private ExtenderSubsystem extender;
        private GripperTiltSubsystem gripperTilt;
    
    // ===== CONTROLLER INPUT METHODS =====
    public void getControllerInputs() {
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
    public void controlArm() {
        // Left/Right Bumpers control arm rotation
        boolean leftBumperPressed = controller.getRawButton(Gamepad.LEFT_BUMPER);
        boolean rightBumperPressed = controller.getRawButton(Gamepad.RIGHT_BUMPER);
        
        if (leftBumperPressed && !Constants.prevLeftBumperState) {
            arm.decreasePosition();
            LoggingSystem.logInfo("Left bumper pressed - arm rotating down");
        }
        
        if (rightBumperPressed && !Constants.prevRightBumperState) {
            arm.increasePosition();
            LoggingSystem.logInfo("Right bumper pressed - arm rotating up");
        }
    }
    
    public void controlGripper() {
        // X button opens gripper incrementally, Y button closes gripper incrementally
        boolean xButtonPressed = controller.getRawButton(Gamepad.BUTTON_X);
        boolean yButtonPressed = controller.getRawButton(Gamepad.BUTTON_Y);

        // X button: close gripper by one increment
        if (xButtonPressed && !Constants.prevXButtonState) {
            gripper.decreasePosition();
            LoggingSystem.logInfo("X button pressed - closing gripper incrementally");
        }

        // Y button: open gripper by one increment
        if (yButtonPressed && !Constants.prevYButtonState) {
            gripper.increasePosition();
            LoggingSystem.logInfo("Y button pressed - opening gripper incrementally");
        }
    }
    
    public void controlGripperTilt() {
        // Stick buttons control gripper tilt
        boolean leftStickButtonPressed = controller.getRawButton(Gamepad.LEFT_STICK_BUTTON);
        boolean rightStickButtonPressed = controller.getRawButton(Gamepad.RIGHT_STICK_BUTTON);
        
        if (leftStickButtonPressed && !Constants.prevLeftStickButtonState) {
            gripperTilt.decreasePosition();
            LoggingSystem.logInfo("Left stick button pressed - tilting gripper up");
        }
        
        if (rightStickButtonPressed && !Constants.prevRightStickButtonState) {
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
    public void controlExtender() {
        boolean startButtonPressed = controller.getRawButton(Gamepad.START_BUTTON);
        boolean backButtonPressed = controller.getRawButton(Gamepad.BACK_BUTTON);

        // START button: extend incrementally
        if (startButtonPressed && !Constants.prevStartButtonState) {
            extender.extend();
            LoggingSystem.logInfo("Start button pressed - extending extender incrementally");
        }

        // BACK button: retract incrementally
        if (backButtonPressed && !Constants.prevBackButtonState) {
            extender.retract();
            LoggingSystem.logInfo("Back button pressed - retracting extender incrementally");
        }

        boolean aButtonPressed = controller.getRawButton(Gamepad.BUTTON_A);
        boolean bButtonPressed = controller.getRawButton(Gamepad.BUTTON_B);

        if (aButtonPressed && !Constants.prevAButtonState) {
            extender.goToExtended();
            LoggingSystem.logInfo("A button pressed - extender to full extension");
        }

        if (bButtonPressed && !Constants.prevBButtonState) {
            extender.goToRetracted();
            LoggingSystem.logInfo("B button pressed - extender to full retraction");
        }
    }
    
    public void controlDrivetrain() {
        // Analog sticks control drivetrain
        double forwardSpeed = -controller.getRawAxis(Gamepad.LEFT_ANALOG_Y) * Constants.MAX_DRIVE_SPEED;
        double rotationSpeed = controller.getRawAxis(Gamepad.RIGHT_ANALOG_X) * Constants.MAX_ROTATION_SPEED;
        
        DrivetrainSubsystem.drive(forwardSpeed, rotationSpeed);
    }
}
