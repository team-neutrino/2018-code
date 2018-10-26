/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3928.robot;


import org.usfirst.frc.team3928.robot.AutonomousModes.AutonomousColor;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
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
	 * The object to drive a curve.
	 */
	private DriveCurve DriveCurveInst;

	/**
	 * The elevator controller 
	 */
	private Joystick ThrustMaster;
	
	/**
	 * Whether the cube manipulator override button is being pushed or not.
	 */
	private boolean CubeManipulatorOverride;
	
	/**
	 * Whether the elevator override button is being pushed or not.
	 */
	private boolean ElevatorOverride;
	
	/**
	 * If the cube manipulator should be open.
	 */
	private boolean IntakingOpen;
	
	/**
	 * Time for when an override button has been pushed, 0 if it has not been pushed.
	 */
	private long OverridePressed;
	
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
		
		CubeManipulatorOverride = false;
		
		ElevatorOverride = false;
		
		IntakingOpen = false;
		
		OverridePressed = 0;
//		AutoChooser = new SendableChooser<String>();
//		AutoChooser.addDefault("Blue: drive forward", "BLUE");
//		AutoChooser.addObject("Yellow: center switch", "YELLOW");
//		AutoChooser.addObject("Green: right to scale", "GREEN");
//		AutoChooser.addObject("Orange: right to switch and scale", "ORANGE");
//		AutoChooser.addObject("Purple: left to switch and scale - do not use", "PURPLE");
//		AutoChooser.addObject("Pink: right only right side", "PINK");
//		AutoChooser.addObject("Red: left only left side", "RED");
//		AutoChooser.addObject("Test", "TEST");
//		SmartDashboard.putData("Autonomous Mode", AutoChooser);
		
		DriveCurveInst = new DriveCurve(DriveInst);
		
		new ValuePrinter(new Printer() 
		{
			@Override
			public void PrintValues() 
			{
				SmartDashboard.putNumber("Right joystick: ", RightJoystick.getY());
				SmartDashboard.putNumber("Left joystick: ", LeftJoystick.getY());
				SmartDashboard.putNumber("Thrustmaster value: ", (-ThrustMaster.getZ() + 1) / 2);
				SmartDashboard.putNumber("Intake: ", ThrustMaster.getRawAxis(5));
				SmartDashboard.putBoolean("Elevator Override: ", ElevatorOverride);
				SmartDashboard.putBoolean("Intake Override: ", CubeManipulatorOverride);
			}
		}
		);
		
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		
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
//		if (AutoChooser.getSelected().equals("BLUE"))
//		{
//			AutonomousModes.PickAutonomousMode(AutonomousColor.BLUE, CubeManipulatorInst, ElevatorInst, DriveInst, DriveCurveInst);
//		}
//		else if (AutoChooser.getSelected().equals("YELLOW"))
//		{
//			AutonomousModes.PickAutonomousMode(AutonomousColor.YELLOW, CubeManipulatorInst, ElevatorInst, DriveInst, DriveCurveInst);
//		}
//		else if (AutoChooser.getSelected().equals("GREEN"))
//		{
//			AutonomousModes.PickAutonomousMode(AutonomousColor.GREEN, CubeManipulatorInst, ElevatorInst, DriveInst, DriveCurveInst);
//		}
//		else if (AutoChooser.getSelected().equals("ORANGE"))
//		{
//			AutonomousModes.PickAutonomousMode(AutonomousColor.ORANGE, CubeManipulatorInst, ElevatorInst, DriveInst, DriveCurveInst);
//		}
//		else if (AutoChooser.getSelected().equals("PURPLE"))
//		{
//			AutonomousModes.PickAutonomousMode(AutonomousColor.PURPLE, CubeManipulatorInst, ElevatorInst, DriveInst, DriveCurveInst);
//		}
//		else if (AutoChooser.getSelected().equals("PINK"))
//		{
//			System.out.println("PINK");
//			
//			AutonomousModes.PickAutonomousMode(AutonomousColor.PINK, CubeManipulatorInst, ElevatorInst, DriveInst, DriveCurveInst);
//		}
//		else if (AutoChooser.getSelected().equals("RED"))
//		{
//			AutonomousModes.PickAutonomousMode(AutonomousColor.RED, CubeManipulatorInst, ElevatorInst, DriveInst, DriveCurveInst);
//		}
//		else if (AutoChooser.getSelected().equals("TEST"))
//		{
//			AutonomousModes.PickAutonomousMode(AutonomousColor.TEST, CubeManipulatorInst, ElevatorInst, DriveInst, DriveCurveInst);
//		}
//		else
//		{
//			AutonomousModes.PickAutonomousMode(AutonomousColor.BLUE, CubeManipulatorInst, ElevatorInst, DriveInst, DriveCurveInst);
//
//		}
		
		AutonomousModes.PickAutonomousMode(AutonomousColor.PINK, CubeManipulatorInst, ElevatorInst, DriveInst, DriveCurveInst);
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
		//Drive control
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
		
		//Elevator control
		double elevatorPercent = (-ThrustMaster.getZ() + 1) / 2;
			
		if(elevatorPercent < 0.05)	
		{
			if (!ElevatorOverride)
			{
				ElevatorInst.setDistancePercent(0);
			}
				
			if (!CubeManipulatorOverride)
			{
				if (LeftJoystick.getRawButton(6) || RightJoystick.getRawButton(2) || LeftJoystick.getRawButton(2))
				{
					CubeManipulatorInst.SetActuatorSetPoint(1);
				}
				else
				{
					CubeManipulatorInst.SetActuatorSetPoint(0.1);
				}
			}
		}
		else if(elevatorPercent < 0.25)
		{
			if (!ElevatorOverride)
			{
				ElevatorInst.setDistanceInches(22);
			}
			if (!CubeManipulatorOverride)
			{
				CubeManipulatorInst.SetActuatorSetPoint(0.6);
			}
		}
		else
		{
			double minScaleInches = 45;
			double elevatorRange = Constants.ELEVATOR_PID_INPUT_RANGE_MAX - minScaleInches; //71-54 = 17
			double elevatorHeightScalar = (elevatorPercent - 0.25) * elevatorRange / 0.75;
		
			if (!ElevatorOverride)
			{
				ElevatorInst.setDistanceInches(minScaleInches + elevatorHeightScalar);
			}
				
			if (!CubeManipulatorOverride)
			{
					CubeManipulatorInst.SetActuatorSetPoint(0.75);
			}
		}
		
		//Cube manipulator arm control
		if(ThrustMaster.getRawButton(8) && !IntakingOpen)
		{
			CubeManipulatorInst.ArmPosition(true);
		}
		else if(!IntakingOpen)
		{
			CubeManipulatorInst.ArmPosition(false);
		}
		
		//Cube manipulator wheels control
		double intakeSpeed = ThrustMaster.getRawAxis(5);
		
		if (ThrustMaster.getRawButton(9))	
		{
			CubeManipulatorInst.MoveCube(1);
			CubeManipulatorInst.ArmPosition(true);
			IntakingOpen = true;
		}
		else if (intakeSpeed > 0.5)
		{
			CubeManipulatorInst.MoveCube(1);
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
			CubeManipulatorInst.MoveCube(0);
			IntakingOpen = false;
		}
		
		//Cube manipulator override enable/disable
		if (ThrustMaster.getRawButton(10))
		{
			if (OverridePressed == 0)
			{
				OverridePressed = System.currentTimeMillis();
			}
			
			if ((OverridePressed + 1000) < System.currentTimeMillis())
			{
				//CubeManipulatorOverride = !CubeManipulatorOverride;
				CubeManipulatorInst.EnableCubeManipulatorPIDController(!CubeManipulatorOverride);
				OverridePressed = 0;
			}
		}
	
		//Elevator override enable/disable
		if (ThrustMaster.getRawButton(3))
		{
			if (OverridePressed == 0)
			{
				OverridePressed = System.currentTimeMillis();
			}
			
			if ((OverridePressed + 1000) < System.currentTimeMillis())
			{
				ElevatorOverride = !ElevatorOverride;
				ElevatorInst.EnableElevatorPIDController(!ElevatorOverride);
				OverridePressed = 0;
			}
		}
		
		//Cube manipulator override control
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
			
		//Elevator override control
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