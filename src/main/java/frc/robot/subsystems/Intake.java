package frc.robot.subsystems;

import frc.robot.variables.Motors;
import frc.robot.variables.Objects;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.variables.MagicNumbers;

public class Intake {

    int counter;
    double speed;

    public Intake() {
        counter = 0;
    }

    /**
     * Operates the intake
     * @param mode
     * <ul>
     * <li>-1 = stop [retract intake bar and turn off motor]</li>
     * <li>0 = extend intake bar and pull ball in</li>
     * <li>1 = extend intake bar and push ball out</li>
     */
    public void intake(int mode) {
        if (Objects.index.getInd0()) {
            if (counter > MagicNumbers.intakePause) {
                speed = 0.35;
            } else {
                speed = MagicNumbers.intakePower;
                counter++;
            }
        } else {
            counter = 0;
            speed = MagicNumbers.intakePower;
        }
        if (mode == MagicNumbers.defaultIntake) {
            stop();
        }
        if (mode == MagicNumbers.intake) {
            Motors.intake.set(speed);
            Objects.intakeSolenoidExtend.set(Value.kForward);
            Objects.intakeSolenoidRetract.set(Value.kReverse);
        }
        if (mode == MagicNumbers.purgeIntake) {
            Motors.intake.set(-MagicNumbers.intakePower);
            Objects.intakeSolenoidExtend.set(Value.kForward);
            Objects.intakeSolenoidRetract.set(Value.kReverse);
        }
    }

    public void stop() {
        Motors.intake.set(0);
        Objects.intakeSolenoidExtend.set(Value.kReverse);
        Objects.intakeSolenoidRetract.set(Value.kForward);
    }
}