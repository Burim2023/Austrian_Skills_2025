package frc.robot.constants;


public final class Constants {
    public static final int xMeter = 1;
    public static final int yMeter = 1;
    public static final int omega_degree = 1;   

    public static final int wheelRadius_mm = 50;
    public static final int robotRadius_mm = 15;
    public static final int motorLeft = 2;
    public static final int motorRight = 0;
    public static final int motorBack = 1;
    
      // ===== HARDWARE CONFIGURATION =====
      public static final int ARM_SERVO_PORT = 18;
      public static final int GRIPPER_SERVO_PORT = 19;
      public static final int EXTENDER_SERVO_PORT = 20;
      public static final int GRIPPER_TILT_SERVO_PORT = 21;
      
      // ===== SPEED CONSTANTS =====
      public static final double MAX_DRIVE_SPEED = 0.10; // m/s
      public static final double MAX_ROTATION_SPEED = 1.0; // rad/s
      
      // ===== BUTTON STATE TRACKING =====
      public static boolean prevLeftBumperState = false;
      public static boolean prevRightBumperState = false;
      public static boolean prevXButtonState = false;
      public static boolean prevYButtonState = false;
      public static boolean prevLeftStickButtonState = false;
      public static boolean prevRightStickButtonState = false;
      public static boolean prevBackButtonState = false;
      public static boolean prevStartButtonState = false;
      public static boolean prevAButtonState = false;
      public static boolean prevBButtonState = false;
      public static double  prevExtenderInput = 0.0;

}