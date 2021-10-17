package frc.robot;

import frc.robot.variables.ControllerMapGamepad;
import edu.wpi.first.wpilibj.SlewRateLimiter;
import frc.robot.variables.Objects;

public class Driver {

    public Driver() {
       
    }

    public void drive() {
        //arcadeDrive();
        // cubeDrive();
        diamondDriveCubic();
        // diamondDriveCubicRateLimiter();
        // autonomousShoot();
        activeClimb();
        //autonomousLineUp();
        //turnLightsOff();
        // pixyCamAutonomousTest();
        // manualIntake();
    }

    public void arcadeDrive() {
        if (Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY) >= 0.03 
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX) >= 0.03
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY) <= -0.03
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX) <= -0.03) {
            Objects.driveTrain.arcadeDrive2(Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY),
            Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX));
        } else if(!Objects.vision.autoLineup && !Objects.pixyCamOperate.lineup) {
            Objects.driveTrain.stop();
        }
    }
    public void cubeDrive() {
        if (Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY) >= 0.03 
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX) >= 0.03
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY) <= -0.03
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX) <= -0.03) {
            Objects.driveTrain.arcadeDrive(Objects.driveTrain.cubicDriveCalculator(Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY)), 
            (Objects.driveTrain.cubicDriveCalculator(Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX))));
        } else if(!Objects.vision.autoLineup && !Objects.pixyCamOperate.lineup) {
            Objects.driveTrain.stop();
        }
    }

    public final SlewRateLimiter m_throttleLimit = new SlewRateLimiter(3);
    public final SlewRateLimiter m_rotateLimit = new SlewRateLimiter(3);
    public void diamondDriveCubic() {
        double throttleSpeed = m_throttleLimit.calculate(Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY));
        double rotateSpeed = m_rotateLimit.calculate(Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX));

        double deadband = 0.03;
        if (   Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY) >= deadband
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX) >= deadband
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY) <= -deadband
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX) <= -deadband) {
            Objects.driveTrain.diamondDrive(Objects.driveTrain.cubicDriveCalculator(throttleSpeed), 
            (Objects.driveTrain.cubicDriveCalculatorRotate(rotateSpeed)), false);
        } 
        else if(!Objects.vision.autoLineup && !Objects.pixyCamOperate.lineup) {
            Objects.driveTrain.stop();
        }
    }

    public void diamondDriveCubicRateLimiter() {
        double deadband = 0.03;
        if (   Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY) >= deadband
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX) >= deadband
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY) <= -deadband
            || Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX) <= -deadband) {
            Objects.driveTrain.diamondDriveRateLimiter(Objects.driveTrain.cubicDriveCalculator(Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_STICKY)), 
            (Objects.driveTrain.cubicDriveCalculator(Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_STICKX))), false);
        } 
        else if(!Objects.vision.autoLineup && !Objects.pixyCamOperate.lineup) {
            Objects.driveTrain.stop();
        }
    }

    // public void autonomousShoot() {
    //     if (Objects.driverJoy.getRawButton(ControllerMap.X_BUTTON)) {
    //         Objects.visionSystems.turnLightOn();
    //         Objects.outtake.autoShoot(Objects.vision.getFittedHeight());
    //         Objects.vision.autoLineup = true;
    //     }
    //     Objects.vision.autoLineup = false;
    // }

    public void turnLightsOff() {
        if (!Objects.driverJoy.getRawButton(ControllerMapGamepad.X_BUTTON) 
            && !Objects.driverJoy.getRawButton(ControllerMapGamepad.L_STICKD)) {
            // Objects.visionSystems.turnLightOff();
            Objects.visionSystems.turnLightOn();
        }
    }

    public void activeClimb() {
        if (Objects.driverJoy.getRawButton(ControllerMapGamepad.L_BUMPER)) {
            Objects.climb.leftSlowActiveClimb();
        } else if (Objects.driverJoy.getRawButton(ControllerMapGamepad.R_BUMPER)) {
            Objects.climb.rightSlowActiveClimb();
        } else if (Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_TRIGGER) > 0) {
            Objects.climb.rightActiveClimb(Objects.driverJoy.getRawAxis(ControllerMapGamepad.R_TRIGGER));
        } else if (Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_TRIGGER) > 0) {
            Objects.climb.rightActiveClimb(-Objects.driverJoy.getRawAxis(ControllerMapGamepad.L_TRIGGER));
        } else {
            Objects.climb.stop();
        }
    }

    public void autonomousLineUp() {
        if (Objects.driverJoy.getRawButton(ControllerMapGamepad.L_STICKD)) {
            System.out.println("auto line");
            Objects.vision.autonomousLineUp();
            Objects.vision.autoLineup = true;
            Objects.visionSystems.turnLightOn();
        } else {
            Objects.vision.autoLineup = false;
        }
    }

    public void manualIntake() {
        if (Objects.driverJoy.getRawButton(ControllerMapGamepad.A_BUTTON)) {
            System.out.println("test out");
            Objects.intake.intake(0);
        }
        else {
            // Objects.intake.intake(-1);
            // System.out.println("test in");
        }
    }
    public void pixyCamAutonomousTest() {
        if (Objects.driverJoy.getRawButton(ControllerMapGamepad.Y_BUTTON)) {
            Objects.pixyCamOperate.lineup = true; //true if y button pressed --> don't stop drivetrain
            Objects.pixyCamOperate.pickUpBall2();
            Objects.intake.intake(0);
        }
        else {
            Objects.pixyCamOperate.lineup = false;
            // Objects.pixyCamVision.statusUpdate();
            Objects.pixyCamOperate.intakeDown = false;
            Objects.pixyCamOperate.waiting = false;
            Objects.pixyCamOperate.waitingIntake = false;
        }
    }
}
