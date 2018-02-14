/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3928.robot;

import java.util.Set;

import org.usfirst.frc.team3928.robot.CubeManipulator.IntakeState;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.hal.PDPJNI;
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
	/**
	 * The left joystick on the drivers station. 
	 */
	private Joystick LeftJoystick;
	
	/**
	 * The right joystick on the drivers station. 
	 */
	private Joystick RightJoystick;
	
	/**
	 * The x-box controller on the drivers station. 
	 */
	private XboxController Controller;
	
	/**
	 * The object for the elevator. 
	 */
	private Elevator ElevatorInst;
	
	/**
	 * The object for the drive. 
	 */
	private Drive DriveInst;
	
	/**
	 * the object for the cube manipulator;
	 */
	private CubeManipulator CubeManipulatorInst;
	
	/**
	 * The number of times that the teleopPeriodic loop 
	 * has run. 
	 */
	public static int NumTimesThroughLoop;
	
	/**
	 * The elevator controller 
	 */
	public Joystick ThrustMaster;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() 
	{
		ThrustMaster = new Joystick(Constants.THRUST_MASTER_CONTROLLER);
		LeftJoystick = new Joystick(Constants.LEFT_JOYSTICK); 
		RightJoystick = new Joystick(Constants.RIGHT_JOYSTICK); 
		
		ElevatorInst = new Elevator();
		DriveInst = new Drive();
		CubeManipulatorInst = new CubeManipulator();
		
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
		//DriveInst.TurnDegrees(90);
		//DriveInst.DriveDistance(140);
		
		
//		try 
//		{
//			ElevatorInst.setDistanceInches(30);
//			DriveInst.DriveDistance(140);
//			Thread.sleep(1000);
//			DriveInst.TurnDegrees(-90);
//			Thread.sleep(1000);
//			DriveInst.DriveDistance(40);
//		} 
//		catch (InterruptedException e) 
//		{
//			e.printStackTrace();
//		}
		
		try 
		{
			ElevatorInst.setDistanceInches(30);
			DriveInst.DriveDistance(140);
			Thread.sleep(1000);
			DriveInst.TurnDegrees(90);
			Thread.sleep(1000);
			DriveInst.DriveDistance(40);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
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
		double elevatorPercent = (-ThrustMaster.getZ() + 1) / 2;
		ElevatorInst.setDistancePercent(elevatorPercent);
		
		double leftY = LeftJoystick.getY();
		double rightY = RightJoystick.getY();
		
		if (Math.abs(leftY) < 0.05)
		{
			leftY = 0;
		}
		
		if (Math.abs(rightY) < 0.05)
		{
			rightY = 0;
		}
		
		DriveInst.SetLeft(leftY);
		DriveInst.SetRight(rightY);
		
//		if (LeftJoystick.getRawButton(1))
//		{
//			CubeManipulatorInst.MoveCube(IntakeState.INTAKE);
//		}
//		else if (LeftJoystick.getRawButton(2))
//		{
//			CubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
//		}
//		else
//		{
//			CubeManipulatorInst.MoveCube(IntakeState.OFF);
//		}
		
		if (NumTimesThroughLoop % Constants.PRINT_SPEED_DIVIDER == 0) 
		{

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
