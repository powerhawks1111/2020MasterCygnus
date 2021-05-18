package frc.robot.subsystems;

import frc.robot.variables.Objects;
import java.lang.Math;

public class PixyCamOperate {
    public boolean lineup = false;
    public boolean intakeDown = false;
    public boolean waiting = false;
    public int ballsRetrieved = 0;
    public boolean gotBall = true;
    public boolean waitingIntake = false;
    /**
     * Rotates the robot in place to track the ball
     * <br><br>
     * Rotation equation: https://www.desmos.com/calculator/zgfwpnir32
     * @return X_Position from the pixyCam target
     */
    public int pixyLineUp() {
        int X_Position = Objects.pixyCamVision.getPixyX(0);
        int center = 166;
        double scaledPower = 0.3;
        double equationPower = 13 / 9; // power for the calculated speed equation
        double calculatedSpeed = scaledPower * (1 / Math.pow(center, equationPower)) * (Math.pow((X_Position - center), equationPower));

        if (X_Position != -1) {
            Objects.driveTrain.tankDrive(-calculatedSpeed, calculatedSpeed);
        }

        else {
            Objects.driveTrain.tankDrive(0, 0);
        }
        return X_Position;
    }

    /**
     * Rotates and drives to the ball in one smooth motion
     * <br><br>
     * Rotation equation: https://www.desmos.com/calculator/zgfwpnir32
     * Drive equation:    https://www.desmos.com/calculator/29julrgx6u
     * @return True if the ball both meets X and Y constraints
     */
    public boolean pixyFetch() { //you should only use arcade drive 2, as the two add to above 1
        int X_Position = Objects.pixyCamVision.getPixyX(0);
        int Y_Position = Objects.pixyCamVision.getPixyY(0);
        int center = 166;
        int maxY = 165; //increased to get robot to approach ball when intake down
        double scaledPowerRotate = .4;
        double scaledPowerDrive = .4;
        double equationPower_Rotate = 13 / 9;
        double equationPower_Drive = 13/ 9;
        double rotationSpeed = scaledPowerRotate * (1 / Math.pow(center, equationPower_Rotate)) * (Math.pow((X_Position - center), equationPower_Rotate));
        double driveSpeed = scaledPowerDrive * (1 / Math.pow(maxY, equationPower_Drive)) * (Math.pow((maxY - Y_Position), equationPower_Drive));
        if (X_Position != -1) {
            Objects.driveTrain.arcadeDrive(driveSpeed, rotationSpeed);
            return (pixyStatus());
        }
        else {
            Objects.driveTrain.tankDrive(0, 0);
            return (false);
        }
        
        
    }
    /**
     * Function checks to see if ball 
     * @return Boolean that is true if ball is close enough to attempt to be indexed
     */
    public Boolean pixyStatus() {
        int X_Position = Objects.pixyCamVision.getPixyX(0);
        int Y_Position = Objects.pixyCamVision.getPixyY(0);
        boolean xIsGood = (X_Position >= (166 - 6) && X_Position <= (166 + 6));
        boolean yIsGood = (Y_Position >= (130) && Y_Position <= (145));
        return xIsGood && yIsGood;
    }
    public void pickUpBall() {
        if (!intakeDown) {
            if (pixyFetch()) {
                intakeDown = true;
                Objects.intake.intake(0); //intake ball
            }
            else {
                Objects.intake.intake(-1); //retract intake
            }
        }
        else if (intakeDown) {
            Objects.driveTrain.arcadeDrive(0.05, 0);
            if (Objects.index.getInd0()) {
                intakeDown = false;
                Objects.driveTrain.arcadeDrive(0, 0);
            }
        }
        else {
            Objects.intake.intake(-1); //retract intake
        }
    }

    /**
     * Evaluates if the ball is going into the intake and into the mecanum wheels
     * @return Boolean that is true if the ball is in the mecanum wheels
     */
    public Boolean checkForIntake() {
        int X_Position = Objects.pixyCamVision.getPixyX(0);
        int Y_Position = Objects.pixyCamVision.getPixyY(0);
        boolean xIsGood = (X_Position >= (166 - 10) && X_Position <= (166 + 10));
        boolean yIsGood = (Y_Position >= (125) && Y_Position <= (185)); //the point at which the ball is DEFINITELY intaked.
        return xIsGood && yIsGood;
    }

    /**
     * Full pixycam sequence to retrieve ball 
     *      <li>Logic (not in order of if statments):</li>
     *      <ul>
     *           <li>If the intake isn't down, keep doing pixyFetch</li>
     *           <li>If the intake is down but the ball is not in the intake keep doing pixyFetch</li>
     *           <li>Check if the ball is close to being intaked with the y value, and set a bool to true</li>
     *           <li>If the ball is in the mecanum wheels, wait until it's indexed, then move on to the next ball</li>
     *      </ul>
     */
    public void pickUpBallWithIntake() {
        if (intakeDown) { //if the intake is down
            Objects.intake.intake(0); 
            if (waiting) { //if we know the ball's about it be indexed, the ball is surely in the intake
                if (waitingIntake && !Objects.index.getInd0()){
                    waitingIntake = false;
                    waiting = false;
                    intakeDown = false;
                    Objects.intake.intake(-1); 
                }
                else if (Objects.index.getInd0()) {
                    waitingIntake = true;
                    Objects.driveTrain.stop();
                }
                else {
                    Objects.driveTrain.arcadeDrive2(.15, 0);
                }
            } 
            else if (checkForIntake()) { //if the ball is still out there check if its about to go into the intake
                waiting = true; 
            }
            else { //still manuever towards ball if the ball isn't about to be intaked and we're not waiting
                pixyFetch();
            } 
        } 
        else if (pixyFetch()) { //do pixyfetch until in range
            intakeDown = true;
            gotBall = false;
            Objects.intake.intake(0);
        } else { //the intake isn't down and the ball isn't in range  
        }
    }
public void pickUpBall2() {
        if (waiting) { //if we know the ball's about it be indexed, the ball is surely in the intake
            if (waitingIntake && !Objects.index.getInd0()){
                waitingIntake = false;
                waiting = false;
                intakeDown = false;
                Objects.intake.intake(-1); 
            }
            else if (Objects.index.getInd0()) {
                waitingIntake = true;
                Objects.driveTrain.stop();
            }
            else {
                Objects.driveTrain.arcadeDrive2(.1, 0);
            }
        } 
        else if (checkForIntake()) { //if the ball is still out there check if its about to go into the intake
            waiting = true; 
        }
        else { //still manuever towards ball if the ball isn't about to be intaked and we're not waiting
            pixyFetch();
        } 
    }
}