package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import frc.robot.variables.Objects;

public class VisionSystems {

    public VisionSystems() {
        
    }

    public void turnLightOn(){
        Objects.relay.set(Relay.Value.kOn);
    }

    public void turnLightOff(){
        Objects.relay.set(Relay.Value.kOff);
    }
}
