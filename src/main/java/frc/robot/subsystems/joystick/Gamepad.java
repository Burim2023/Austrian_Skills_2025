package frc.robot.subsystems.joystick;

/**
 * Constants and utility methods for the Studica Multicontroller
 */
public class Gamepad {
    /*
     * Studica Multicontroller button and axis mappings
     */

    // Buttons
    public static final int BUTTON_A = 1;
    public static final int BUTTON_B = 2;
    public static final int BUTTON_X = 3;
    public static final int BUTTON_Y = 4;
    public static final int LEFT_BUMPER = 5;
    public static final int RIGHT_BUMPER = 6;
    public static final int BACK_BUTTON = 7;
    public static final int START_BUTTON = 8;
    public static final int LEFT_STICK_BUTTON = 9;
    public static final int RIGHT_STICK_BUTTON = 10;

    // Axes
    public static final int LEFT_ANALOG_X = 0;
    public static final int LEFT_ANALOG_Y = 1;
    public static final int LEFT_TRIGGER = 2;
    public static final int RIGHT_TRIGGER = 3;
    public static final int RIGHT_ANALOG_X = 4;
    public static final int RIGHT_ANALOG_Y = 5;
    
    // D-Pad values (POV hat angles in degrees)
    public static final int POV_UP = 0;
    public static final int POV_UP_RIGHT = 45;
    public static final int POV_RIGHT = 90;
    public static final int POV_DOWN_RIGHT = 135;
    public static final int POV_DOWN = 180;
    public static final int POV_DOWN_LEFT = 225;
    public static final int POV_LEFT = 270;
    public static final int POV_UP_LEFT = 315;
    public static final int POV_CENTER = -1;  // No direction pressed
    
    // Default deadband to reduce noise from joysticks when centered
    public static final double DEFAULT_DEADBAND = 0.05;
    
    /**
     * Apply a deadband to an axis value
     * @param value The raw axis value
     * @param deadband The deadband to apply
     * @return The filtered value
     */
    public static double applyDeadband(double value, double deadband) {
        if (Math.abs(value) < deadband) {
            return 0.0;
        }
        return value;
    }
    
    /**
     * Apply the default deadband to an axis value
     * @param value The raw axis value
     * @return The filtered value
     */
    public static double applyDeadband(double value) {
        return applyDeadband(value, DEFAULT_DEADBAND);
    }
    
    /**
     * Square an axis value while preserving the sign
     * This makes fine control easier while still allowing full power
     * @param value The axis value to square
     * @return The squared value with the original sign
     */
    public static double squareInput(double value) {
        return Math.copySign(value * value, value);
    }
}
