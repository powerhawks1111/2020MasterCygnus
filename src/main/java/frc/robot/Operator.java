package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.variables.ControllerMapGamepad;
import frc.robot.variables.MagicNumbers;
import frc.robot.variables.Objects;

public class Operator {

    public Operator() {

    }

    public void operate() {
        farShoot();
        middleShoot();
        closeShoot();
        intake();
        index();
        extendPiston();
        retractPiston();
        manualOuttake();
        stopShoot();
        shouldStopIndex();
        SmartDashboard.putNumber("Fitted Width: ", Objects.vision.getFittedWidth());
        SmartDashboard.putNumber("Fitted Height: ", Objects.vision.getFittedHeight());
        SmartDashboard.putNumber("angle: ", Objects.navx.getAngle());
    }

    public void stopShoot() {
        if (!Objects.operatorJoy.getRawButton(ControllerMapGamepad.A_BUTTON)
            && !Objects.operatorJoy.getRawButton(ControllerMapGamepad.B_BUTTON)
            && !Objects.operatorJoy.getRawButton(ControllerMapGamepad.X_BUTTON)
            && !Objects.operatorJoy.getRawButton(ControllerMapGamepad.Y_BUTTON)
            && !Objects.driverJoy.getRawButton(ControllerMapGamepad.X_BUTTON)) {
            Objects.outtake.stop();
        }
    }

    public void farShoot() {
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.Y_BUTTON)) {
            Objects.outtake.farShoot();
        } 
    }

    public void middleShoot() {
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.X_BUTTON)) {
            Objects.outtake.middleShoot();
        }
    }

    public void closeShoot() {
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.A_BUTTON)) {
            Objects.outtake.closeShoot();
        }
    }

    public void shouldStopIndex() {
        if (!Objects.operatorJoy.getRawButton(ControllerMapGamepad.A_BUTTON)
            && !Objects.operatorJoy.getRawButton(ControllerMapGamepad.B_BUTTON)
            && !Objects.operatorJoy.getRawButton(ControllerMapGamepad.X_BUTTON)
            && !Objects.operatorJoy.getRawButton(ControllerMapGamepad.Y_BUTTON)
            && !Objects.driverJoy.getRawButton(ControllerMapGamepad.X_BUTTON)) {
            Objects.outtake.charging = false;
            Objects.outtake.isShooting = false;
        }
    }
    public void intake() {
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.L_BUMPER)
            && !Objects.operatorJoy.getRawButton(ControllerMapGamepad.R_BUMPER)) {
            Objects.intake.intake(MagicNumbers.intake);
        } else if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.L_BUMPER)
                   && Objects.operatorJoy.getRawButton(ControllerMapGamepad.R_BUMPER)) {
                   Objects.intake.intake(MagicNumbers.purgeIntake);
        } else if (!Objects.pixyCamOperate.lineup){
            Objects.intake.intake(MagicNumbers.defaultIntake);
        }
    }

    public void index() {
        if (Objects.operatorJoy.getRawAxis(ControllerMapGamepad.L_STICKY) > 0.1 
            || Objects.operatorJoy.getRawAxis(ControllerMapGamepad.L_STICKY) < -0.1) {
            Objects.index.setTimer(MagicNumbers.ballSpacing + 1);
            Objects.index.indexOverride(Objects.operatorJoy.getRawAxis(ControllerMapGamepad.L_STICKY));
        } else {
            Objects.index.backgroundIndex();
        }
    }

    public void extendPiston() {
        if (Objects.operatorJoy.getPOV() == ControllerMapGamepad.DEGREES0) {
            Objects.climb.extendPiston();
        }
    }

    public void retractPiston() {
        if (Objects.operatorJoy.getPOV() == ControllerMapGamepad.DEGREES180) {
            Objects.climb.retractPiston();            
        }
    }

    public void manualOuttake() {
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.L_STICKD)) {
            Objects.index.manualOuttake();
        } else if (!Objects.operatorJoy.getRawButton(ControllerMapGamepad.L_STICKD)) {
            Objects.index.setFlush(false);
        }
    }
}