package frc.robot.commands;

import java.util.ArrayList;
import frc.robot.commands.Command;

public class Scheduler {

	boolean done = false;
	boolean running = false;
	int i = 0;
	ArrayList<Command> commands;

	public Scheduler() {
		
	}
	
	public Scheduler(ArrayList<Command> coms) {
		commands = coms;
	}

	public void run() {
		Command com = commands.get(i);
		if (!done) {
			com.execute();
		}
		if (!com.isComplete()) {
			running = true;
		} else {
			com.stop();
			if (i < commands.size() - 1) {
				i++;
			} else {
				done = true;
				running = false;
			}
		}
	}

	public void addCommands(ArrayList<Command> coms) {
		commands = coms;
	}

	public void reset() {
		try {
			commands.clear();
			done = false;
			running = false;
			i = 0;
		}
		catch (Exception e) {
			return;
		}
	}
}