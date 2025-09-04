package frc.robot;

import frc.robot.utilities.logger.LoggingSystem;
import frc.robot.constants.Constants;
import frc.robot.subsystems.joystick.Gamepad;
import frc.robot.subsystems.joystick.JoystickSubsystem;
import frc.robot.subsystems.elevator.ArmSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.elevator.ExtenderSubsystem;
import frc.robot.subsystems.gripper.GripperSubsystem;
import frc.robot.subsystems.gripper.GripperTiltSubsystem;
import frc.robot.utilities.shuffleboard.ShuffleboardData;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    private Joystick controller; // wpilib joystick
    ArmSubsystem arm = new ArmSubsystem(Constants.ARM_SERVO_PORT);
    GripperSubsystem gripper = new GripperSubsystem(Constants.GRIPPER_SERVO_PORT);
    ExtenderSubsystem extender = new ExtenderSubsystem(Constants.EXTENDER_SERVO_PORT);
    GripperTiltSubsystem gripperTilt = new GripperTiltSubsystem(Constants.GRIPPER_TILT_SERVO_PORT);
    DrivetrainSubsystem drivetrain = new DrivetrainSubsystem();
    JoystickSubsystem joystickSub = new JoystickSubsystem();
    ShuffleboardData loadData = new ShuffleboardData();

    
    // ===== ROBOT INITIALIZATION =====
    @Override
    public void robotInit() {
        arm.initializeToMiddle();
        LoggingSystem.setupLogging();
        LoggingSystem.logInfo("Robot initialization started");

    }

    // ===== TELEOP MODE =====
    @Override
    public void teleopInit() {
        LoggingSystem.logTeleop("Teleop mode started");
        SmartDashboard.putString("Robot Mode", "Teleop");
    }

    @Override
    public void teleopPeriodic() {
        // Get all controller inputs
        joystickSub.getControllerInputs();
        
        // Control each subsystem
        joystickSub.controlArm();
        joystickSub.controlGripper();
        joystickSub.controlGripperTilt();
        joystickSub.controlExtender();
        joystickSub.controlDrivetrain();
        
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
                DrivetrainSubsystem.stop();
                DrivetrainSubsystem.resetOdometry();
            }
        } catch (Exception e) {
            LoggingSystem.logError("Error during disabled initialization: " + e.getMessage());
        }
    }
    
    @Override
    public void disabledPeriodic() {
        // Nothing to do when disabled
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
        Constants.prevLeftBumperState = controller.getRawButton(Gamepad.LEFT_BUMPER);
        Constants.prevRightBumperState = controller.getRawButton(Gamepad.RIGHT_BUMPER);
        Constants.prevXButtonState = controller.getRawButton(Gamepad.BUTTON_X);
        Constants.prevYButtonState = controller.getRawButton(Gamepad.BUTTON_Y);
        Constants.prevLeftStickButtonState = controller.getRawButton(Gamepad.LEFT_STICK_BUTTON);
        Constants.prevRightStickButtonState = controller.getRawButton(Gamepad.RIGHT_STICK_BUTTON);
        Constants.prevBackButtonState = controller.getRawButton(Gamepad.BACK_BUTTON);
        Constants.prevStartButtonState = controller.getRawButton(Gamepad.START_BUTTON);
        Constants.prevAButtonState = controller.getRawButton(Gamepad.BUTTON_A);
        Constants.prevBButtonState = controller.getRawButton(Gamepad.BUTTON_B);
    }
}