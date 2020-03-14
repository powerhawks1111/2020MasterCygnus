package frc.robot.variables;

import frc.robot.subsystems.*;
import frc.robot.Robot;
import frc.robot.commands.Scheduler;
import com.revrobotics.CANEncoder;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import com.kauailabs.navx.frc.AHRS;

public class Objects {
    public static CANEncoder encoder = new CANEncoder(Motors.leftBack);
    public static Climb climb = new Climb();
    public static Compressor compressor = new Compressor();
    public static DoubleSolenoid climbSolenoid1= new DoubleSolenoid(0, 7);
    public static DoubleSolenoid climbSolenoid2= new DoubleSolenoid(1, 6);
    public static DoubleSolenoid intakeSolenoidExtend= new DoubleSolenoid(3, 4);
    public static DoubleSolenoid intakeSolenoidRetract= new DoubleSolenoid(2, 5);
    public static DriveTrain driveTrain = new DriveTrain();
    public static Index index = new Index(Robot.pcStartCount.getSelected());
    public static Intake intake = new Intake();
    public static Joystick driverJoy = new Joystick(ControllerMap.DRIVER_PORT);
    public static Joystick operatorJoy = new Joystick(ControllerMap.OPERATOR_PORT);
    public static Outtake outtake = new Outtake();
    public static Relay relay = new Relay(0, Direction.kReverse);
    public static Scheduler scheduler = new Scheduler();
    public static Vision vision = new Vision();
    public static VisionSystems visionSystems = new VisionSystems();
    public static AHRS navx = new AHRS();
}

