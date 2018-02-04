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
		ElevatorEncoder = new Encoder(Constants.ELEVATOR_ENCODER_POWER_CHANNEL, Constants.ELEVATOR_ENCODER_DATA_CHANNEL); 
		ElevatorEncoder.reset();
		ElevatorEncoder.setDistancePerPulse(Constants.ELEVATOR_DISTANCE_TRAVELED_PER_ROTATION/Constants.ENCODER_COUNTS_PER_ROTATION); 
		
		ElevatorMotor = new Talon(Constants.ELEVATOR_MOTOR_1); 
	}
	
	//Make a move method once we have encoders that work
	
}
