package frc.robot.utilities;

import frc.robot.utilities.LoggingSystem;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ShuffleboardData {

    private final ShuffleboardTab commandTab;

    public ShuffleboardData() {
        commandTab = Shuffleboard.getTab("Function Tests");

        // calibrate entire robot
        commandTab.add("Robot Base", new InstantCommand("Start", () -> {
            LoggingSystem.logInfo("Calibrating Base");
        }));

        // elevator arm
        commandTab.add("Elevator Arm", new InstantCommand("Start", () -> {
            LoggingSystem.logInfo("Calibrating Elevator Arm");
            
        }));

        // elevator lift
        commandTab.add("Elevator Lift", new InstantCommand("Start",() -> {
            LoggingSystem.logInfo("Calibrating Elevator Lift");
        }));

        // calibrate gripper
        commandTab.add("Gripper", new InstantCommand("Start",() -> {
            LoggingSystem.logInfo("Calibrating Gripper");
        }));

        // calibrate gripper tilt
        commandTab.add("Gripper Tilt", new InstantCommand("Start",() -> {
            LoggingSystem.logInfo("Calibrating Gripper Tilt");
        }));

        // calibrate arm
        commandTab.add("Elevator Rotate", new InstantCommand("Start",() -> {
            LoggingSystem.logInfo("Calibrating Elevator Rotate");
        }));
        
        // camera test
        commandTab.add("Camera Test", new InstantCommand("Start",() -> {
            LoggingSystem.logInfo("Testing Camera detection");
        }));

        // calculate positon
        commandTab.add("Calculate Position", new InstantCommand("Start",() -> {
            LoggingSystem.logInfo("Calculating Positio");
        }));
    }
}