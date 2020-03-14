package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.variables.MagicNumbers;
import frc.robot.variables.Motors;
import frc.robot.variables.Objects;

public class Index {

    private int numCells;
    private static boolean flushIndex = false;
    private int timer = 0;
    private boolean end = false;
    boolean startOfIndex = false;
    boolean stopIntake = true;
    boolean stopOuttake = true;

    DigitalInput ind0 = new DigitalInput(1);
    DigitalInput ind5 = new DigitalInput(2);

    public Index(final int numCells) {
        this.numCells = numCells;
    }

    public void countCells(int change) { 
        numCells += change;
    }

    public void setFlush(boolean flushIndex) {
        Index.flushIndex = flushIndex;
    }

    public int getNumCells() {
        return numCells;
    }

    public boolean getInd5() {
        return ind5.get();
    }

    public boolean getInd0() {
        return ind0.get();
    }

    public void moveCells(double speed) {
        Motors.indexLead.set(speed);
        Motors.indexFollower.follow(Motors.indexLead, false);
        Motors.moveUp.set(speed);
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void backgroundIndex() {
        if (!flushIndex) {
            intakeIndex();
            outtakeIndex();
            stopIndex();
        }
    }

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

    public void stopIndex() {
        if (stopIntake && stopOuttake) {
            moveCells(0.0);
        }
    }

    public void indexOverride(double percent) {
        moveCells(0.5 *percent);
    }
    
	public void manualOuttake() {
        setFlush(true);
        moveCells(0.5);
    }

	public void stop() {
        moveCells(0.0);
	}
}