/**
 * https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:porting_guide
 */
package frc.robot.subsystems;

import java.util.ArrayList;
import  frc.robot.subsystems.pixy2api.*;
import frc.robot.subsystems.pixy2api.Pixy2;
import  frc.robot.subsystems.pixy2api.Pixy2CCC.Block;
import frc.robot.variables.Objects;

public class FixedPixyCamVision {
    public Pixy2 pixycam = Pixy2.createInstance(Pixy2.LinkType.I2C);
    boolean isCamera = false; //Camera initialization status
    int state =-1; //error state of the PixyCam initialization
    int n = 0;

    public FixedPixyCamVision() {
        // constructor
    }

    /**
     * Returns the average value of 10 PixyCam readings in order to smooth out the X value
     * @return Average value of 10 pixycam readings
     */
    public int smoothX() {
        int i;
        double value = 0;
        int numReadings = 10;

        if (getPixyX() != -1) { //if no error
            for (i = 0; i < numReadings; i++) {
                value = value + getPixyX();
            }
            value = value / numReadings;
            System.out.println(value);
            return((int)value);
        }
        else {
            return -1;
        }
    }

    /**
     * Returns the average value of 10 PixyCam readings in order to smooth out the Y value
     * @return Average value of 10 pixycam readings
     */
    public int smoothY() {
        int i;
        double value = 0;
        int numReadings = 10;

        if (getPixyX() != -1) {
            for (i = 0; i < numReadings; i++) {
                value = value + getPixyY();
            }
            value = value / numReadings;
            return((int)value);
        }
        else {
            return -1;
        }
    }

     public int getPixyX() {//gets X value fro pixycam
        if (!isCamera) { //initializes if not initialized
            state = pixycam.init();
        }

        isCamera = state >= 0;
        pixycam.getCCC().getBlocks(false); //don't wait for a ball, just give us data
        ArrayList<Block> blocks = pixycam.getCCC().getBlockCache(); //give us data in an array
        
        try { //try to get the x value, if no ball, we'll catch the error and return -1 as error
            return(blocks.get(0).getX());
        }
        catch (Exception e) {
            return(-1);
        }
        
    }
    
    public int getPixyY() { //same procedure as getPixyX, just with the y value
        if (!isCamera){
            state = pixycam.init();
        }

        isCamera = state >= 0;
        pixycam.getCCC().getBlocks(false);
        ArrayList<Block> blocks= pixycam.getCCC().getBlockCache();
        
        try {
            return(blocks.get(0).getY());
        }
        catch (Exception e) {
            return(-1);
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
