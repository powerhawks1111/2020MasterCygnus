package frc.robot.variables;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class Motors {

     //Index Motors
     public static CANSparkMax indexLead = new CANSparkMax(8, CANSparkMaxLowLevel.MotorType.kBrushless);
     public static CANSparkMax indexFollower = new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless);
     public static CANSparkMax moveUp = new CANSparkMax(9, CANSparkMaxLowLevel.MotorType.kBrushless);

     //Outtake/Shooter
     public static CANSparkMax outLeader = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
     public static CANSparkMax outFollower = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
     public static CANSparkMax move1Up = new CANSparkMax(9, CANSparkMaxLowLevel.MotorType.kBrushless);
     
     //DriveTrain Motors
     public static CANSparkMax rightFront = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
     public static CANSparkMax leftFront = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
     public static CANSparkMax rightBack = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
     public static CANSparkMax leftBack = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless); 
 
     //Intake Motors
     public static CANSparkMax intake = new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless);
    
     //Active Climb Motors
     public static CANSparkMax activeClimb = new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);
}
