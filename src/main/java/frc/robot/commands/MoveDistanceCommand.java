package frc.robot.commands;

//import frc.robot.commands.Command;
import frc.robot.variables.MagicNumbers;
import frc.robot.variables.Objects;

public class MoveDistanceCommand implements Command {

    public double targetDistance;
    public boolean direction;
    public double power;
    public boolean complete;

    /**
     * Autonomous command constructor to move the robot forward a certain distance TODO: units!
     * @param t
     * <ul><li>targetDistance (TODO: units!)</li></ul>
     * @param p
     * <ul><li>power (-1 to 1, inclusive)</li></ul>
     * @param d
     * <ul><li>direction (true = forward)</li></ul>
     */
    public MoveDistanceCommand(double t, double p, boolean d) {
        targetDistance = t;
        power = p;
        direction = !d;
    }

    /**
     * Executes the autonomous move distance command
     * Called by Scheduler.run during autonomous
     */
    public void execute() {
        Objects.driveTrain.moveDistance(targetDistance, power, direction);
        Objects.intake.intake(MagicNumbers.intake);
        Objects.index.backgroundIndex();
        complete = !Objects.driveTrain.isDriving();
    }

    /**
     * Ensures the robot has stopped after the command is complete
     * Called by Scheduler.run during autonomous
     */
    public void stop() {
        Objects.driveTrain.stop();
    }

    /**
     * Called by Scheduler.run during autonomous to determine
     * whether to move to the next command
     */
    public boolean isComplete() {
        return complete;
    }
}
