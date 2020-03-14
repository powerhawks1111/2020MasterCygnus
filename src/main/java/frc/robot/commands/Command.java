package frc.robot.commands;

public interface Command {

    public double target = -1;

    public boolean complete = false;

    public void execute(); 

    public void stop();

    public boolean isComplete(); 
}
