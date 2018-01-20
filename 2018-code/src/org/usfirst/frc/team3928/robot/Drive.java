package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;

/**
 * This class is for the information associated with the 
 * dive train.
 * 
 * @author NicoleEssner
 *
 */
public class Drive 
{
	/**
	 * The first speed controller on the right side of drive 
	 * train.  
	 */
	private TalonSRX RightMotor1;
	
	/**
	 * The second speed controller on the right side of drive 
	 * train.   
	 */
	private TalonSRX RightMotor2;
	
	/**
	 * The first speed controller on the left side of drive 
	 * train.  
	 */
	private TalonSRX LeftMotor1;
	
	/**
	 * The second speed controller on the left side of drive 
	 * train.  
	 */
	private TalonSRX LeftMotor2;
	
	/**
	 * The encoder for the right side of the drive train. 
	 */
	private Encoder RightEncoder;
	
	/**
	 * The encoder for the left side of the drive train. 
	 */
	private Encoder LeftEncoder;
	
	/**
	 * Constructor for the Drive class. 
	 */
	public Drive()
	{
		RightMotor1 = new TalonSRX(1);
		RightMotor2 = new TalonSRX(2);
		LeftMotor1 = new TalonSRX(3);
		LeftMotor2 = new TalonSRX(4);
		
		RightEncoder = new Encoder(2, 3);
		LeftEncoder = new Encoder(0, 1);
		
		//Wedk why 16?
		RightEncoder.setDistancePerPulse(16.0 * Math.PI / 360);
		LeftEncoder.setDistancePerPulse(16.0 * Math.PI / 360);
		
		RightEncoder.reset();
		LeftEncoder.reset();
	}
	
	/**
	 * This method sets the motor speed on the right side of the
	 * drive train. 
	 * 
	 * @param motorPower
	 * 		A double value between -1 and 1 to set the motor to. 
	 */
	public void SetRight(double motorPower)
	{
		// make one negative
		RightMotor1.set(ControlMode.PercentOutput, motorPower);
		RightMotor2.set(ControlMode.PercentOutput, motorPower);
	}
	
	/**
	 * This method sets the motor speed on the left side of the
	 * drive train. 
	 * 
	 * @param motorPower
	 * 		A double value between -1 and 1 to set the motor to.
	 */
	public void SetLeft(double motorPower)
	{
		LeftMotor1.set(ControlMode.PercentOutput, -motorPower);
		LeftMotor2.set(ControlMode.PercentOutput, -motorPower);
	}
	
	public void DriveDistance(double targetDistance)
	{
		RightEncoder.reset();
		LeftEncoder.reset();
		
		int numTimesThroughLoop = 0;  
		
		while(Math.abs(targetDistance) > Math.abs(RightEncoder.getDistance()) && 
			  Math.abs(targetDistance) > Math.abs(LeftEncoder.getDistance()))
		{
			double rightMotorPower = PID.PIDControl(targetDistance, RightEncoder.getDistance(), 0.05, 0.2, 5, (numTimesThroughLoop % 250 == 0));
			//double leftMotorPower = PID.PID(targetDistance, LeftEncoder.getDistance(), 0.05, 0.2, 5);
		
			SetRight(rightMotorPower);
			//setLeft(leftMotorPower);
			//System.out.println("Encoder right dstance: " + RightEncoder.getDistance());
			
			numTimesThroughLoop++; 
		}
	}
	
}