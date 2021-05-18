package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.variables.MagicNumbers;
import frc.robot.variables.Motors;
import frc.robot.subsystems.Vision;

public class Robot extends TimedRobot {

    Driver driver = new Driver();
    Operator operator = new Operator();
    Autonomous autonomous = new Autonomous();
    Test test = new Test();
    Vision cam = new Vision();

    private final SendableChooser<String> autoChooser = new SendableChooser<>();
    private static final String kDefaultAuto = "Default";
    private static final String kAuto1 = "Red Path";
    private static final String kAuto2 = "Orange Path";
    private static final String kAuto3 = "Yellow Path";
    private static final String kAuto4 = "Green Path";
    private static final String kAuto5 = "Blue Path";
    private static final String kAuto6 = "Indigo Path";
    private static final String kAuto7 = "Violet Path";
    private String autoSelected;

    private final SendableChooser<String> testChooser = new SendableChooser<>();
    private static final String kDefaultTest = "Default";
    private static final String kTest1 = "Test Index";
    private static final String kTest2 = "Test Outtake";
    private static final String kTest3 = "Test DriveTrain";
    private static final String kTest4 = "Test Intake";
    private static final String kTest5 = "Test Climb";
    private static final String kTest6 = "Test Vision";
    private String testSelected;

    public final static SendableChooser<Integer> pcStartCount = new SendableChooser<>();
    private static final int zero = 0;
    private static final int one = 1;
    private static final int two = 2;
    private static final int three = 3;

    public final static SendableChooser<String> allianceChooser = new SendableChooser<>();
    private static final String kDefaultColor = "White";
    private static final String kColor1 = "Red";
    private static final String kColor2 = "Blue";

    public void robotInit() {

        CameraServer.getInstance().startAutomaticCapture();

        autoChooser.setDefaultOption("Default Auto", kDefaultAuto);
        autoChooser.addOption("Red Path", kAuto1);
        autoChooser.addOption("Orange Path", kAuto2);
        autoChooser.addOption("Yellow Path", kAuto3);
        autoChooser.addOption("Green Path", kAuto4);
        autoChooser.addOption("Blue Path", kAuto5);
        autoChooser.addOption("Indigo Path", kAuto6);
        autoChooser.addOption("Violet Path", kAuto7);
        SmartDashboard.putData("Autonomuos Choices", autoChooser);

        testChooser.setDefaultOption("Default Test", kDefaultTest);
        testChooser.addOption("Test Index", kTest1);
        testChooser.addOption("Test Outtake", kTest2);
        testChooser.addOption("Test DriveTrain", kTest3);
        testChooser.addOption("Test Intake", kTest4);
        testChooser.addOption("Test Climb", kTest5);
        testChooser.addOption("Test Vision", kTest6);
        SmartDashboard.putData("Test Choices", testChooser);

        pcStartCount.setDefaultOption("Default: 3 Power Cells", three);
        pcStartCount.addOption("2 Power Cells", two);
        pcStartCount.addOption("1 Power Cell", one);
        pcStartCount.addOption("No Power Cells", zero);
        SmartDashboard.putData("Power Cell Chooser", pcStartCount);

        allianceChooser.setDefaultOption("Default (White)", kDefaultColor);
        allianceChooser.addOption("Red", kColor1);
        allianceChooser.addOption("Blue", kColor2);
        SmartDashboard.putData("Alliance Chooser", allianceChooser);

        Motors.indexLead.setSmartCurrentLimit(MagicNumbers.currentLimit);
        Motors.indexFollower.setSmartCurrentLimit(MagicNumbers.currentLimit);
        Motors.moveUp.setSmartCurrentLimit(MagicNumbers.currentLimit);
        Motors.intake.setSmartCurrentLimit(MagicNumbers.currentLimit);
        Motors.activeClimb.setSmartCurrentLimit(MagicNumbers.currentLimit);

        Motors.leftBack.setClosedLoopRampRate(MagicNumbers.voltsPerSecond);
        Motors.leftFront.setClosedLoopRampRate(MagicNumbers.voltsPerSecond);
        Motors.rightBack.setClosedLoopRampRate(MagicNumbers.voltsPerSecond);
        Motors.rightFront.setClosedLoopRampRate(MagicNumbers.voltsPerSecond);

        SmartDashboard.setDefaultNumber("Outtake RPM", 0.0);

        SmartDashboard.setDefaultNumber("Fitted Height: ", cam.getFittedHeight());
        SmartDashboard.setDefaultNumber("Fitted Width: ", cam.getFittedWidth());

    }

    public void robotPeriodic() {
        autoSelected = autoChooser.getSelected();
        testSelected = testChooser.getSelected();
    }

    public void autonomousInit() {
        autonomous.setStart(false);
    }

    public void autonomousPeriodic() {
        autonomous.moveDistanceTest(); //commented out code to test distance functionss
        // switch (autoSelected) {
        // case kAuto1:
        //     autonomous.redPath();
        //     break;
        // case kAuto2:
        //     autonomous.orangePath();
        //     break;
        // case kAuto3:
        //     autonomous.yellowPath();
        //     break;
        // case kAuto4:
        //     autonomous.greenPath();
        //     break;
        // case kAuto5:
        //     autonomous.bluePath();
        //     break;
        // case kAuto6:
        //     autonomous.indigoPath();
        //     break;
        // case kAuto7:
        //     autonomous.violetPath();
        //     break;
        // case kDefaultAuto:
        // default:
        //     autonomous.defaultAutonomous();
        //     break;
        // }
    }

    

    public void teleopPeriodic() {
        driver.drive();
        operator.operate();
    }

    public void testPeriodic() {
        switch (testSelected) {
        case kTest1:
            test.testIndex();
            break;
        case kTest2:
            test.testOuttake();
            break;
        case kTest3:
            test.testDriveTrain();
            break;
        case kTest4:
            test.testIntake();
            break;
        case kTest5:
            test.testClimb();
            break;
        case kTest6:
            test.testVision();
            break;
        case kDefaultTest:
        default:
            // DO NOT PUT ANYTHING HERE
            break;
        }
    }
}