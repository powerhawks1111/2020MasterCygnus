package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.variables.MagicNumbers;
import frc.robot.variables.Motors;
import frc.robot.variables.Objects;

public class Index {

    private int numCells; //how many balls are in the index
    private static boolean flushIndex = false; //whether or not to fire all balls
    private int timer = 0; //counter for the 60hz loop based timer
    private boolean end = false;
    boolean startOfIndex = false;
    boolean stopIntake = true;
    boolean stopOuttake = true;

    DigitalInput ind0 = new DigitalInput(1); //Optical sensor input from the robot
    DigitalInput ind5 = new DigitalInput(2); //Optical sensor input from the robot

    /**
     * Constructor
     * @param numCells
     * <ul><li>How many balls the robot is able to carry (usually 5)</li></ul>
     */
    public Index(final int numCells) {
        this.numCells = numCells;
    }

    /**
     * Increments the number of balls known to be stored in the Index
     * @param change
     */
    public void countCells(int change) { 
        numCells += change;
    }

    /**
     * Sets whether or not the index is flushing.
     * @param flushIndex
     */
    public void setFlush(boolean flushIndex) {
        Index.flushIndex = flushIndex;
    }

    /**
     * Returns the number of balls currently stored in the index
     * @return numCells
     * <ul><li>Integer number of balls that are in the index</li></ul>
     */
    public int getNumCells() {
        return numCells;
    }

    /**
     * Returns a boolean value of whether or not a ball is in the index position 5
     * <br><br>
     * Data comes from optical sensor on the robot
     * @return Boolean value of object in front of sensor
     * <ul><li>True = object present</li></ul>
     * <ul><li>False = Slot empty</li></ul>
     */
    public boolean getInd5() {
        return ind5.get();
    }

    /**
     * Returns a boolean value of whether or not a ball is in the index position 1
     * <br><br>
     * Data comes from optical sensor on the robot
     * @return Boolean value of object in front of sensor
     * <ul><li>True = object present</li></ul>
     * <ul><li>False = Slot empty</li></ul>
     */
    public boolean getInd0() {
        return ind0.get();
    }

    /**
     * Moves the motors and belts on the index system
     * @param speed
     * <ul><li>Percent speed to move the index, from -1 to 1 inclusive</li></ul>
     */
    public void moveCells(double speed) {
        Motors.indexLead.set(speed);
        Motors.indexFollower.follow(Motors.indexLead, false);
        Motors.moveUp.set(speed);
    }

    /**
     * Sets the timer variable to any value
     * @param timer
     */
    public void setTimer(int timer) {
        this.timer = timer;
    }

    /**
     * As long as the index isn't being flushed, it will poll the intake, outtake, and stop functions
     */
    public void backgroundIndex() {
        if (!flushIndex) {
            intakeIndex();
            outtakeIndex();
            stopIndex();
        }
    }

    /**
     * Advances the balls by one position based on the 60hz loop timer
     */
    public void intakeIndex() {
        if (timer < MagicNumbers.ballSpacing) {
            if (ind0.get()) {
                stopIntake = false;
                moveCells(1);
                end = true;
            } else if (end) {
                moveCells(1);
                timer++;
            }
        } else {
            timer = 0;
            end = false;
            stopIntake = true;
        }
    }

    /**
     * Advances the balls to the outtake flywheel based on the 60hz loop timer
     * To quote my friend Cygnus, "YEET!"
     */
    public void outtakeIndex() {
        if (Objects.outtake.isShooting()) {
            stopOuttake = false;
            if (ind5.get()) {
                moveCells(1);
            } else {
                stopOuttake = true;
            }
        } else if (Objects.outtake.isCharging()) {
            stopOuttake = false;
            if (ind5.get()) {
                moveCells(0.0);
            } else {
                moveCells(1);
            }
        } else {
            stopOuttake = true;
        }
    }

    /**
     * Stops the index motors
     */
    public void stopIndex() {
        if (stopIntake && stopOuttake) {
            moveCells(0.0);
        }
    }

    /**
     * Manually moves the index forwards or backwards. Multiplied by 0.5 to slow it down.
     * @param percent
     * <ul><li>How fast to move the index, from -1 to 1 inclusive</li></ul>
     */
    public void indexOverride(double percent) {
        moveCells(0.5 *percent);
    }
    
    /**
     * Begins moving the balls into the outtake, even if the outtake flywheel isn't ready
     */
	public void manualOuttake() {
        setFlush(true);
        moveCells(0.5);
    }

    /**
     * Stops moving the balls
     */
	public void stop() {
        moveCells(0.0);
	}
}