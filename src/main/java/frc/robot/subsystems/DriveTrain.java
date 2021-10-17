package frc.robot.subsystems;

import frc.robot.variables.Motors;
import frc.robot.variables.Objects;
import frc.robot.variables.MagicNumbers;
import java.lang.Math;

import com.revrobotics.ControlType;
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
     * Drives the robot with a maximum motor acceleration
     * @param left - Speed to drive the motors from -1 to 1, inclusive
     * @param maxAcc - Maximum acceleration, in RPM/S
     */
    public void trapezoidTankDrive(double left, double right, int maxAcc) {
        Motors.rightFrontPID.setSmartMotionMaxAccel(maxAcc, 0);
        Motors.leftFrontPID.setSmartMotionMaxAccel(maxAcc, 0);
        Motors.rightBackPID.setSmartMotionMaxAccel(maxAcc, 0);
        Motors.leftBackPID.setSmartMotionMaxAccel(maxAcc, 0);

        Motors.rightFrontPID.setReference(right, ControlType.kSmartVelocity);
        Motors.leftFrontPID.setReference(-left, ControlType.kSmartVelocity);
        Motors.rightBackPID.setReference(right, ControlType.kSmartVelocity);
        Motors.leftBackPID.setReference(-left, ControlType.kSmartVelocity);
    }

    /**
     * Calculates the speed to drive the motors using a cubic (just x^3) equation and an input joystick value
     * @param value
     * <ul><li>Value of the joystick between -1 and 1, inclusive</li></ul>
     * @return cubicFn
     * <ul><li>Motor speed percentage between -1 and 1, inclusive</li></ul>
     */
    public double cubicDriveCalculator(double value) {
        //not actually cubic!
        //Usually .8 but tested at .4
        double cubicFn = Math.pow(value, 3);
        return cubicFn;
    }

    /**
     * Calculates the speed to drive the motors using a cubic (just x^3) equation and an input joystick value
     * @param value
     * <ul><li>Value of the joystick between -1 and 1, inclusive</li></ul>
     * @return cubicFn
     * <ul><li>Motor speed percentage between -1 and 1, inclusive</li></ul>
     */
    public double cubicDriveCalculatorRotate(double value) {
        //not actually cubic!
        //Usually .8 but tested at .4
        double cubicFn = Math.pow(value, 5/3);
        return cubicFn;
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
     * Drives the left and right sides together, and calculates the speed for each motor given the forward speed and the yaw rotational speed
     * @param throttle
     * <ul><li>Percentage to drive forward, from -1 to 1 inclusive</li></ul>
     * @param turnVal
     * <ul><li>Perventage to rotate, from -1 to 1 inclusive</li></ul>
     */
    public void arcadeDrive(double throttle, double turnVal) {
        //+ dir = outtake forward, - dir = intake forward
        double nvar = 0.8;
        int dir = 1;
        Motors.leftFront.set(-nvar * dir * (throttle - dir * turnVal));
        Motors.leftBack.follow(Motors.leftFront, false);
        Motors.rightFront.set(nvar * dir * (throttle + dir * turnVal));
        Motors.rightBack.follow(Motors.rightFront, false);
    }

    /**
     * Drives the left and right sides together
     * without having to worry about going over 100% power as the result
     * @param throttle
     * <ul><li>Percentage to drive forward, from -1 to 1 inclusive</li></ul>
     * @param turnVal
     * <ul><li>Perventage to rotate, from -1 to 1 inclusive</li></ul>
     */
    public void arcadeDrive2(double throttle, double turnVal) {
        double scale = 0.8;
        double speed = 0.7 * throttle;
        double rotate = 0.3 * turnVal;

        Motors.leftFront.set(-scale * (speed - rotate));
        Motors.leftBack.follow(Motors.leftFront, false);
        Motors.rightFront.set(scale * (speed + rotate));
        Motors.rightBack.follow(Motors.rightFront, false);
    }

    /**
     * Applies a diamond throttle map to the stick inputs to eliminate control deadzones or overruns
     * @param throttle
     * <ul><li>Throttle stick input, from -1 to 1 inclusive</li></ul>
     * @param turnVal
     * <ul><li>Turn stick input, from -1 to 1 inclusive</li></ul>
     * @param reverse
     * <ul><li>When true, switches the "forward" direction</li></ul>
     */
    public void diamondDrive(double throttle, double turnVal, boolean reverse) {
        // turn is x, throttle is y

        turnVal *= 0.3;
        double direction = reverse ? -1 : 1;
        double scaledPower = 1;
        //convert to polar
        double originalRadius = Math.hypot(turnVal, throttle);
        double originalAngle = Math.atan2(throttle, turnVal);

        // rotate by 45 degrees
        originalAngle += Math.PI / 4;

        // back to cartesian
        double left = originalRadius * Math.cos(originalAngle);
        double right = originalRadius * Math.sin(originalAngle);

        // rescale coords
        left *= Math.sqrt(2);
        right *= Math.sqrt(2);

        // clamp to -1/+1
        left = Math.max(-1, Math.min(left, 1));
        right = Math.max(-1, Math.min(right, 1));

        Motors.leftFront.set(direction * scaledPower * left);
        Motors.leftBack.follow(Motors.leftFront, false);
        Motors.rightFront.set(direction * scaledPower * right);
        Motors.rightBack.follow(Motors.rightFront, false);
    }

        /**
     * Applies a diamond throttle map to the stick inputs to eliminate control deadzones or overruns
     * @param throttle
     * <ul><li>Throttle stick input, from -1 to 1 inclusive</li></ul>
     * @param turnVal
     * <ul><li>Turn stick input, from -1 to 1 inclusive</li></ul>
     * @param reverse
     * <ul><li>When true, switches the "forward" direction</li></ul>
     */
    public void diamondDriveRateLimiter(double throttle, double turnVal, boolean reverse) {
        // turn is x, throttle is y
        double direction = reverse ? -1 : 1;
        double scaledPower = 1;
        double maximumAcceleration = 5200;
        double maxRPM = 5200;
        //convert to polar
        double originalRadius = Math.hypot(turnVal, throttle);
        double originalAngle = Math.atan2(throttle, turnVal);

        // rotate by 45 degrees
        originalAngle += Math.PI / 4;

        // back to cartesian
        double left = originalRadius * Math.cos(originalAngle);
        double right = originalRadius * Math.sin(originalAngle);

        // rescale coords
        left *= Math.sqrt(2);
        right *= Math.sqrt(2);

        // clamp to -1/+1
        left = Math.max(-1, Math.min(left, 1));
        right = Math.max(-1, Math.min(right, 1));

        Motors.leftFrontPID.setSmartMotionMaxAccel(maximumAcceleration, 0);
        Motors.rightFrontPID.setSmartMotionMaxAccel(maximumAcceleration, 0);

        Motors.leftFrontPID.setReference(direction * scaledPower * left * maxRPM, ControlType.kSmartVelocity, 0);
        Motors.leftBack.follow(Motors.leftFront, false);
        Motors.rightFrontPID.setReference(direction * scaledPower * right * maxRPM, ControlType.kSmartVelocity, 0);
        Motors.rightBack.follow(Motors.rightFront, false);
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
     * Moves the robot a specified distance with a speed/power and a forward/reverse option (Doesn't currently work with teleoperated)
     * @param targetDistance
     * <ul><li>The distance, in inches</li></ul>
     * @param power
     * <ul><li>Percentage of speed to drive, from -1 to 1 inclusive</li></ul>
     * @param direction
     * <ul><li>Forwards (true) or backwards (false)</li></ul>
     */
    public void moveDistance(double targetDistance, double power, boolean direction) {
        if (isStart) {
            startingTicks = Motors.rightBack.getEncoder().getPosition();
            targetTicks = targetDistance * MagicNumbers.TPI;
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

    public void powerDriveIndex(double power) {
        Motors.indexLead.set(-power);
        Motors.indexFollower.follow(Motors.indexLead);
    }

    /**
     * Moves the index a specified distance with a speed/power and a forward/reverse option (Not tested, doesn't work, not needed)
     * @param targetDistance
     * <ul><li>The distance, in Undetermined (Probably Inches)</li></ul>
     * @param power
     * <ul><li>Percentage of speed to move the index, from -1 to 1 inclusive</li></ul>
     * @param direction
     * <ul><li>Forwards (true) or backwards (false)</li></ul>
     */
    public void moveDistanceIndex(double targetDistance, double power, boolean direction) {
        if (isStart) {
            startingTicks = Motors.indexLead.getEncoder().getPosition();
            targetTicks = targetDistance * MagicNumbers.TPI;
            isStart = false;
        }
        currentTicks = Motors.indexLead.getEncoder().getPosition();
        if (direction && currentTicks < (targetTicks + startingTicks)) {
            powerDriveIndex(power);
            isIndexing = true;
        } else if (!direction && currentTicks >  (startingTicks - targetTicks)) {
            powerDriveIndex(-power);
            isIndexing = true;
        } else {
            isIndexing = false;
        }
    }
    
    /**
     * Rotates the robot to a given angle (currently doesn't work with teleoperated)
     * @param targetAngle
     * <ul><li>What angle to rotate, in degrees</li></ul>
     * @param power
     * <ul><li>Percentage of power to use when rotating, from -1 to 1 inclusive</li></ul>
     * @param direction
     * <ul><li>Whether to rotate cw (true) or ccw (false) </li></ul>
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
     * Rotates the robot to a new angle, with some math to make it smoother (Doesn't work with teleoperated currently)
     * @param targetAngle
     * <ul><li>What angle to rotate, in degrees</li></ul>
     * @param power
     * <ul><li>Percentage of power to use when rotating, from -1 to 1 inclusive</li></ul>
     * @param direction
     * <ul><li>Whether to rotate cw (true) or ccw (false) </li></ul>
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