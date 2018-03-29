/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3928.robot;


import org.usfirst.frc.team3928.robot.AutonomousModes.AutonomousColor;
import org.usfirst.frc.team3928.robot.CubeManipulator.IntakeState;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
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
	 * Actuation to trigger the climber. 
	 */
	private Solenoid ClimbUp;

	/**
	 * Actuation to stop triggering the climber.
	 */
	private Solenoid StopClimb;


	/**
	 * The number of times that the teleopPeriodic loop 
	 * has run. 
	 */
	public static int NumTimesThroughLoop;

	/**
	 * The elevator controller 
	 */
	private Joystick ThrustMaster;
	
	private Solenoid IntakeOpen;
	private Solenoid IntakeClose;
	
	private boolean CubeManipulatorOverride;
	
	private boolean ElevatorOverride;
	
	private boolean ClimbButtonPressed;

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
		
		ClimbUp = new Solenoid(Constants.ELEVATOR_CLIMBER_SOLENOID_OUT); 
		StopClimb = new Solenoid(Constants.ELEVATOR_CLIMBER_SOLENOID_IN); 
		
		IntakeOpen = new Solenoid(7);
		IntakeClose = new Solenoid(6);
		
		CubeManipulatorOverride = false;
		
		ElevatorOverride = false;
		
		ClimbButtonPressed = false;
		
		new ValuePrinter(new Printer() 
		{
			@Override
			public void PrintValues() 
			{
				SmartDashboard.putNumber("Right joystick: ", RightJoystick.getY());
				SmartDashboard.putNumber("Left joystick: ", LeftJoystick.getY());
				SmartDashboard.putNumber("Thrustmaster value: ", ThrustMaster.getY());
				SmartDashboard.putBoolean("Set up to climb: ", ThrustMaster.getRawButton(2));
				SmartDashboard.putBoolean("Climb: ", ThrustMaster.getRawButton(1));
				SmartDashboard.putNumber("Intake: ", ThrustMaster.getRawAxis(5));
			}
		});
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
		AutonomousModes.PickAutonomousMode(AutonomousColor.ORANGE, CubeManipulatorInst, ElevatorInst, DriveInst);
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
		if(ThrustMaster.getRawButton(8))
		{
			IntakeOpen.set(true);
			IntakeClose.set(false);
		}
		else
		{
			IntakeOpen.set(false);
			IntakeClose.set(true);
		}
		
		if (CubeManipulatorOverride)
		{
			if (ThrustMaster.getRawButton(7))
			{
				CubeManipulatorInst.ActuationMotorSetPower(0.4);
			}
			else
			{
				CubeManipulatorInst.ActuationMotorSetPower(0);
			}
		}
			
		if (ElevatorOverride)
		{
			if (ThrustMaster.getRawButton(4))
			{
				ElevatorInst.setMotorSpeed(0.5);
			}
			else if (ThrustMaster.getRawButton(5))
			{
				ElevatorInst.setMotorSpeed(-0.5);
			}
			else
			{
				ElevatorInst.setMotorSpeed(0);
			}
		}
		
		if (ThrustMaster.getRawButton(1)) //climb button
		{
			ClimbButtonPressed = true;
			ElevatorInst.EnableElevatorPIDController(false);
			CubeManipulatorInst.EnableCubeManipulatorPIDController(false);
			
			ElevatorInst.setMotorSpeed(-0.3);
			
			ClimbUp.set(true);
			StopClimb.set(false);
		}
		else if (ThrustMaster.getRawButton(2)) //ready to climb button set
		{
			if (!ElevatorOverride)
				{
					ElevatorInst.setDistanceInches(70);
				}
			if (!CubeManipulatorOverride)
			{
				CubeManipulatorInst.SetActuatorSetPoint(-1);
			}
		}
		else
		{
			if (ClimbButtonPressed)
			{
				ElevatorInst.setMotorSpeed(0);
			}
			ClimbUp.set(false);
			StopClimb.set(true);
			
			double elevatorPercent = (-ThrustMaster.getZ() + 1) / 2;
			if(elevatorPercent < 0.05)
			{
				if (!ElevatorOverride)
				{
					ElevatorInst.setDistancePercent(0);
				}
				if (!CubeManipulatorOverride)
				{
					if (ThrustMaster.getRawButton(9))
					{
						CubeManipulatorInst.SetActuatorSetPoint(0);
					}
					else
					{
						CubeManipulatorInst.SetActuatorSetPoint(1);
					}
				}
			}
			else if(elevatorPercent < 0.25)
			{
				if (!ElevatorOverride)
					{
						ElevatorInst.setDistanceInches(30);
					}
				if (!CubeManipulatorOverride)
				{
					CubeManipulatorInst.SetActuatorSetPoint(0.6);
				}
			}
			else
			{
				double minScaleInches = 54;
				double elevatorRange = Constants.ELEVATOR_PID_INPUT_RANGE_MAX - minScaleInches; //71-54 = 17
				
				double elevatorHeightScalar = (elevatorPercent - 0.25) * elevatorRange / 0.75;
				if (!ElevatorOverride)
				{
					ElevatorInst.setDistanceInches(minScaleInches + elevatorHeightScalar);
				}
				
				if (!CubeManipulatorOverride)
				{
						CubeManipulatorInst.SetActuatorSetPoint(0.85);
				}
			}
			
		}

		double leftY = -LeftJoystick.getY();
		double rightY = -RightJoystick.getY();

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

		double intakeSpeed = ThrustMaster.getRawAxis(5);
		if (intakeSpeed > 0.5)
		{
			CubeManipulatorInst.MoveCube(IntakeState.INTAKE);
		}
		else if (intakeSpeed < -0.3)
		{
			if(intakeSpeed < -0.8)
			{
				intakeSpeed = -0.8;
			}
			CubeManipulatorInst.MoveCube(intakeSpeed);
		}
		else
		{
			CubeManipulatorInst.MoveCube(IntakeState.OFF);
		}

		if (NumTimesThroughLoop % Constants.PRINT_SPEED_DIVIDER == 0) 
		{

		}
		
		if (ThrustMaster.getRawButton(7))
		{
			CubeManipulatorOverride = true;
			CubeManipulatorInst.EnableCubeManipulatorPIDController(false);
		}
		else if (ThrustMaster.getRawButton(10))
		{
			CubeManipulatorOverride = false;
			if (!ClimbButtonPressed)
			{
				CubeManipulatorInst.EnableCubeManipulatorPIDController(true);
			}
		}
		
		if (ThrustMaster.getRawButton(4) || ThrustMaster.getRawButton(5))
		{
			ElevatorOverride = true;
			ElevatorInst.EnableElevatorPIDController(false);
		}
		else if (ThrustMaster.getRawButton(3))
		{
			ElevatorOverride = false;
			if (!ClimbButtonPressed)
			{
				ElevatorInst.EnableElevatorPIDController(true);
			}
		}
		
		NumTimesThroughLoop++;
		Utill.SleepThread(1);
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() 
	{

	}
}
