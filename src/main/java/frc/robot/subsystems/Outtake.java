package frc.robot.subsystems;

import frc.robot.variables.ControllerMapGamepad;
import frc.robot.variables.Motors;
import frc.robot.variables.Objects;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANEncoder;

public class Outtake {

    public CANPIDController pidController;
    private final CANEncoder encoder;
    public double kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;
    public boolean isShooting;
    public boolean prepareFire = false;
    public static boolean isRev;
    public double kP = 0.00005; 
    public double kI = 0.0000005;
    public double kD = 0.00000000001;
    public boolean charging = false; // when true, background index advances balls to shooter
    public boolean overShot = false; // when true, background index purges index through shooter

    /**
     * Constructor
     */
    public Outtake() {
        isRev = false;
        pidController = Motors.outLeader.getPIDController();
        encoder = Motors.outLeader.getEncoder();
        // kP = .0004; //oscilation at 0.0015 
        // kI = 0.000001;
        // kD = 10;
        // kFF = 0.0000; 

        kP = 0.0001; 
        kI = 0.0000005;
        kD = 0.00001;
        kFF = 0.0000; 
        kMaxOutput = 1; 
        kMinOutput = -1;
        maxRPM = 5700;
        maxVel = 2000;
        maxAcc = 1500;

        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
    }

    /**
     * Revs the motor to 4000 rpm using the pidController velocity control type
     */
    public void revUp() { 
        pidController.setReference(-4000, ControlType.kVelocity);
        Motors.outFollower.follow(Motors.outLeader, true);
        SmartDashboard.putNumber("Velocity", encoder.getVelocity());
    }

    public void moveUp(final double percent) {
        Motors.moveUp.set(-percent);
        Motors.indexLead.set(-percent);
        Motors.indexFollower.follow(Motors.indexLead);
    }

    public void fire(final double percentPower) {
        System.out.println(Motors.outLeader.getEncoder().getVelocity());
        Motors.outLeader.setVoltage(percentPower);
        Motors.outFollower.follow(Motors.outLeader, true);
    }

    public void fire2(final double setPoint) {
        pidController.setReference(-setPoint, ControlType.kVelocity);
        Motors.outFollower.follow(Motors.outLeader, true);
        double currentMotorVelocity = encoder.getVelocity();
        SmartDashboard.putNumber("Velocity", -currentMotorVelocity);
        SmartDashboard.putNumber("Set point", setPoint);
        SmartDashboard.putNumber("Error", (setPoint) - (-encoder.getVelocity()));

        if ((currentMotorVelocity > (-setPoint) - 70) && (currentMotorVelocity < (-setPoint) + 70)) {
            charging = false;
            isShooting = true;
        }
        else {
            charging = true;
            isShooting = false;
        }
    }

    public void fire3(final double setPoint) {
        pidController.setReference(-setPoint, ControlType.kVelocity);
        Motors.outFollower.follow(Motors.outLeader, true);
        if (encoder.getVelocity() > (Math.abs(setPoint)) - 500) {
            pidController.setFF(1/setPoint);
        } else {
            pidController.setFF(0);
        }
        if ((encoder.getVelocity() > (Math.abs(setPoint)) - 70) 
             && (encoder.getVelocity() < (Math.abs(setPoint)) + 70)) {
            moveUp(-1);
            isShooting = true;
        } else {
            moveUp(0);
        }
    }
    
    public void fire4(final double setPoint) {
        charging = true;
        int rpmWindow = 80;
        pidController.setReference(-setPoint, ControlType.kVelocity);
        Motors.outFollower.follow(Motors.outLeader, true);
        SmartDashboard.putNumber("Velocity", -encoder.getVelocity());
        SmartDashboard.putNumber("Set point", setPoint);
        SmartDashboard.putNumber("Error", (setPoint) - (-encoder.getVelocity()));
        if ((encoder.getVelocity() > (-setPoint) - rpmWindow) && (encoder.getVelocity() < (-setPoint) + rpmWindow)) {
            charging = false;
            isShooting = true;
    
            SmartDashboard.putBoolean("In Range?", true);
        } else {
            charging = true;
            isShooting = false;
            SmartDashboard.putBoolean("In Range?", false);
        } 
    }


    public void fire5(final double setPoint) {
        charging = true;
        int overShoot = 400;

        double currentMotorVelocity = encoder.getVelocity();

        SmartDashboard.putNumber("Velocity", -currentMotorVelocity);
        SmartDashboard.putNumber("Set point", setPoint);
        SmartDashboard.putNumber("Error", (setPoint) - (-currentMotorVelocity));

        if ((currentMotorVelocity < (-setPoint + overShoot-100))) {
            pidController.setReference(-setPoint + overShoot,ControlType.kVelocity);
            Motors.outFollower.follow(Motors.outLeader, true);
        }
        else {
            pidController.setReference(-setPoint, ControlType.kVelocity);
            Motors.outFollower.follow(Motors.outLeader, true);
        }

        if ((currentMotorVelocity > (-setPoint) - 80) && (currentMotorVelocity < (-setPoint) + 80)) {
            charging = false;
            isShooting = true;
        }
        else {
            charging = true;
            isShooting = false;
        }
    }

    public void fire6(final double setpoint) {
        
    }

    /**
     * Calculates the velocity the motor needs to fire the ball a certain distance
     * @param distance
     * @return
     */
    public double autoVelocity(double distance) {
        double d = distance;
        double velocity;
        if (d < 14) {
            velocity = (12.5 * (d*d)) - (d*187.5) + 4325;
        } else {
            velocity = (4.02 * (d * d)) - (90.97*d) + 4675.26;
        }
        if (velocity > 5400) {
            velocity = 5400;
        } else if(velocity < 3500) {
            velocity = 3500;
        }
        return velocity;
    }
    
    public double heightToDistance(double height) {
        return ((height * height) * 0.0066) - (1.175 * height) + 60.31;
    }

    public void farShoot() {
        double targetHeight = Objects.vision.getFittedHeight();
        double distance = heightToDistance(targetHeight);
        double rpmToShoot = autoVelocity(distance);
        SmartDashboard.putNumber("Auto velocity ", rpmToShoot);
        fire4(rpmToShoot);
    }

    public void middleShoot() { //trench
        fire5(4500);
    }

    public void closeShoot() { //white line
        fire2(4500); 
    }
    
    public void rpmShoot(int velocity) {
        fire2(velocity);
    }
    public void firePercent(final double percent) {
        Motors.outLeader.set(percent);
        Motors.outFollower.follow(Motors.outLeader, true);
    }

    public void reset() {
        Motors.outLeader.restoreFactoryDefaults();
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIZone(kIz);
    }

    public void stop() {
        Motors.outLeader.stopMotor();
        Motors.outFollower.stopMotor();
        reset();
    }

    public boolean isShooting() {
        return isShooting;
    }

    public boolean isCharging() {
        return charging;
    }

    public void fireSequenceTest(int setPoint) {
        if (Objects.driverJoy.getRawButton(ControllerMapGamepad.X_BUTTON)) {
            fire3(setPoint);
        }
    }
    
    public double getRPM() {
        return SmartDashboard.getNumber("Outtake RPM", 0.0);
    }
}

