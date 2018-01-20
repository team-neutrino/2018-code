package org.usfirst.frc.team3928.robot;

/**
 * This is a static class that has one method to implement a PID 
 * loop on a given mechanism. 
 * 
 * @author NicoleEssner
 *
 */
public class PID 
{
	/**
	 * Method that will use PID to get a mechanism to a set value by
	 * adjusting the value given to the motor controlling the mechanism.
	 * This method should be called in a loop. This method is used to 
	 * implement the proportional part of a PID loop.  
	 * 
	 * @param target
	 * 		The desired value for the loop the achieve. 
	 * 
	 * @param sensorPosition
	 * 		The current value of the sensor being used. 
	 * 
	 * @param pVal
	 * 		The proportional value of the PID loop. 
	 *  
	 * @param minimumMotorPower
	 * 		The minimum power that will make the mechanism PID is being
	 * 		implemented on move. 
	 * 
	 * @param allowedError
	 * 		The allowed error margin in inches that final position of 
	 * 		the mechanism can be off by. 
	 * 
	 * @param isPrinting
	 * 		True to print the target value, motor power, and current 
	 * 		sensor position. 
	 * 
	 * @return
	 * 		A value between -1 and 1 the motor that is controlling the
	 * 		mechanism should be set to. 
	 */
	public static double PIDControl(double target, double sensorPosition, double pVal,
							       double minimumMotorPower, double allowedError, boolean isPrinting)
	{
		double difference = sensorPosition - target;
		double motorPower = difference * pVal;
		
		if (motorPower < -1.0)
		{
			motorPower = -1.0;
		}
		else if (motorPower > 1.0)
		{
			motorPower = 1.0;
		}
		
		if (Math.abs(motorPower) < minimumMotorPower && 
			Math.abs(difference) > allowedError)
		{
			if (motorPower > 0)
			{
				motorPower = minimumMotorPower;
			}
			else
			{
				motorPower = -minimumMotorPower;
			}
		}
		
		if (isPrinting)
		{
			System.out.println("Target Value: " + target + " Motor Power is: " + motorPower + 
							   " Actual Position: " + sensorPosition);
		}
		
		return motorPower;
	}
}
