package frc.robot;

import java.util.ArrayList;
import frc.robot.commands.*;
import frc.robot.subsystems.DriveTrain;
import frc.robot.variables.Objects;

public class Autonomous {

    private ArrayList<Command> commandsList;
    boolean start = false;
    
    public Autonomous() {
        commandsList = new ArrayList<Command>();
    }

    public void moveDistanceTest() {
        if (!start) {
            Objects.visionSystems.turnLightOn();
            commandsList.add(new MoveDistanceCommand(20, .2, true));
            start = true;
        }
        startAutononous();
    }

    /**
     * starts facing target directly. Shoots, goes to trench, picks up balls, shoots from trench
     */
    public void redPath() {
        if (!start) {
            Objects.visionSystems.turnLightOn();
            // commandsList.add(new ShootCommand(3700, true));  
            // commandsList.add(new TurnCommand(90, 0.2, false)); 
            // commandsList.add(new TurnCommand(90, 0.2, true));    
            commandsList.add(new TurnCommand(29.14, 0.15, false));    

            // commandsList.add(new MoveDistanceCommand(60, 0.1, false));
            // commandsList.add(new TurnCommand(52.03187, 0.2, true));    
            // commandsList.add(new MoveDistanceCommand(192.63, 0.34, false));
            // commandsList.add(new MoveDistanceCommand(108, 0.34, true));
            // commandsList.add(new TurnCommand(52.03187, 0.2, false)); 
            // commandsList.add(new ShootCommand(3900, true));
            start = true;
        }
        startAutononous();
    }

    /**
     * starts with back directed towards the trench. Picks up balls in trench, shoots from trench
     */
    public void orangePath() {
        if (!start) {
            Objects.visionSystems.turnLightOn();
            commandsList.add(new TurnCommand(29.14, 0.15, false));    
            // commandsList.add(new ShootCommand(3820, true));  //find value
            // commandsList.add(new TurnCommand(29.14, 0.15, true)); 
            // // commandsList.add(new TurnCommand(29.14, 0.2, true));       
            // // commandsList.add(new MoveDistanceCommand(86.63, 0.34, false));
            // // commandsList.add(new MoveDistanceCommand(108, 0.34, false));
            // // commandsList.add(new MoveDistanceCommand(108, 0.34, true));
            // commandsList.add(new MoveDistanceCommand(24, 0.34, false));
            // commandsList.add(new MoveDistanceCommand(24, 0.34, true));

            // commandsList.add(new TurnCommand(29.14, 0.15, false)); 
            // commandsList.add(new ShootCommand(4350, true)); 
            start = true;
        }
        startAutononous();

    }

    /**
     * starts facing target directly. Picks up 2 balls from rendezvous, shoots
     */
    public void yellowPath() {
        if (!start) {
            // Objects.visionSystems.turnLightOn();
            // commandsList.add(new ShootCommand(3350, true));  
            // commandsList.add(new MoveDistanceCommand(122.63, 0.2, false));
            // commandsList.add(new TurnCommand(33.1956, 0.1, true)); 
            // commandsList.add(new MoveDistanceCommand(21.63, 0.2, false));
            // commandsList.add(new MoveDistanceCommand(21.63, 0.2, true));
            // commandsList.add(new TurnCommand(33.1956, 0.1, false)); 
            // commandsList.add(new ShootCommand(true)); //find value
            // start = true;
            Objects.visionSystems.turnLightOn();
            commandsList.add(new ShootCommand(3820, true)); //find value
            // commandsList.add(new MoveDistanceCommand(122.63, 0.2, false));
            commandsList.add(new TurnCommand(29.143, 0.2, false)); 
            commandsList.add(new MoveDistanceCommand(122.63, 0.34, false));
            commandsList.add(new TurnCommand(33.1956, 0.2, true)); 
            commandsList.add(new TurnCommand(33.1956, 0.2, false));
            commandsList.add(new MoveDistanceCommand(122.63, 0.34, true)); 
            commandsList.add(new TurnCommand(29.143, 0.2, true)); 
            commandsList.add(new ShootCommand(4350, true)); //find value
            start = true;
        }
        startAutononous();
    }

    /**
     * Starts facing opponents dropping station. picks up balls from opponents trench and then shoots
     */
    public void greenPath() {
        if (!start) {
            Objects.visionSystems.turnLightOn();
            commandsList.add(new TurnCommand(7.373, 0.2, true)); 
            commandsList.add(new MoveDistanceCommand(251.058, 0.2, false)); 
            commandsList.add(new MoveDistanceCommand(10, 0.2, false)); 
            commandsList.add(new TurnCommand(18.303, 0.2, true)); 
            commandsList.add(new MoveDistanceCommand(287.264, 0.2, true));
            commandsList.add(new ShootCommand(3850, true)); //find value
            start = true;
        }
        startAutononous();
    }
    /**
     * shoots starting in middle directed at target. moves off line
     */
    public void bluePath() {
        if (!start) {
            Objects.index.moveCells(.6);
            // Objects.visionSystems.turnLightOn();
            commandsList.add(new ShootCommand(3900, false)); 
            commandsList.add(new MoveDistanceCommand(48, 0.2, false)); 
            start = true;
        }
        startAutononous();
    }

    /**
     * shoots starting in on side of target. moves off line
     */
    public void indigoPath() {
        if (!start) {
            Objects.visionSystems.turnLightOn();
            commandsList.add(new ShootCommand(true)); 
            commandsList.add(new MoveDistanceCommand(36, 0.2, true)); 
            start = true;
        }
        startAutononous();    
   
    }

    public void violetPath() {

    }

    public void defaultAutonomous() {
        if (!start) {
            Objects.visionSystems.turnLightOn();
            commandsList.add(new MoveDistanceCommand(60, 0.1, false));
            start = true;
        }
        startAutononous();
    }

    public void startAutononous() {
        Objects.scheduler.addCommands(commandsList);
        Objects.scheduler.run();
    }

    public void setStart(boolean start) {
        this.start = start;
        Objects.navx.zeroYaw();
    }
}

