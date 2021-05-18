package frc.robot.subsystems;

import frc.robot.variables.Motors;
import frc.robot.variables.Objects;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Climb {
    
    public Climb() {
        
    }

    /**
     * Retracts the lift pistons
     */
    public void retractPiston() {
        Objects.climbSolenoid1.set(Value.kReverse);
        Objects.climbSolenoid2.set(Value.kReverse);
    }

    /**
     * Extends the lift pistons
     */
    public void extendPiston() {
        Objects.climbSolenoid1.set(Value.kForward);
        Objects.climbSolenoid2.set(Value.kForward);
    }

    /**
     * Moves the robot left at a specified power with the climb system
     * @param power
     * <ul><li>The percent of power to move left with</li></ul>
     */
    public void leftActiveClimb(double power) {
        Motors.activeClimb.set(-power);
    }

    /**
     * Moves the robot right at a specified power with the climb system
     * @param power
     * <ul><li>The percent of power to move right with</li></ul>
     */
    public void rightActiveClimb(double power) {
        Motors.activeClimb.set(power);
    }
    
    /**
     * Moves the robot left with a preset 1/3 power
     */
    public void leftSlowActiveClimb() {
        Motors.activeClimb.set(-0.3);
    }

    /**
     * Moves the robot right with a preset 1/3 power
     */
    public void rightSlowActiveClimb() {
        Motors.activeClimb.set(0.3);
    }

    /**
     * Stops moving the climb motor
     */
    public void stop() {
        Motors.activeClimb.set(0);
    }
}
