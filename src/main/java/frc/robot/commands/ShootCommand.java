package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
//import frc.robot.commands.Command;
import frc.robot.variables.MagicNumbers;
import frc.robot.variables.Objects;

public class ShootCommand implements Command {

    int velocity; //flywheel velocity
    Timer timer = new Timer();
    boolean lineup; //TODO: what is this?

    /**
     * Default autonomous shoot command constructor that accepts a given flywheel rpm
     * @param v
     * <ul><li>Velocity (rpm)</li></ul>
     * @param l
     * <ul><li>Lineup value (normally true)</li></ul>
     */
    public ShootCommand(int v, boolean l) {
        velocity = v;
        lineup = l;
        if (!lineup) {
            timer.start();
        }
        Objects.outtake.isShooting = false;
    }

    /**
     * Overloaded sutonomous shoot command
     * <br><br>
     * Automatically calculates the flywheel velocity based on the width of the target
     * @param l
     * <ul><li>Lineup value (normally true)</li></ul>
     */
    public ShootCommand(boolean l) {
        velocity = -1; //uses the vision autoShoot function to calculate the correct motor speed based on the height of the target
        lineup = l;
        if (!lineup) {
            timer.start();
        }
        Objects.outtake.isShooting = false;
    }

    /**
     * Executes the autonomous shoot command
     * Called by Scheduler.run during autonomous
     */
    public void execute() {
        Objects.intake.intake(MagicNumbers.defaultIntake); //stop the intake
        Objects.visionSystems.turnLightOn();
        // if (lineup && (Objects.vision.getYaw() > 1 || Objects.vision.getYaw() < -1)) {
            // Objects.driveTrain.tankDrive(-Objects.vision.spinUp(), Objects.vision.spinUp());
        // } 
        // else {
            if (velocity == -1) {
                Objects.outtake.fire4(Objects.vision.autoShoot(Objects.vision.getFittedHeight()));
            } else {
                Objects.outtake.fire4(velocity);
            }
        // }
    }

    /**
     * Ensures the robot has stopped after the command is complete
     * Called by Scheduler.run during autonomous
     */
    public void stop() {
        Objects.outtake.firePercent(0);
    }

    /**
     * Called by Scheduler.run during autonomous to determine
     * whether to move to the next command
     */
    public boolean isComplete() {
        boolean result = false;
        if (!Objects.outtake.isShooting) {
            timer.start();
        }
        if (timer.get() > 6) {
            result = true;
            timer.stop();
            Objects.outtake.stop();
            Objects.index.moveCells(0);
            Objects.visionSystems.turnLightOff();
        }
        return result;
    }
}
