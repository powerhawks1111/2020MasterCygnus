package frc.robot.subsystems;

import frc.robot.variables.Objects;

public class deprecatedPixyCamOperate {
    Boolean gotBall = false;
    Boolean broken = false; // determines if ball goes outside range
    /**
     * Tries to get the ball until the ball is detected in the intake.
     */
    public void fullBallFind() {
            Objects.driveTrain.tankDrive(.1,.1);
    }



    /**
     * The entire find loop to get the ball. Gets position in the x direction, and
     * proximity to intake, and acts accordingly.
     * <li>LOGIC:</li>
     * <ul>
     * <li>If close to the intake and off center, the robot pivots around its
     * vertical axis.</li>
     * <li>If ball is close and straight ahead, the robot operates the intake until
     * the index detects the ball or the ball moves out of the correct position</li>
     * <li>If the ball is to the right/left, the robot uses arcade drive to drive
     * right/left.</li>
     * <li>If straight ahead but not in range, the robot drives forward.</li>
     */
    public void getBall() { // GETS THE BALL
        String command = lineUp(); // determines status of robot
        String yCommand = closeY();
        if (command != "Obey Driver" && yCommand != "No Ball Found") { // If robot doesn't have to obey driver and ball
                                                                       // is found
            if (command == "Straight" && yCommand == "Able") { // do if close to intake and straight ahead
                broken = false; // ball is still in range
                System.out.println("GET IT!");
                Objects.driveTrain.arcadeDrive(.1, 0); // move forward
                if (!Objects.index.ind0.get()) { // if ball isn't in index
                    if (closeY() == "Unable" || lineUp() != "Straight") { // if ball moves out of position
                        //Objects.intake.intake(-1);
                        System.out.println("Out of Range");
                    }
                    System.out.println("Intake Down");
                    //Objects.intake.intake(0); // activate intake
                }
                else {
                    gotBall = true;
                    //Objects.intake.intake(-1);
                }
                // repeats until it finds the ball in the right position again
            } else if (command == "Right" && yCommand == "Able") { // just orient right if ball is close and to the
                                                                   // right
                Objects.driveTrain.tankDrive(-.1, .1);
                System.out.println("ORIENTING RIGHT");
            } else if (command == "Left" && yCommand == "Able") {// opposite of else if above
                Objects.driveTrain.tankDrive(.1, -.1);
                System.out.println("ORIENTING LEFT");
            } else if (command == "Straight") { // do if not close to intake but straight ahead
                Objects.driveTrain.arcadeDrive(.1, 0);
                System.out.println("STRAIGHT!");
            } else if (command == "Right") { // do if ball to the right
                Objects.driveTrain.arcadeDrive(.1, .1);
                System.out.println("RIGHT!");
            } else if (command == "Left") { // do if ball is to the left
                Objects.driveTrain.arcadeDrive(.1, -.1);
                System.out.println("LEFT!");
            }
        }

        else {
            System.out.println("No Ball Found");
        }
    }

    /**
     * Determines the relative position of the ball to the robot. Uses only the x
     * value, and has a deadzone 40 pixels wide in the center
     * 
     * @return string of the position of the ball: Right, Left, Straight, or Obey
     *         Driver (no ball)
     */
    public String lineUp() { // determines status of ball relative to robot
        int x = Objects.pixyCamVision.getPixyX(0);
        // center of intake 170
        if (x != -1) { // if no error
            if (x > 186) { // if to right, deadzone 20 on each side of robot
                return ("Right");
            } else if (x < 146) {
                return ("Left"); // if to the left
            } else {
                return ("Straight"); // If in deadzone, ball is straight ahead
            }
        } else {
            return ("Obey Driver");
        }
    }

    //public void setMotorsArcade(test, turnVal ) {
       //// Double lastThrottle = test;
//  if (throttle != lastThrottle || turnVal != lastTurnVal) {
        //    Objects.driveTrain.arcadeDrive(throttle, turnVal);
            
       // }
       // else {
       //     Objects.driveTrain.arcadeDrive(lastThrottle, turnVal);
       // }
   // }//
    /**
     * Takes into account y value of ball and determines if it is close enough to
     * intake. The logic is when the ball is below a certain part of the frame.
     * 
     * @return String statement of the ability of the robot: Able, Unable, No Ball
     *         Found
     */
    public String closeY() {
        int y = Objects.pixyCamVision.getPixyY(0); //
        if (y != -1) { // If no error
            if (y > 115) { // If close the intake (Value may change)
                return ("Able");

            } else {
                return ("Unable");
            }
        } else { // In event of error, so no ball found
            return ("No Ball Found");
        }
    }
}
