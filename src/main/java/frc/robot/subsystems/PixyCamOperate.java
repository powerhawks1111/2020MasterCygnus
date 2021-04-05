package frc.robot.subsystems;

import frc.robot.variables.Objects;

public class PixyCamOperate {
    /**
     * Takes the position of the target that the PixyCam has selected, begins turning towards that target, and returns the current X position
     * @return X_Position
     */
    public int pixyLineUp() {
        Objects.pixyCamVision.updatePixyCamData();
        int X_Position = Objects.pixyCamVision.getX();
        if (X_Position < 5 && X_Position > -5) { //TODO change the margin of error
            Objects.driveTrain.tankDrive(Objects.pixyCamVision.pixyCamSpeedLeft(X_Position), Objects.pixyCamVision.pixyCamSpeedRight(X_Position)); //TODO: Might need to be negative values because intake is on the back
        }
        else {
            Objects.driveTrain.tankDrive(0, 0);
        }
        return X_Position;
    }

    /**
     * Rotates the robot until it finds a ball, then precisely rotates so the ball is in the center of the field of view.
     * @return
     */
    public boolean searchForBalls() {
        int minWidth = 50; //TODO: Change this to the correct minimum number of pixels wide the nearest ball should be
        int marginOfError = 15; //TODO: Change this to the correct +/- margin of error that the ball can be off of center
        int currXPosition;
        boolean hasLinedUp = false;
        if (Objects.pixyCamVision.getWidth() < minWidth) { //if there are no valid targets, continue rotating
            System.out.println("Turning and searching for balls.");
            Objects.driveTrain.arcadeDrive(0, 0.1);
        }
        else if (Objects.pixyCamVision.getWidth() > minWidth) {
            System.out.println("Ball found. Lining up precisely.");
            currXPosition = pixyLineUp();
            if (currXPosition < marginOfError && currXPosition > -(marginOfError)) {
                hasLinedUp = true;
            }
        }
        if (hasLinedUp == true) {
            System.out.println("Robot is lined up with the ball.");
            Objects.driveTrain.arcadeDrive(0, 0);
        }
        else {
            System.out.println("Robot is still lining up precisely.");
        }
        return hasLinedUp;
    }

    /**
     * Full sequence to look for and pick up one ball
     * @return ballPickedUp - whether or not the robot has made an attempt at intaking a ball
     */
    public boolean driveToBall() {
        int maxWidth = 200; //TODO: Change this to the correct maximum number of pixels wide a ball will be when it is in front of the intake
        int minY = 100; //TODO: Change this to the correct minimum height that the ball will be before the intake should be activated
        boolean ballPickedUp = false;
        double drivingSpeed = 0.1; //TODO: Perentage of power that the robot should drive towards the ball with, might need to be negative because intake is on the "back"
        if (searchForBalls() == true && Objects.pixyCamVision.getWidth() < maxWidth) {
            System.out.println("Driving towards ball.");
            Objects.driveTrain.arcadeDrive(drivingSpeed, 0);
            pixyLineUp();
        }
        else if (Objects.pixyCamVision.getWidth() >= maxWidth || Objects.pixyCamVision.getY() <= minY) {
            Objects.intake.intake(0);
            Objects.index.intakeIndex();
            Objects.driveTrain.moveDistance(30, 0.05, true);
            ballPickedUp = true;
        }
        return ballPickedUp;
    }

    /**
     * Searches for balls, locks on to the target that the pixycam chooses, and then drives to each ball to pick it up
     * @param numBallsToFind
     * <ul><li>How many balls to search for and pick up</li></ul>
     */
    public void fullPixyCamSequence(int numBallsToFind) {
        if (Objects.magicNumbers.numBallsPickedUp <= numBallsToFind && !Objects.magicNumbers.successfulCompletion) {
            if (driveToBall() == true) {
                Objects.magicNumbers.numBallsPickedUp++;
            }
        }
        else {
            Objects.magicNumbers.numBallsPickedUp = 0;
            Objects.magicNumbers.successfulCompletion = true;
        }
    }
}
