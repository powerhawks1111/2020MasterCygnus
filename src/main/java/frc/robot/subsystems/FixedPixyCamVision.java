/**
 * https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:porting_guide
 */
package frc.robot.subsystems;

//import edu.wpi.first.wpilibj.I2C;
//import edu.wpi.first.wpilibj.I2C.Port;
//import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.wpilibj.IterativeRobot;

//import vars.Motors;

//import edu.wpi.first.wpilibj.SerialPort;

//import com.ctre.phoenix.motorcontrol.ControlMode;

//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;

import java.util.ArrayList;

import  frc.robot.subsystems.pixy2api.*;
//import frc.robot.subsystems.pixy2api.Pixy2;
import  frc.robot.subsystems.pixy2api.Pixy2CCC.Block;
//import edu.wpi.first.wpilibj.SerialPort;

public class FixedPixyCamVision {
    public Pixy2 pixycam = Pixy2.createInstance(Pixy2.LinkType.I2C);//new
    boolean isCamera = false;
    int state =-1;
    //int temp;
    //int i;
    
        
    //private int checkSum; //WE KNOWWW
    //private int sig;
    //private int x;
    //private int y;
    //private int width;
    //private int height;
     //new
     public FixedPixyCamVision() {
        // constructor
    }

     public int getPixyX() {
        if (!isCamera){
            state=pixycam.init();
        }
        isCamera = state>=0;
        pixycam.getCCC().getBlocks(false);
        ArrayList<Block> blocks= pixycam.getCCC().getBlockCache();
        
        try {
        return(blocks.get(0).getX());
        }
        catch (Exception e) {
            return (-1);
        }
        
    }
    public int getPixyY() {
        if (!isCamera){
            state=pixycam.init();
        }
        isCamera = state>=0;
        pixycam.getCCC().getBlocks(false);
        ArrayList<Block> blocks= pixycam.getCCC().getBlockCache();
        
        try {
        return(blocks.get(0).getY());
        }
        catch (Exception e) {
            return (-1);
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