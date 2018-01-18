package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.Encoder;

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
	 * Constructor for the elevator object.
	 */
	public Elevator()
	{
		ElevatorEncoder = new Encoder(0, 1); // Will be made constant
		ElevatorEncoder.reset();
		ElevatorEncoder.setDistancePerPulse(9.0/360); // 9 inches for every one encoder rotation
	}
	
	/**
	 * Method that will use PID to get a mechanism to a set value by
	 * adjusting the value given to the motor controlling the mechanism.
	 * This method should be called in a loop.  
	 * 
	 * @param target
	 * 		The target value for the PID to get the mechanism to. 
	 * 
	 * @param encoderVal
	 * 		The current value of the encoder that is attached to the 
	 * 		mechanism. 
	 * 
	 * @param pVal
	 * 		The p value that will be used in the PID to adjust the 
	 * 		mechanism. 
	 * 
	 * @return
	 * 		The value that should be feed into the motor to get to the 
	 * 		target value. 
	 * 
	 */
	public double PID(double target, double encoderVal, double pVal)
	{
		double difference = encoderVal - target;
		double motorPower = difference * pVal;
		return motorPower;
	}
	
}
