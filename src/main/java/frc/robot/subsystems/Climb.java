package frc.robot.subsystems;

import frc.robot.variables.Motors;
import frc.robot.variables.Objects;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Climb {
    
	public Climb() {
        
    }

    public void retractPiston() {
        Objects.climbSolenoid1.set(Value.kReverse);
        // Objects.climbSolenoid2.set(Value.kForward);
	}

    public void extendPiston() {
        Objects.climbSolenoid1.set(Value.kForward);
        // Objects.climbSolenoid2.set(Value.kReverse);
    }

    public void leftActiveClimb(double power) {
        Motors.activeClimb.set(-power);
    }

    public void rightActiveClimb(double power) {
        Motors.activeClimb.set(power);
    }

    public void leftSlowActiveClimb() {
        Motors.activeClimb.set(-0.3);
    }

    public void rightSlowActiveClimb() {
        Motors.activeClimb.set(0.3);
    }

    public void stop() {
        Motors.activeClimb.set(0);
    }
}
