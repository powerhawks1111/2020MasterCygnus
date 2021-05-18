package frc.robot;

import frc.robot.variables.ControllerMapJoystick;
import frc.robot.variables.Objects;

public class DriverJoystickTest {
    public DriverJoystickTest() {

    }

    public void drive() {
        arcadeDrive();
    }

    /**
     * Evaluates whether or not any axis of the stick is outside the deadzone and the robot should drive
     * @param deadZ - value of half the width of the deadzone. Normally 0.03
     * @return Boolean value of true if any axis of the stick is outside the deadzone
     */
    private boolean shouldDrive(double deadZ) {
        double deadZone = deadZ;
        return Objects.driverJoy.getRawAxis(ControllerMapJoystick.STICK_X) >= deadZone
            || Objects.driverJoy.getRawAxis(ControllerMapJoystick.STICK_X) <= -deadZone
            || Objects.driverJoy.getRawAxis(ControllerMapJoystick.STICK_Y) >= deadZone
            || Objects.driverJoy.getRawAxis(ControllerMapJoystick.STICK_Y) <= -deadZone
            || Objects.driverJoy.getRawAxis(ControllerMapJoystick.TWIST) >= deadZone
            || Objects.driverJoy.getRawAxis(ControllerMapJoystick.TWIST) <= -deadZone;
    }

    /**
     * Evaluates whether or not the drive functions should tell the drivetrain to stop moving
     * @return Boolean value of true if no other systems are trying to use the drivetrain
     */
    private boolean shouldLockMotors() {
        return !Objects.vision.autoLineup || !Objects.pixyCamOperate.lineup;
    }

    /**
     * Drives the robot
     */
    public void arcadeDrive() {
        if (shouldDrive(0.03)) {
            Objects.driveTrain.arcadeDrive2(Objects.driverJoy.getRawAxis(ControllerMapJoystick.STICK_Y),
            Objects.driverJoy.getRawAxis(ControllerMapJoystick.STICK_X));
        }
        else if(shouldLockMotors()) {
            Objects.driveTrain.stop();
        }
    }

    public void cubeDrive() {
        double cubicSpeed = Objects.driveTrain.cubicDriveCalculator(Objects.driverJoy.getRawAxis(ControllerMapJoystick.STICK_Y));
        double cubicRotate = Objects.driveTrain.cubicDriveCalculator(Objects.driverJoy.getRawAxis(ControllerMapJoystick.STICK_X));
        if (shouldDrive(0.03)) {
            Objects.driveTrain.arcadeDrive(cubicSpeed, cubicRotate);
        }
        else if(shouldLockMotors()) {
            Objects.driveTrain.stop();
        }
    }

    public void autoBallPickup() {
        if (Objects.driverJoy.getRawButton(ControllerMapJoystick.BUTTON7)) {
            Objects.pixyCamOperate.lineup = true; //true if 7 button pressed --> don't stop drivetrain
            Objects.pixyCamOperate.pickUpBall2();
        }
        else {
            Objects.pixyCamOperate.lineup = false;
            Objects.pixyCamVision.statusUpdate();
            Objects.pixyCamOperate.intakeDown = false;
            Objects.pixyCamOperate.waiting = false;
        }
    }
}
