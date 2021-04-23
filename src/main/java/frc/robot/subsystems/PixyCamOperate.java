package frc.robot.subsystems;

import frc.robot.variables.Objects;

public class PixyCamOperate {
    Boolean gotBall = false;
    Boolean broken = false; // determines if ball goes outside range

    public void fullBallFind() {
        if (!gotBall) {
            getBall();
        }
    }

    /**
     * Takes the position of the target that the PixyCam has selected, begins
     * turning towards that target, and returns the current X position
     * 
     * @return X_Position
     */
    public int pixyLineUp() {
        Objects.pixyCamVision.updatePixyCamData();
        int X_Position = Objects.pixyCamVision.getX();
        int center = 168; //center of image is 168 pixels from the left side of the frame
        if (X_Position < (center - 20) || X_Position > (center + 20)) {
            Objects.driveTrain.tankDrive(Objects.pixyCamVision.pixyCamSpeedLeft(X_Position), Objects.pixyCamVision.pixyCamSpeedRight(X_Position));
        }
        else {
            Objects.driveTrain.tankDrive(0, 0);
        }
        return X_Position;
    }

    public void getBall() { // GETS THE BALL
        String command = lineUp(); // determines status of robot
        String yCommand = closeY();
        if (command != "Obey Driver" && yCommand != "No Ball Found") { // If robot doesn't have to obey driver and ball is found
            if (command == "Straight" && yCommand == "Able") { // do if close to intake and straight ahead
                broken = false; //ball is still in range
                System.out.println("GET IT!");
                Objects.driveTrain.arcadeDrive(.1, 0); //move forward
                while (!Objects.index.ind0.get()) { //while ball isn't in index
                    if (closeY() == "Unable" || lineUp() != "Straight") { // if ball moves out of position
                        Objects.intake.intake(-1);
                        broken = true;
                        break;
                    }
                    Objects.intake.intake(0); //activate intake
                }
                if (broken == false) { //if we got the ball
                    gotBall = true;
                }
            //repeats until it finds the ball in the right position again
            }
            else if (command == "Right" && yCommand == "Able") { // just orient right if ball is close and to the right
                Objects.driveTrain.tankDrive(.1, -.1);
                System.out.println("ORIENTING RIGHT");
            }
            else if (command == "Left" && yCommand == "Able") {// opposite of else if above
                Objects.driveTrain.tankDrive(-.1, .1);
                System.out.println("ORIENTING LEFT");
            }
            else if (lineUp() == "Straight") { // do if not close to intake but straight ahead
                Objects.driveTrain.arcadeDrive(.15, 0);
                System.out.println("STRAIGHT!");
            }
            else if (lineUp() == "Right") { // do if ball to the right
                Objects.driveTrain.arcadeDrive(.15, .15);
                System.out.println("RIGHT!");
            }
            else if (lineUp() == "Left") { // do if ball is to the left
                Objects.driveTrain.arcadeDrive(.15, -.15);
                System.out.println("LEFT!");
            }
        }
        
    else {
            System.out.println("No Ball Found");
        }
    }

    public String lineUp() { // determines status of ball relative to robot
        int x = Objects.fixedPixyCamVision.smoothX();
        // center of intake 170
        if (x != -1) { // if no error
            if (x > 190) { // if to right, deadzone 20 on each side of robot
                return ("Right");
            } else if (x < 150) {
                return ("Left"); // if to the left
            } else {
                return ("Straight"); // If in deadzone, ball is straight ahead
            }
        } else {
            return ("Obey Driver");
        }
    }

    public String closeY() {
        int y = Objects.fixedPixyCamVision.smoothY(); //
        if (y != -1) { // If no error
            if (y > 100) { // If close the intake (Value may change)
                return ("Able");

            } else {
                return ("Unable");
            }
        } else { // In event of error, so no ball found
            return ("No Ball Found");
        }

    }

    /**
     * Rotates the robot until it finds a ball, then precisely rotates so the ball
     * is in the center of the field of view.
     * 
     * @return
     */
    public boolean searchForBalls() {
        int marginOfError = 15; // TODO: Change this to the correct +/- margin of error that the ball can be off
                                // of center
        int currXPosition;
        boolean hasLinedUp = false;
        Objects.pixyCamVision.updatePixyCamData();
        if (Objects.pixyCamVision.getWidth() < minWidth) { // if there are no valid targets, continue rotating
            System.out.println("Turning and searching for balls.");
            Objects.driveTrain.arcadeDrive(0, 0.1);
        } else if (Objects.pixyCamVision.getWidth() > minWidth) {
            System.out.println("Ball found. Lining up precisely.");
            currXPosition = pixyLineUp();
            if (currXPosition < marginOfError && currXPosition > -(marginOfError)) {
                hasLinedUp = true;
            }
        }
        if (hasLinedUp == true) {
            System.out.println("Robot is lined up with the ball.");
            Objects.driveTrain.arcadeDrive(0, 0);
        } else {
            System.out.println("Robot is still lining up precisely.");
        }
        return hasLinedUp;
    }

    /**
     * Full sequence to look for and pick up one ball
     * 
     * If the robot is lined up with the ball, the robot drives forward at 10%
     * power. When it gets close enough to the ball, it switches to intaking and
     * moving a specific distance.
     * 
     * @return ballPickedUp - whether or not the robot has made an attempt at intaking a ball
     */
    public boolean driveToBall() {
        int minY = 160;
        boolean ballPickedUp = false;
        double drivingSpeed = 0.1;
        Objects.pixyCamVision.updatePixyCamData();

        if (searchForBalls() == true && Objects.pixyCamVision.getY() > minY) {
            System.out.println("Driving towards ball.");
            Objects.driveTrain.arcadeDrive(drivingSpeed, 0);
            pixyLineUp();
        }
        else {
            Objects.intake.intake(0);
            Objects.index.intakeIndex();
            Objects.driveTrain.moveDistance(30, 0.1, true); //TODO: Determine drive distance
            ballPickedUp = true;
        }
        return ballPickedUp;
    }

    /**
     * Searches for balls, locks on to the target that the pixycam chooses, and then
     * drives to each ball to pick it up
     * 
     * @param numBallsToFind
     * <ul>
     * <li>How many balls to search for and pick up</li>
     * </ul>
     */
    public void fullPixyCamSequence(int numBallsToFind) {
        if (Objects.magicNumbers.numBallsPickedUp <= numBallsToFind && !Objects.magicNumbers.successfulCompletion) {
            if (driveToBall() == true) {
                Objects.magicNumbers.numBallsPickedUp++;
            }
        } else {
            Objects.magicNumbers.numBallsPickedUp = 0;
            Objects.magicNumbers.successfulCompletion = true;
        }
    }
}
