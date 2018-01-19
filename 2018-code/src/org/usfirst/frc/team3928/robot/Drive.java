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
		RightMotor1 = new TalonSRX(0);
		RightMotor2 = new TalonSRX(1);
		LeftMotor1 = new TalonSRX(2);
		LeftMotor2 = new TalonSRX(3);
		
		RightEncoder = new Encoder(0, 1);
		LeftEncoder = new Encoder(2, 3);
		
		RightEncoder.setDistancePerPulse(4.0 * Math.PI / 360);
		LeftEncoder.setDistancePerPulse(4.0 * Math.PI / 360);
		
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
	public void setRight(double motorPower)
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
	public void setLeft(double motorPower)
	{
		LeftMotor1.set(ControlMode.PercentOutput, motorPower);
		LeftMotor2.set(ControlMode.PercentOutput, motorPower);
	}
	
	public void driveStraight(double motorPower)
	{
		if (RightEncoder.getDistance() < LeftEncoder.getDistance())
		{
			//change for mirrored
			RightMotor1.set(ControlMode.PercentOutput, motorPower + 0.01);
			RightMotor2.set(ControlMode.PercentOutput, motorPower + 0.01);
			LeftMotor1.set(ControlMode.PercentOutput, motorPower + 0.01);
			LeftMotor2.set(ControlMode.PercentOutput, motorPower + 0.01);
		}
	}
}