package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class DriveCurve 
{
	/**
	 * Drive object
	 */
	private Drive drive;
	
	/**
	 * PID controller for right side.
	 */
	private PIDController RightPIDController;
	
	/**
	 * PID controller for left side.
	 */
	private PIDController LeftPIDController;
	
	/**
	 * Constructor for curve
	 * @param drive
	 */
	public DriveCurve(Drive drive)
	{
		this.drive = drive;
		
		RightPIDController = new PIDController(0.01, 0, 0, new PIDSource() {
		
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) 
			{
				// TODO Auto-generated method stub
				System.out.println("This is the PIDSourceType: " + pidSource.toString());
			}
			
			@Override
			public double pidGet() 
			{
				// TODO Auto-generated method stub
				return drive.GetRightEncoder();
			}
			
			@Override
			public PIDSourceType getPIDSourceType() 
			{
				// TODO Auto-generated method stub
				return PIDSourceType.kRate;
			}
		}, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) 
			{
				// TODO Auto-generated method stub
				drive.SetRight(output);
			}
		});
		
		
		
		LeftPIDController = new PIDController(0.0075, 0, 0, new PIDSource() {
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) 
			{
				// TODO Auto-generated method stub
				System.out.println("This is the PIDSourceType: " + pidSource.toString());
			}
			
			@Override
			public double pidGet() 
			{
				// TODO Auto-generated method stub
				System.out.println("Left Encoder: " + drive.GetLeftEncoder());
				return drive.GetLeftEncoder();
			}
			
			@Override
			public PIDSourceType getPIDSourceType() 
			{
				// TODO Auto-generated method stub
				return PIDSourceType.kRate;
			}
		}, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) 
			{
				// TODO Auto-generated method stub
				drive.SetLeft(output);
				System.out.println("Left output: " + output);
			}
		});
		LeftPIDController.setOutputRange(0, 1);
		RightPIDController.setOutputRange(0, 1);
		
		LeftPIDController.setInputRange(0, 150);
		RightPIDController.setInputRange(0, 150);
	}
	
	/**
	 * Drives robot in a curve using given rates for given amount of time.
	 * @param leftRate
	 * 	The rate to turn left wheels
	 * @param rightRate
	 * 	The rate to turn right wheels
	 * @param timeMillis
	 * 	The time to drive in curve
	 */
	public void Curve(double leftRate, double rightRate, double timeMillis)
	{
		long time = System.currentTimeMillis();
		
		RightPIDController.setSetpoint(rightRate);
		LeftPIDController.setSetpoint(leftRate);
		
		RightPIDController.enable();
		LeftPIDController.enable();
		
		while(System.currentTimeMillis() - time < timeMillis && DriverStation.getInstance().isAutonomous())
		{
			Utill.SleepThread(1);
		}
		
		RightPIDController.disable();
		LeftPIDController.disable();
	}
	
}
