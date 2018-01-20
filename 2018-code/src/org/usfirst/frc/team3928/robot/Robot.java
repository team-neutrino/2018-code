/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot 
{
	private Talon IntakeSideMotor;
	private Talon IntakeFrontMotor;
	private Talon IntakeUpDownMotor;
	
	private Joystick LeftJoystick;
	private Joystick RightJoystick;
	private XboxController controller;
	
	private Elevator ElevatorInst;
	private Drive DriveInst;
	
	private int NumTimesThroughLoop;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() 
	{
		IntakeSideMotor = new Talon(0);
		IntakeFrontMotor = new Talon(1);
		IntakeUpDownMotor = new Talon(2);
		
		controller = new XboxController(0);
		LeftJoystick = new Joystick(1);
		RightJoystick = new Joystick(2);
		
		ElevatorInst = new Elevator();
		DriveInst = new Drive();
		
		NumTimesThroughLoop = 0;
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() 
	{
		DriveInst.DriveDistance(20);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() 
	{
		
		
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() 
	{
//		IntakeSideMotor.set(LeftJoystick.getY());
//		IntakeFrontMotor.set(RightJoystick.getY());
//		IntakeUpDownMotor.set(controller.getX());
		
		double setDistance = LeftJoystick.getY() * 10 + 20;
		
		if (NumTimesThroughLoop % 250 == 0)
		{
//			System.out.println();
		}
		
		NumTimesThroughLoop++;
		
		try 
		{
			Thread.sleep(1);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() 
	{
		
	}
	
}
