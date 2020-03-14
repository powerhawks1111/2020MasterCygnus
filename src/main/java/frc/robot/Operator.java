package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.variables.ControllerMap;
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
        SmartDashboard.putNumber("Fitted Width: ", Objects.vision.getFittedWidth());
        SmartDashboard.putNumber("Fitted Height: ", Objects.vision.getFittedHeight());
        SmartDashboard.putNumber("angle: ", Objects.navx.getAngle());
    }

    public void stopShoot() {
        if (!Objects.operatorJoy.getRawButton(ControllerMap.A_BUTTON)
            && !Objects.operatorJoy.getRawButton(ControllerMap.B_BUTTON)
            && !Objects.operatorJoy.getRawButton(ControllerMap.X_BUTTON)
            && !Objects.operatorJoy.getRawButton(ControllerMap.Y_BUTTON)
            && !Objects.driverJoy.getRawButton(ControllerMap.X_BUTTON)) {
            Objects.outtake.stop();
        }
    }

    public void farShoot() {
        if (Objects.operatorJoy.getRawButton(ControllerMap.Y_BUTTON)) {
            Objects.outtake.farShoot();
        }
    }

    public void middleShoot() {
        if (Objects.operatorJoy.getRawButton(ControllerMap.X_BUTTON)) {
            Objects.outtake.middleShoot();
        }
    }

    public void closeShoot() {
        if (Objects.operatorJoy.getRawButton(ControllerMap.A_BUTTON)) {
            Objects.outtake.closeShoot();
        }
    }

    public void intake() {
        if (Objects.operatorJoy.getRawButton(ControllerMap.L_BUMPER)
            && !Objects.operatorJoy.getRawButton(ControllerMap.R_BUMPER)) {
            Objects.intake.intake(MagicNumbers.intake);
        } else if (Objects.operatorJoy.getRawButton(ControllerMap.L_BUMPER)
                   && Objects.operatorJoy.getRawButton(ControllerMap.R_BUMPER)) {
                   Objects.intake.intake(MagicNumbers.purgeIntake);
        } else {
            Objects.intake.intake(MagicNumbers.defaultIntake);
        }
    }

    public void index() {
        if (Objects.operatorJoy.getRawAxis(ControllerMap.L_STICKY) > 0.1 
            || Objects.operatorJoy.getRawAxis(ControllerMap.L_STICKY) < -0.1) {
            Objects.index.setTimer(MagicNumbers.ballSpacing + 1);
            Objects.index.indexOverride(Objects.operatorJoy.getRawAxis(ControllerMap.L_STICKY));
        } else {
            Objects.index.backgroundIndex();
        }
    }

    public void extendPiston() {
        if (Objects.operatorJoy.getPOV() == ControllerMap.DEGREES0) {
            Objects.climb.extendPiston();
        }
    }

    public void retractPiston() {
        if (Objects.operatorJoy.getPOV() == ControllerMap.DEGREES180) {
            Objects.climb.retractPiston();            
        }
    }

    public void manualOuttake() {
        if (Objects.operatorJoy.getRawButton(ControllerMap.L_STICKD)) {
            Objects.index.manualOuttake();
        } else if (!Objects.operatorJoy.getRawButton(ControllerMap.L_STICKD)) {
            Objects.index.setFlush(false);
        }
    }
}