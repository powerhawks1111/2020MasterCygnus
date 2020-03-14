package frc.robot.commands;

import frc.robot.commands.Command;
import frc.robot.variables.MagicNumbers;
import frc.robot.variables.Objects;

public class TurnCommand implements Command {

    public double targetAngle;
    public double power;
    public boolean direction;
    public boolean complete;

    public TurnCommand(double t, double p, boolean d) {
        targetAngle = t;
        power = p;
        direction = d;
    }

    public void execute() { 
        Objects.driveTrain.turnTo(targetAngle, power, direction);
        Objects.intake.intake(MagicNumbers.intake);
        Objects.index.backgroundIndex();
        complete = !Objects.driveTrain.isTurning();
    }

    public void stop() {
        Objects.driveTrain.stop();
        Objects.navx.zeroYaw();
    }

    public boolean isComplete() {
        return !Objects.driveTrain.isTurning();
    }
}
