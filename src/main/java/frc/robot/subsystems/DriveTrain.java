package frc.robot.subsystems;

import frc.robot.variables.Motors;
import frc.robot.variables.Objects;
import frc.robot.variables.MagicNumbers;

public class DriveTrain {

    public boolean isDriving;
    public boolean isTurning;
    public boolean isStart;
    public boolean isIndexing;
    public double startingTicks; 
    public double currentTicks;
    public double targetTicks;
    public double startingAngle;
    public double currentAngle;

    public DriveTrain() {
        isDriving = false;
        isTurning = false;
        isStart = true;
        startingTicks = -1;
        currentTicks = -1;
        targetTicks = -1;
        startingAngle = -1;
        currentAngle = -1;
    }

    public double quadraticDrive(double value) { 
        double slow = 0.999999999999994 * (value * value);
        if(value < 0){
            slow = -slow;
        }
        return slow;
    }

    public void tankDrive(double left, double right) {
        double nvar = 1;
        Motors.leftFront.set(-nvar * left);
        Motors.leftBack.set(-nvar * left);
        Motors.rightFront.set(nvar * right);
        Motors.rightBack.set(nvar * right);
    }

    public void arcadeDrive(double throttle, double turnVal) {
        double nvar = 1;
        double reduceTurn = 1;
        Motors.leftFront.set(-nvar * (throttle - turnVal * reduceTurn));
        Motors.leftBack.set(-nvar * (throttle - turnVal * reduceTurn));
        Motors.rightFront.set(nvar * (throttle + turnVal * reduceTurn));
        Motors.rightBack.set(nvar * (throttle + turnVal * reduceTurn));
    }

    public void powerDrive(double power) {
        Motors.leftFront.set(-power);
        Motors.leftBack.set(-power);
        Motors.rightFront.set(power);
        Motors.rightBack.set(power);
    }

    public void moveDistance(double targetDistance, double power, boolean direction) {
        if (isStart) {
            startingTicks = Motors.rightBack.getEncoder().getPosition();
            targetTicks = targetDistance * MagicNumbers.TPC;
            isStart = false;
        }
        currentTicks = Motors.rightBack.getEncoder().getPosition();
        if (direction && currentTicks < (targetTicks + startingTicks)) {
            powerDrive(power);
            isDriving = true;
        } else if (!direction && currentTicks >  (startingTicks - targetTicks)) {
            powerDrive(-power);
            isDriving = true;
        } else {
            isDriving = false;
        }
    }

    // public void powerDriveIndex(double power) {
    //     Motors.indexLead.set(-power);
    //     Motors.indexFollower.follow(Motors.indexLead);
    // }

    // public void moveDistanceIndex(double targetDistance, double power, boolean direction) {
    //     if (isStart) {
    //         startingTicks = Motors.indexLead.getEncoder().getPosition();
    //         targetTicks = targetDistance * MagicNumbers.TPC;
    //         isStart = false;
    //     }
    //     currentTicks = Motors.indexLead.getEncoder().getPosition();
    //     if (direction && currentTicks < (targetTicks + startingTicks)) {
    //         powerDriveIndex(power);
    //         isIndexing = true;
    //     } else if (!direction && currentTicks >  (startingTicks - targetTicks)) {
    //         powerDriveIndex(-power);
    //         isIndexing = true;
    //     } else {
    //         isIndexing = false;
    //     }
    // }
    
    public void turnTo(double targetAngle, double power, boolean direction) {
        if (isStart && !(Objects.navx.getAngle() == 0)) {
            startingAngle = Objects.navx.getAngle();
            isStart = false;
        }
        currentAngle = Objects.navx.getAngle();
        if (direction && currentAngle < (startingAngle + targetAngle)) {
            tankDrive(-power, power);
            isTurning = true;
        } else if(!direction && currentAngle > (startingAngle - targetAngle)) {
            tankDrive(power, -power);
            isTurning = true;
        } else {
            isTurning = false;
        }
    }

    double MIN_TURNPOW = 0.05;
    double MAX_TURNPOW = 1;

    double deltaAngle = 1;
    public void turnToNew(double targetAngle, double power, boolean direction)
    {
        if (isStart) {
            startingAngle = Objects.navx.getAngle();
            isStart = false;
            deltaAngle = startingAngle - targetAngle;
        }
        currentAngle = Objects.navx.getAngle();

        double adjustedPow = getPowerShift(currentAngle, targetAngle, power);
        if (direction && currentAngle < (startingAngle + targetAngle)) {
            tankDrive(-adjustedPow, adjustedPow);
            isTurning = true;
        } else if(!direction && currentAngle > (startingAngle - targetAngle)) {
            tankDrive(adjustedPow, -adjustedPow);
            isTurning = true;
        } else {
            isTurning = false;
        }
    }
    // (MAX)/(|target|-|current|/delta)

    public double getPowerShift(double curAngle, double targetAngle, double power)
    {
        if(deltaAngle != 0)
        {
            double res = Math.abs(power / (curAngle - (targetAngle)/deltaAngle));
            return Math.min(Math.max(res, MIN_TURNPOW), MAX_TURNPOW);
        }
        return 0;
    }

    public boolean isDriving() {
        return isDriving;
    }

    public boolean isTurning() {
        return isTurning;
    }

    public void stop() {
        powerDrive(0);
        isStart = true;
    }
}