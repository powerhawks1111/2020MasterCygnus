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

    /**
     * Calculates the spesd to drive the motors using a quadratic equation and an input joystick value
     * @param value
     * <ul><li>Value of the joystick between -1 and 1, inclusive</li></ul>
     * @return
     * <ul><li>Motor speed percentage between -1 and 1, inclusive</li></ul>
     */
    public double quadraticDrive(double value) { 
        double slow = 0.999999999999994 * (value * value);
        if(value < 0){
            slow = -slow;
        }
        return slow;
    }

    /**
     * Drives each side of the robot independently, and requires values for both the left and right motors
     * @param left
     * <ul><li>Left side motor percentage, from -1 to 1 inclusive</li></ul>
     * @param right
     * <ul><li>Right side motor percentage, from -1 to 1 inclusive</li></ul>
     */
    public void tankDrive(double left, double right) {
        double nvar = 1;
        Motors.leftFront.set(-nvar * left);
        Motors.leftBack.set(-nvar * left);
        Motors.rightFront.set(nvar * right);
        Motors.rightBack.set(nvar * right);
    }

    /**
     * Drives the left and right sides together, and calculates the speed for each motor given the forward speed and the rotational speed
     * @param throttle
     * <ul><li>Percentage to drive forward, from -1 to 1 inclusive</li></ul>
     * @param turnVal
     * <ul><li>Perventage to rotate, from -1 to 1 inclusive</li></ul>
     */
    public void arcadeDrive(double throttle, double turnVal) {
        double nvar = 1;
        double reduceTurn = 1;
        Motors.leftFront.set(-nvar * (throttle - turnVal * reduceTurn));
        Motors.leftBack.set(-nvar * (throttle - turnVal * reduceTurn));
        Motors.rightFront.set(nvar * (throttle + turnVal * reduceTurn));
        Motors.rightBack.set(nvar * (throttle + turnVal * reduceTurn));
    }

    /**
     * TODO: test this function!
     * <br><br>
     * Nathan's fixed drive function, not tested yet.
     * <br><br>
     * I just read through it and it doesn't seem to take into account that the front 
     * and back motor of each side need to turn in opposite directions (-Rex)
     * @param throttle
     * <ul><li>Percentage to drive forward, from -1 to 1 inclusive</li></ul>
     * @param turnVal
     * <ul><li>Perventage to rotate, from -1 to 1 inclusive</li></ul>
     */
    public void arcadeDriveFixed(final double throttle, final double turnVal) {
        double nvar = 1;
        double reduceTurn = 1;
        double left = (-nvar * (throttle - turnVal * reduceTurn));
        double right = (nvar * (throttle + turnVal * reduceTurn));
        Boolean leftIsNeg = false;
        Boolean rightIsNeg = false;
        //caps values and maintains ratio of values for similar turn
        if (left>1||right>1||left<-1||right<-1){
            //I kept getting errors with using the absolute value function so I won't use it, I'll multiply by -1
            //stores pos/neg value of variable, and switches any negatives to positives for comparisons
            if (left<0){
                leftIsNeg = true;
                left=left*-1;
            }
            if (right<0) {
                rightIsNeg = true;
                right=right*-1;
            } 
            if(left>right){
                //basically 1, the maximum
                left=left/left;
                right=right/left;
            } else if (right>left){
                right=right/right;
                left=left/right;
            }
            //returns variables to their original neg values, if applicable
            if (leftIsNeg==true){
                left=left*-1;
            }
            if (rightIsNeg==true){
                right=right*-1;
            }
        }
        Motors.leftFront.set(left);
        Motors.leftBack.set(left);
        Motors.rightFront.set(right);
        Motors.rightBack.set(right);
    }

    /**
     * Drives the robot forwards or backwards
     * @param power
     * <ul><li>Percentage to drive the robot forwards or backwards, from -1 to 1 inclusive</li></ul>
     */
    public void powerDrive(double power) {
        Motors.leftFront.set(-power);
        Motors.leftBack.set(-power);
        Motors.rightFront.set(power);
        Motors.rightBack.set(power);
    }

    /**
     * Moves the robot a specified distance with a speed/power and a forward/reverse option
     * @param targetDistance
     * <ul><li>The distance, in TODO: what unit?</li></ul>
     * @param power
     * <ul><li>Percentage of speed to drive, from -1 to 1 inclusive</li></ul>
     * @param direction
     * <ul><li>Forwards (true) or backwards (false)</li></ul>
     */
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
    
    /**
     * Rotates the robot to a given angle
     * @param targetAngle
     * <ul><li>What angle to rotate, in degrees</li></ul>
     * @param power
     * <ul><li>Percentage of power to use when rotating, from -1 to 1 inclusive</li></ul>
     * @param direction
     * <ul><li>Whether to rotate cw (false?) or ccw (true?) TODO: determine rotation direction</li></ul>
     */
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

    /**
     * Rotates the robot to a new angle, with some math to make it smoother
     * @param targetAngle
     * <ul><li>What angle to rotate, in degrees</li></ul>
     * @param power
     * <ul><li>Percentage of power to use when rotating, from -1 to 1 inclusive</li></ul>
     * @param direction
     * <ul><li>Whether to rotate cw (false?) or ccw (true?) TODO: determine rotation direction</li></ul>
     */
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

    /**
     * Math function for the turnToNew function
     * @param curAngle
     * @param targetAngle
     * @param power
     * @return
     */
    private double getPowerShift(double curAngle, double targetAngle, double power)
    {
        if(deltaAngle != 0)
        {
            double res = Math.abs(power / (curAngle - (targetAngle)/deltaAngle));
            return Math.min(Math.max(res, MIN_TURNPOW), MAX_TURNPOW);
        }
        return 0;
    }

    /**
     * Is the robot driving?
     * @return isDriving
     * <ul><li>Boolean values of whether or not the robot is currently driving</li></ul>
     */
    public boolean isDriving() {
        return isDriving;
    }

    /**
     * Is the robot turning?
     * @return isTurning
     * <ul><li>Boolean values of whether or not the robot is currently turning</li></ul>
     */
    public boolean isTurning() {
        return isTurning;
    }

    /**
     * Stops the robot's drive motors
     */
    public void stop() {
        powerDrive(0);
        isStart = true;
    }
}