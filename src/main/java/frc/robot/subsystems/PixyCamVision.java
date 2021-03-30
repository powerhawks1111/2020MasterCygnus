/**
 * https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:porting_guide
 */
package frc.robot.subsystems;



import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
//import edu.wpi.first.wpilibj.IterativeRobot;

//import vars.Motors;

//import edu.wpi.first.wpilibj.SerialPort;

//import com.ctre.phoenix.motorcontrol.ControlMode;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


//import edu.wpi.first.wpilibj.SerialPort;

public class PixyCamVision {

	I2C PixyCamI2C = new I2C(Port.kOnboard, 0x69);
	
	int temp;
	int i;
	private int checkSum;
	private int sig;
	private int x;
	private int y;
	private int width;
	private int height;
	
	public PixyCamVision() {
		//constructor
	}

	/**
	 * TODO: Rewrite this so it is all one line and not multiple nested IF statements
	 * It works right now so I won't touch it till I can test it, but it needs to get fixed
	 * Date: 3/30/2021
	 * -Rex McAllister
	 */
	private void updateData() {
			if(readByte() == 85) { //85 = 01010101 = 55
				if(readByte() == -86) { //-86 = 10101010 = AA
					if(readByte() == 85) { //The I2C stream will send these expected test bits to make sure the conenction is good
						if(readByte() == -86) {
							checkSum = readShort();
							sig = readShort();
							x = readShort();
							y = readShort();
							width = readShort();
							height = readShort();

						}
					}	
				}	
			}else {
				x = 0;
			}
		}

	private byte readByte() {
		ByteBuffer buffer = ByteBuffer.allocateDirect(1);
		PixyCamI2C.readOnly(buffer, 1);
		byte myByte = buffer.get();
		return myByte;
	}
	private short readShort() {
		ByteBuffer buffer = ByteBuffer.allocateDirect(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		PixyCamI2C.readOnly(buffer, 2);
		short myShort = buffer.getShort();
		return myShort;
	}

	public int getCheckSum() {
		System.out.println("Checksum: " + checkSum);
		return checkSum;
	}
	public int getSig() {
		System.out.println("sig: " + sig);
		return sig;
	}
	public int getX(){
		System.out.println("x: " + x);
		return x;
	}
	public int getWidth(){
		System.out.println("width " + width);
		return width;
	}
	public int getY(){
		System.out.println("Y " + y);
		return y;
	}
	public int getHeight(){
		System.out.println("height " + height);
		return height;
	}


	public void updatePixyCamData() {
		try {
			updateData();
			System.out.println("PixyCam X-Val: " + x);
			
		} catch (Exception e) {
			System.out.println("PixyCam had an error! Line 111 of PixyCamVision.java!");
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

	//spins in place so target is in center
	//TODO: Not sure if this works, 3/30/2021
    public double spinUp(){
		double speed = 0;
		updateData();
        int x = getX();
        //double speed = -0.4;
        if(x > 0) {
            // matrix.defaultMatrix = false;
            // matrix.fillColor(0, 255, 0);
            double value = (-0.00625*x)+1;
		   // dt.tankDrive(value * -speed, value * speed);
		   speed = value;
        }
        return speed;
    }
}
