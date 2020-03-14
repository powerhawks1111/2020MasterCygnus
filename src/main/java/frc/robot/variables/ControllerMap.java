package frc.robot.variables;

/**
 * Controller Map Functions
 * "-" is No-Mapping
 */
/**
 * Driver: Quadratic Drive
 * A Button: -
 * B Button: -
 * X Button: Line-up and Shoot
 * Y Button: -
 * Left Bumper: Slow Left Active Climb
 * Right Bumper: Slow Right Active Climb
 * Left Trigger: Variable Left Active Climb
 * Right Trigger: Variable Right Active Climb
 * 0 Degrees: Set to Color
 * 45 Degrees: -
 * 90 Degrees: Outtake the Control Panel
 * 135 Degrees: -
 * 180 Degrees: Velocity (3 to 5 Spins)
 * 225 Degrees: Intake the Control Panel
 * 270 Degrees: -
 * 315 Degrees: -
 * Left Stick Down: Auto Line-up ONLY
 * Right Stick Down: - 
 */

/**
 * Operator
 * A Button: Rev-up
 * B Button: Far Shoot
 * X Button: Middle Shoot
 * Y Button: Close Shoot
 * Left Bumper: Hold to Intake (LB + RB Purges Intake)
 * Right Bumper: Hold to Purge; Dumps Index (LB + RB Purges Intake)
 * Left Trigger: -
 * Right Trigger: -
 * 0 Degrees: -
 * 45 Degrees: -
 * 90 Degrees: Outtake Climb
 * 135 Degrees: -
 * 180 Degrees: Intake Climp
 * 225 Degrees: -
 * 270 Degrees: -
 * 315 Degrees: -
 * Left Stick Y: -
 * Left Stick X: -
 * Right Stick Y: -
 * Right Stick X: -
 * Left Stick Down: Manual Index; to Outtake
 * Right Stick Down: - 
 */

public class ControllerMap {

    //A,B,X,Y Buttons
    public static int A_BUTTON = 1;
    public static int B_BUTTON = 2;
    public static int X_BUTTON = 3;
    public static int Y_BUTTON = 4;

    //Bumpers and Triggers
    public static int L_BUMPER = 5;
    public static int R_BUMPER = 6;
    public static int L_TRIGGER = 2;
    public static int R_TRIGGER = 3;

    //DPad Degrees
    public static int DEGREES0 = 0;
    public static int DEGREES45 = 45;
    public static int DEGREES90 = 90;
    public static int DEGREES135 = 135;
    public static int DEGREES180 = 180;
    public static int DEGREES225 = 225;
    public static int DEGREES270 = 270;
    public static int DEGREES315 = 315;

    //Joysticks
    public static int L_STICKY = 1;
    public static int L_STICKX = 0;
    public static int R_STICKY = 5;
    public static int R_STICKX = 4;
    public static int L_STICKD = 9;

    //Ports
    public static int DRIVER_PORT = 0;
    public static int OPERATOR_PORT = 1;

}
