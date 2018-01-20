package org.usfirst.frc.team3928.robot;

public class PID 
{
	public static double PID(double target, double actualPosition, double pVal,
							 double minimumMotorPower, double allowedError)
	{
		//t = 20
		//a = 19.9
		//p = .2
		double difference = actualPosition - target;
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
		
		//System.out.println("Target Value: " + target + " Motor Power is: " + motorPower + 
		//				   " Actual Position: " + actualPosition);
		return motorPower;
	}
}
