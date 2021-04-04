package frc.robot.commands;

//import frc.robot.commands.Command;
import frc.robot.variables.MagicNumbers;
import frc.robot.variables.Objects;

public class TurnCommand implements Command {

    public double targetAngle;
    public double power;
    public boolean direction;
    public boolean complete;

    /**
     * Autonomous command constructor to rotate the robot a specific number of degrees
     * @param t
     * <ul><li>targetAngle (degrees)</li></ul>
     * @param p
     * <ul><li>power (-1 to 1, inclusive)</li></ul>
     * @param d
     * <ul><li>direction (true = forward)</li></ul>
     */
    public TurnCommand(double t, double p, boolean d) {
        targetAngle = t;
        power = p;
        direction = d;
    }

    /**
     * Executes the autonomous turn command
     * Called by Scheduler.run during autonomous
     */
    public void execute() { 
        Objects.driveTrain.turnTo(targetAngle, power, direction);
        Objects.intake.intake(MagicNumbers.intake);
        Objects.index.backgroundIndex();
        complete = !Objects.driveTrain.isTurning();
    }

    /**
     * Ensures the robot has stopped after the command is complete
     * Called by Scheduler.run during autonomous
     */
    public void stop() {
        Objects.driveTrain.stop();
        Objects.navx.zeroYaw();
    }

    /**
     * Called by Scheduler.run during autonomous to determine
     * whether to move to the next command
     */
    public boolean isComplete() {
        return !Objects.driveTrain.isTurning();
    }
}
