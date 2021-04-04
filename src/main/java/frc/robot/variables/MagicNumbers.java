package frc.robot.variables;

public class MagicNumbers {
    public static final double TPC = 0.63518;// 0.317593689;
    public static double intakePower = 0.8;
    public static double activeClimbSpeed = 0.3;
    public static int defaultIntake = -1; //stop
    public static int intake = 0; //extend and pull ball in
    public static int purgeIntake = 1; //extend and push ball out
    public static int ballSpacing = 18;
    public static int intakePause = 10;
    public static int voltsPerSecond = 36;
    public static int currentLimit = 20;
    public static double targetRatio = 4.0 / 3.0;
    public static int TICKS_CHECK = 5;

    //counter for PixyCam Auto
    public int numBallsPickedUp = 0;
    public boolean successfulCompletion = false;
}
