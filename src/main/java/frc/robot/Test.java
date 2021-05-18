package frc.robot;

import frc.robot.variables.Objects;
import frc.robot.variables.ControllerMapGamepad;
import frc.robot.variables.Motors;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Test {

    public Test() {
        
    }

    public void testIndex() {
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.A_BUTTON)) {
            Motors.indexLead.set(.8);
            Motors.indexFollower.follow(Motors.indexLead, false);
        } else {
            Motors.indexLead.set(0);
            Motors.indexFollower.follow(Motors.indexLead, false);
        }
    }

    public void testOuttake() {
        int time1 = 20;
        int time2 = 20;
        int setPoint = 1000;
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.A_BUTTON)) {
            if (time1 >= 20) {
                setPoint += 50;
                time1 = 0;
            } else if (time1 >= 100) {
            
            } else {
                time1++;
            }
        }
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.B_BUTTON)) {
            if (time2 >= 20) {
                setPoint -= 50;
                time2 = 0;
            } else if (time2 >= 100) {
            
            } else {
                time2++;
            }
        }
        if(Objects.operatorJoy.getRawButton(ControllerMapGamepad.Y_BUTTON)) {
            // Objects.outtake.fire3(setPoint);
            Objects.outtake.fire2(setPoint);
        } else {
            Objects.outtake.stop();
        }
    }

    public void testDriveTrain() {
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.R_BUMPER)) {
            Motors.leftBack.set(-Objects.operatorJoy.getRawAxis(ControllerMapGamepad.L_STICKY));
            Motors.leftFront.set(-Objects.operatorJoy.getRawAxis(ControllerMapGamepad.L_STICKY));
            Motors.rightBack.set(Objects.operatorJoy.getRawAxis(ControllerMapGamepad.R_STICKY));
            Motors.rightFront.set(Objects.operatorJoy.getRawAxis(ControllerMapGamepad.R_STICKY));  
        } else {
            Motors.leftBack.set(0);
            Motors.leftFront.set(0);
            Motors.rightBack.set(0);
            Motors.rightFront.set(0); 
        }
    }

    public void testIntake() {
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.A_BUTTON)) {
            Motors.intake.set(.1);
        } else {
            Motors.intake.set(0);
        }
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.B_BUTTON)) {
            Objects.intakeSolenoidRetract.set(Value.kReverse);
            Objects.intakeSolenoidExtend.set(Value.kForward);
        }
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.Y_BUTTON)) {
            Objects.intakeSolenoidRetract.set(Value.kForward);
            Objects.intakeSolenoidExtend.set(Value.kReverse);
        }
    }

    public void testClimb() {
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.A_BUTTON)) {
            Motors.activeClimb.set(.1);
        } else {
            Motors.activeClimb.set(0);
        }
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.B_BUTTON)) {
            Objects.climbSolenoid1.set(Value.kForward);
            // Objects.climbSolenoid2.set(Value.kReverse);
        }
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.Y_BUTTON)) {
            Objects.climbSolenoid1.set(Value.kReverse);
            // Objects.climbSolenoid2.set(Value.kForward);
        }
    }

    public void testControlPanel() {
        
    }
    
    public void testVision(){
        if (Objects.operatorJoy.getRawButton(ControllerMapGamepad.A_BUTTON)){
            Objects.visionSystems.turnLightOn();
        }
        else{
            Objects.visionSystems.turnLightOff();
        }
    }
}
