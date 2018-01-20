package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

/**
 * This class is for the information associated with the 
 * elevator.
 * 
 * @author NicoleEssner
 *
 */
public class Elevator 
{
	/**
	 * The encoder that is attached to the elevator mechanism.  
	 */
	private Encoder ElevatorEncoder;

	/**
	 * The speed controller that is for the elevator motor.  
	 */
	private Talon ElevatorMotor;
	
	/**
	 * Constructor for the elevator object.
	 */
	public Elevator()
	{
		ElevatorEncoder = new Encoder(4, 5); // make constant
		ElevatorEncoder.reset();
		ElevatorEncoder.setDistancePerPulse(9.0/360); // make constant
		
		ElevatorMotor = new Talon(3); // make constant
	}
	
	//Make a move method once we have encoders that work
	
}
