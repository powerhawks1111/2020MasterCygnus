package frc.robot.commands;

import frc.robot.commands.Command;
import frc.robot.variables.MagicNumbers;
import frc.robot.variables.Objects;

public class MoveDistanceCommand implements Command {

    public double targetDistance;
    public boolean direction;
    public double power;
    public boolean complete;

    public MoveDistanceCommand(double t, double p, boolean d) {
        targetDistance = t;
        power = p;
        direction = !d;
    }

    public void execute() {
        Objects.driveTrain.moveDistance(targetDistance, power, direction);
        Objects.intake.intake(MagicNumbers.intake);
        Objects.index.backgroundIndex();
        complete = !Objects.driveTrain.isDriving();
    }

    public void stop() {
        Objects.driveTrain.stop();
    }

    public boolean isComplete() {
        return complete;
    }
}
