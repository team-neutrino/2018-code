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
	 * The speed controller that for the elevator motor.  
	 */
	private Talon ElevatorMotor;
	
	/**
	 * Constructor for the elevator object.
	 */
	public Elevator()
	{
		ElevatorEncoder = new Encoder(0, 1); // Will be made constant
		ElevatorEncoder.reset();
		ElevatorEncoder.setDistancePerPulse(9.0/360); // 9 inches for every one encoder rotation
		
		ElevatorMotor = new Talon(3);
	}
	
	public double getEncoderDistance()
	{
		return ElevatorEncoder.getDistance();
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
	public double PID(double target, double pVal)
	{
		double difference = ElevatorEncoder.getDistance() - target;
		double motorPower = difference * pVal;
		
		if (motorPower < -1.0)
		{
			motorPower = -1.0;
		}
		else if (motorPower > 1.0)
		{
			motorPower = 1.0;
		}
		
		if (Math.abs(motorPower) < 0.25 && Math.abs(difference) > 0.5)
		{
			if (motorPower > 0)
			{
				motorPower = 0.25;
			}
			else
			{
				motorPower = -0.25;
			}
		}
		
		ElevatorMotor.set(-motorPower);
		
		return motorPower;
	}
	
}
