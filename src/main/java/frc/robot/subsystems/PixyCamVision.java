/**
 * https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:porting_guide
 */
package frc.robot.subsystems;

import java.util.ArrayList;

import frc.robot.subsystems.pixy2api.Pixy2;
import frc.robot.subsystems.pixy2api.Pixy2CCC.Block;
import frc.robot.variables.Objects;

public class PixyCamVision {
    public Pixy2 pixycam = Pixy2.createInstance(Pixy2.LinkType.I2C);
    boolean isCamera = false; // Camera initialization status
    int state = -1; // error state of the PixyCam initialization
    int n = 0;
    int index = -1;

    public PixyCamVision() {
        // constructor
    }
    /**
     * Method used to get the index of a certain object without errors
     * @param Position array position you want to find
     * @return index of position
     */
    public int getIndex (int Position) {
        if (!isCamera) { // initializes if not initialized
            state = pixycam.init();
            isCamera = state >= 0;
        }
        pixycam.getCCC().getBlocks(false);
        ArrayList<Block> blocks = pixycam.getCCC().getBlockCache();

        try {
            return (blocks.get(Position).getIndex());
        } catch (Exception e) {
            return (-1); // ArrayList is empty, and there are no valid targets
        }
    }
    /**
     * Returns the x position of a tracked ball (it tracks the index). If the ball
     * is lost, it tracks the next biggest ball
     * 
     * @return X position of indexed (tracked ball)
     */
    public int trackBall() {
        pixycam.getCCC().getBlocks(false);
        ArrayList<Block> blocks = pixycam.getCCC().getBlockCache();
        Boolean found = false;
        if (index != getIndex(0)) { // if index isn't the same as first block in list
            for (int i = 0; i < blocks.size(); i++) { // find the index
                if (index == getIndex(i)) {
                    found = true;
                    return (getPixyX(i));
                }
            }
            if (!found) { // find the next biggest object
                index = getIndex(0);
                return (getPixyX(0));
            }
        } 
        else { // returns already tracked object
            return (getPixyX(0));
        }
        System.out.println("Something went wrong tracking the index");
        return (getPixyX(0)); // idk why but VS code made me put this here

    }

    /**
     * Turns on/off camera LEDs to show ball detected, as well as shows smoothed out
     * x value for testing
     */
    public void statusUpdate() {
        if (getPixyX(0) != -1) {
            Objects.visionSystems.turnLightOn();
        } else {
            Objects.visionSystems.turnLightOff();
        }
    }

    /**
     * Returns the first instance of a pixyCam target that is roughly a square, like a ball.
     * This should help filter out any objects that are yellow but not a ball.
     * @return integer i, the index of the first square pixyCam target, or -1 if there are no targets.
     */
    public int firstValidIndex() {
        if (!isCamera) { // initializes if not initialized
            state = pixycam.init();
            isCamera = state >= 0;
        }
        pixycam.getCCC().getBlocks(false);
        ArrayList<Block> blocks = pixycam.getCCC().getBlockCache();
        try {
            for (int i = 0; i < blocks.size(); i++) {
                int X_Position = blocks.get(i).getX();
                int Y_Position = blocks.get(i).getY();
                boolean isASquare = (Y_Position >= X_Position - 5) || (Y_Position <= X_Position + 5);
                if (isASquare) {
                    return i;
                }
            }
        }

        catch(Exception e) {
            return -1;
        }

        return -1;
    }

    /**
     * Uses the Pixy2 API to request and return the X-position of the first PixyCam
     * target. The first object in the array has the largest area.
     * @param Position - the index of the object in the array you want to get X for
     * @return X-Position (in pixels from the left of the image) of the first Pixy
     *         target (-1 if there are no targets to track)
     */
    public int getPixyX(int Position) {
        if (!isCamera) { // initializes if not initialized
            state = pixycam.init();
            isCamera = state >= 0;
        }
        pixycam.getCCC().getBlocks(false);
        ArrayList<Block> blocks = pixycam.getCCC().getBlockCache();

        try {
            return (blocks.get(Position).getX());
        } catch (Exception e) {
            return (-1); // ArrayList is empty, and there are no valid targets
        }

    }

    /**
     * Uses the Pixy2 API to request and return the Y-position of the first PixyCam
     * target. The largest object in the array has the largest area.
     * @param Position - the index of the object in the array you want to get Y for
     * @return Y-Position (in pixels from the top of the image) of the first Pixy
     *         target (-1 if there are no targets to track)
     */
    public int getPixyY(int Position) {
        if (!isCamera) {
            state = pixycam.init();
            isCamera = state >= 0;
        }

        pixycam.getCCC().getBlocks(false);
        ArrayList<Block> blocks = pixycam.getCCC().getBlockCache();

        try {
            return (blocks.get(Position).getY());
        } catch (Exception e) {
            return (-1); // ArrayList is empty, and there are no valid targets
        } 

    }

    /**
     * Takes in the x-position of the target the PixyCam has selected and calculates
     * the speed that the left set of motors should run in order to capture the ball
     * 
     * @param x
     * @return calculatedSpeedPercentage
     */
    public double pixyCamSpeedLeft(int x) {
        double calculatedSpeedPercentage = (0.0125 * (double) x) - 1;
        if (calculatedSpeedPercentage > 1) {
            calculatedSpeedPercentage = 1;
        }
        return calculatedSpeedPercentage;
    }

    /**
     * Takes in the x-position of the target the PixyCam has selected and calculates
     * the speed that the right set of motors should run in order to capture the
     * ball
     * 
     * @param x
     * @return calculatedSpeedPercentage
     */
    public double pixyCamSpeedRight(int x) {
        double calculatedSpeedPercentage = (-0.0125 * (double) x) + 3;
        if (calculatedSpeedPercentage < -1) {
            calculatedSpeedPercentage = -1;
        }
        return calculatedSpeedPercentage;
    }

}
