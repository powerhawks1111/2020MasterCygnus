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
    public Pixy2 pixycam = Pixy2.createInstance(Pixy2.LinkType.I2C);//initializes pixy2 as I2c port
    boolean isCamera = false; //camera isn't there, sets up if statement to initialize
    int state =-1; //changed in if statement
    int n=0;

    public FixedPixyCamVision() {
        // constructor
    }

    public int smoothX() {//SMOOTHS OUT CHAOTIC READINGS
        int i;
        double value = 0;
        int numReadings = 10; //average of 10 readings

        if (getPixyX() != -1) { //if no error
            Objects.visionSystems.turnLightOn();
            for (i = 0; i < numReadings; i++) {
                value = value + getPixyX(); //get sum of values
            }
            value = value / numReadings; //what will happen here? We could get a decimal
            System.out.println(value);
            return((int)value);
        }

        else {
            Objects.visionSystems.turnLightOff();
            return -1; //continues returning error
        }
    }

    public int smoothY() {//same thing as smoothX
        int i;
        double value=0;
        int numReadings=10; //average of 10 readings

        if (getPixyX() != -1) { //if no error
            Objects.visionSystems.turnLightOn();
            for (i = 0; i < numReadings; i++) {
                value = value + getPixyY(); //get sum of values
            }
            value = value / numReadings; //what will happen here? We could get a decimal
            return((int)value);
        }

        else {
            Objects.visionSystems.turnLightOn();
            return -1; // continues returning error
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
