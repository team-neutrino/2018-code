package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Class the holds all the autonomous methods.
 * 
 * @author NicoleEssner
 *
 */
public class AutonomousModes 
{
	/**
	 * The color of the autonomous mode that is going to be 
	 * executed.
	 * 
	 * @author NicoleEssner
	 *
	 */
	public enum AutonomousColor
	{
		BLUE, YELLOW, GREEN, ORANGE, PURPLE, PINK, RED, TEST;
	}
	
	/**
	 * The side of the field element to drop the cube in. 
	 * 
	 * @author NicoleEssner
	 *
	 */
	private enum FieldElementSide
	{
		LEFT, RIGHT;
	}

	/**
	 * Will take in an integer value corresponding to the autonomous mode, also passed in the 
	 * objects needed to make the autonomous mode possible. 
	 * 
	 * @param autonomousMode
	 * 		The integer value of the autonomous mode that is going to be executed. 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive. 
	 */
	public static void PickAutonomousMode(AutonomousColor color, CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{	
		switch (color)
		{			
			case BLUE:
			{
				//drive forward
				Blue(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case YELLOW:
			{
				//start from center to switch
				Yellow(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case GREEN:
			{
				//start from right to scale
				Green(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case ORANGE:
			{
				//start from right to switch and scale
				Orange(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case PURPLE:
			{
				//start from left to switch and scale
				Purple(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case PINK:
			{
				//start from right, only do right side elements
				Pink(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case RED:
			{
				//start from left, only do left side elements
				Red(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case TEST:
			{
				Test(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			default:
			{
				Blue(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
		}
	}

	/**
	 * Drives forward across the base line. 
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive. 
	 */
	private static void Blue(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Blue");
		Utill.SleepThread(7000);
		
		driveInst.DriveDistance(200);
		
	}

	/**
	 * Single cube dropped in the switch from the center
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive.
	 */
	private static void Yellow(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Yellow");
		int turnDegreesSign = 1;
		if (getFieldElementSideColor()[0] == FieldElementSide.LEFT)
		{
			turnDegreesSign = -1;
			elevatorInst.setDistanceInches(10);
			driveInst.DriveDistance(24);
			Utill.SleepThread(200);
			driveInst.TurnDegrees(turnDegreesSign * 60, 1000);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			driveInst.DriveDistance(75); //85
			elevatorInst.setDistanceInches(24);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			driveInst.TurnDegrees(turnDegreesSign * -60, 1000);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
		}
		else 
		{
			elevatorInst.setDistanceInches(10);
			driveInst.DriveDistance(24);
			Utill.SleepThread(200);
			driveInst.TurnDegrees(turnDegreesSign * 45, 1000);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			driveInst.DriveDistance(65); //65
			elevatorInst.setDistanceInches(24);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			driveInst.TurnDegrees(turnDegreesSign * -45, 1000);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
		}
		driveInst.DriveDistance(20);
		driveInst.SetLeft(0.6);
		driveInst.SetRight(0.6);
		Utill.SleepThread(600);
		driveInst.SetLeft(0);
		driveInst.SetRight(0);
		Utill.SleepThread(250);
		cubeManipulatorInst.MoveCube(-1);
		Utill.SleepThread(500);
		cubeManipulatorInst.MoveCube(0);
	}

	/**
	 * Single cube in the scale from the right
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive.
	 */
	private static void Green(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Green");
		if(getFieldElementSideColor()[1] == FieldElementSide.RIGHT)
		{
			driveInst.DriveDistance(210);
			driveInst.TurnDegrees(-37, 1000);
			elevatorInst.setDistancePercent(100);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			Utill.SleepThread(100);
			driveInst.DriveDistance(43);
			cubeManipulatorInst.MoveCube(-1);
			Utill.SleepThread(250);
			cubeManipulatorInst.MoveCube(0);
			elevatorInst.setDistancePercent(0);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			Utill.SleepThread(1000);;
			driveInst.TurnDegrees(-133, 1000);
			driveInst.DriveDistance(48);
		}
		else
		{
			driveInst.DriveDistance(200);
			driveInst.TurnDegrees(-85, 1000);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			Utill.SleepThread(500);
			driveInst.DriveDistance(230);
			driveInst.TurnDegrees(100, 1500);
			elevatorInst.setDistancePercent(100);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			Utill.SleepThread(300);
			driveInst.DriveDistance(30);
			cubeManipulatorInst.MoveCube(-1);
			Utill.SleepThread(250);
			cubeManipulatorInst.MoveCube(0);
			elevatorInst.setDistancePercent(0);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			driveInst.TurnDegrees(140, 3000);
		}
		
		
	}

	/**
	 * Cube dropped in the switch and scale with robot starting on the right.
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive.
	 */
	private static void Orange(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Orange");
		elevatorInst.setDistanceInches(12);
		
		FieldElementSide[] colors = getFieldElementSideColor();
		
		//scale is right
		if(colors[1] == FieldElementSide.RIGHT)
		{
			driveInst.DriveDistance(210);
			driveInst.TurnDegrees(-37, 1000);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			elevatorInst.setDistancePercent(100);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			Utill.SleepThread(100);
			driveInst.DriveDistance(43);
			cubeManipulatorInst.MoveCube(-1);
			Utill.SleepThread(250);
			cubeManipulatorInst.MoveCube(0);
			elevatorInst.setDistancePercent(0);
			
			//switch is right
			if(colors[0] == FieldElementSide.RIGHT)
			{	
				driveInst.TurnDegrees(-110, 3000);
				cubeManipulatorInst.MoveCube(1);
				driveInst.SetLeft(0.5);
				driveInst.SetRight(0.5);
				Utill.SleepThread(1900);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				cubeManipulatorInst.MoveCube(0);
				elevatorInst.setDistanceInches(30);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				Utill.SleepThread(300);
				driveInst.SetLeft(0.5);
				driveInst.SetRight(0.5);
				Utill.SleepThread(400);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				Utill.SleepThread(1000);
				cubeManipulatorInst.MoveCube(-1);
				Utill.SleepThread(1000);
				cubeManipulatorInst.MoveCube(0);
			}
			//switch is left
			else
			{	
				driveInst.DriveDistanceBackwards(-47);
				driveInst.TurnDegrees(-53, 1000);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.DriveDistance(160);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				cubeManipulatorInst.MoveCube(1);
				driveInst.TurnDegrees(-70, 1000);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.SetLeft(0.5);
				driveInst.SetRight(0.5);
				Utill.SleepThread(1000);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				Utill.SleepThread(200);
				driveInst.SetLeft(-0.2);
				driveInst.SetRight(-0.2);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				Utill.SleepThread(500);
				driveInst.SetLeft(0);
				driveInst.SetLeft(0);
				cubeManipulatorInst.MoveCube(0);
				Utill.SleepThread(200);
				elevatorInst.setDistanceInches(30);
				Utill.SleepThread(500);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.SetLeft(0.5);
				driveInst.SetRight(0.5);
				Utill.SleepThread(200);
				driveInst.SetLeft(0);
				driveInst.SetLeft(0);
				cubeManipulatorInst.MoveCube(-1);
				Utill.SleepThread(500);
				cubeManipulatorInst.MoveCube(0);
			}		
		}
		//scale is left
		else
		{	
			//switch is right
			if(colors[0] == FieldElementSide.RIGHT)
			{			
				driveInst.DriveDistance(80);
				driveInst.TurnDegrees(-45, 1000);
				elevatorInst.setDistanceInches(24);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.SetLeft(0.5);
				driveInst.SetRight(0.5);
				Utill.SleepThread(1000);
				driveInst.SetLeft(0);
				driveInst.SetLeft(0);
				cubeManipulatorInst.MoveCube(-1);
				Utill.SleepThread(250);
				cubeManipulatorInst.MoveCube(0);
				driveInst.DriveDistanceBackwards(-20);
				elevatorInst.setDistancePercent(0);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.TurnDegrees(45, 1000);
				driveInst.DriveDistance(128);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.TurnDegrees(-132, 2000);
				cubeManipulatorInst.MoveCube(1);
				driveInst.DriveDistance(52);
				Utill.SleepThread(350);
				cubeManipulatorInst.MoveCube(0);
				driveInst.DriveDistanceBackwards(-16);
				elevatorInst.setDistanceInches(12);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.TurnDegrees(43, 1000);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.DriveDistance(200);
				elevatorInst.setDistancePercent(100);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.TurnDegrees(135, 1000);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.DriveDistance(24);
				cubeManipulatorInst.MoveCube(-1);
				Utill.SleepThread(250);
				cubeManipulatorInst.MoveCube(0);	
			}
			//switch is left
			else
			{	
				driveInst.DriveDistance(200);
				driveInst.TurnDegrees(-85, 1000);
				Utill.SleepThread(500);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.DriveDistance(230);
				driveInst.TurnDegrees(100, 1500);
				elevatorInst.setDistancePercent(100);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				Utill.SleepThread(300);
				driveInst.DriveDistance(40);
				cubeManipulatorInst.MoveCube(-1);
				Utill.SleepThread(250);
				cubeManipulatorInst.MoveCube(0);
				elevatorInst.setDistancePercent(0);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.TurnDegrees(145, 3000);
				cubeManipulatorInst.MoveCube(1);
				driveInst.SetLeft(0.8);
				driveInst.SetRight(0.8);
				Utill.SleepThread(1000);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				Utill.SleepThread(300);
				driveInst.SetLeft(-0.5);
				driveInst.SetRight(-0.5);
				Utill.SleepThread(200);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				cubeManipulatorInst.MoveCube(0);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				elevatorInst.setDistanceInches(24);
				Utill.SleepThread(800);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				cubeManipulatorInst.MoveCube(-1);
				Utill.SleepThread(250);
				cubeManipulatorInst.MoveCube(0);
			}
		}
		
	}
	
	/**
	 * Cube dropped in the switch and scale with robot starting on the left.
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive.
	 */
	private static void Purple(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Purple");
		elevatorInst.setDistanceInches(12);
		
		FieldElementSide[] colors = getFieldElementSideColor();
		
		if(colors[1] == FieldElementSide.RIGHT)
		{	
			if(colors[0] == FieldElementSide.RIGHT)
			{	
				driveInst.DriveDistance(220);
				driveInst.TurnDegrees(90, 1000);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				Utill.SleepThread(500);
				driveInst.DriveDistance(80);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.DriveDistance(155);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.TurnDegrees(-110, 1500);
				elevatorInst.setDistancePercent(100);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				Utill.SleepThread(100);
				driveInst.DriveDistance(42);
				cubeManipulatorInst.MoveCube(-1);
				Utill.SleepThread(250);
				cubeManipulatorInst.MoveCube(0);
				elevatorInst.setDistancePercent(0);
				driveInst.DriveDistanceBackwards(80);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
			
				driveInst.TurnDegrees(-130, 3000);
				cubeManipulatorInst.MoveCube(1);
				driveInst.SetLeft(0.7);
				driveInst.SetRight(0.7);
				Utill.SleepThread(1400);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				Utill.SleepThread(500);
				cubeManipulatorInst.MoveCube(0);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				elevatorInst.setDistanceInches(30);
				Utill.SleepThread(1500);
				cubeManipulatorInst.MoveCube(-1);
				Utill.SleepThread(1000);
				cubeManipulatorInst.MoveCube(0);
				
//				driveInst.TurnDegrees(180);
//				cubeManipulatorInst.MoveCube(IntakeState.INTAKE);
//				driveInst.DriveDistance(66);
//				Utill.SleepThread(250);
//				cubeManipulatorInst.MoveCube(IntakeState.OFF);
//				elevatorInst.setDistanceInches(30);
//				Utill.SleepThread(1500);
//				cubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
//				Utill.SleepThread(1000);
//				cubeManipulatorInst.MoveCube(IntakeState.OFF);
			}
			else
			{	
				driveInst.DriveDistance(100);
			}		
		}
		else
		{			
			if(colors[1] == FieldElementSide.RIGHT)
			{
				
			}
			else
			{
				
			}
		}
	}
	
	
	/**
	 * Performs operations on the right side of the field.
	 * 		If _R_: Two cube to scale //TODO
	 * 		If RL_: Cube to switch
	 * 		If LL_: Drive forward
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator.
	 * @param elevatorInst
	 * 		An instance of the elevator.
	 * @param driveInst
	 * 		An instance of the drive.
	 */
	private static void Pink(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Pink");
		elevatorInst.setDistanceInches(12);
		cubeManipulatorInst.SetActuatorSetPoint(0);

		FieldElementSide[] colors = getFieldElementSideColor();
		
		//scale is right
		if(colors[1] == FieldElementSide.RIGHT)
		{
			//cube in scale
			driveInst.DriveDistance(210);
			driveInst.TurnDegrees(-37, 1000);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			elevatorInst.setDistancePercent(100);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			Utill.SleepThread(100);
			cubeManipulatorInst.SetActuatorSetPoint(0.7);
			driveInst.DriveDistance(43);
			cubeManipulatorInst.MoveCube(-0.8);
			Utill.SleepThread(250);
			cubeManipulatorInst.MoveCube(0);
			
			//pick up cube by switch
			elevatorInst.setDistancePercent(0);
			cubeManipulatorInst.SetActuatorSetPoint(0);
			driveInst.TurnDegrees(-115, 3000);
			cubeManipulatorInst.MoveCube(1);
			driveInst.SetLeft(0.5);
			driveInst.SetRight(0.5);
			Utill.SleepThread(1900);
			driveInst.SetLeft(0);
			driveInst.SetRight(0);
			cubeManipulatorInst.MoveCube(0);

			//second cube in scale
			elevatorInst.setDistanceInches(12);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			driveInst.SetLeft(-0.4);
			driveInst.SetRight(-0.4);
			Utill.SleepThread(400);
			driveInst.SetLeft(0);
			driveInst.SetRight(0);
			elevatorInst.setDistancePercent(100);
			driveInst.TurnDegrees(130, 3000);
			cubeManipulatorInst.SetActuatorSetPoint(0.7);
			driveInst.SetLeft(0.7);
			driveInst.SetRight(0.7);
			Utill.SleepThread(850);
			driveInst.SetLeft(0);
			driveInst.SetRight(0);
			cubeManipulatorInst.MoveCube(-0.7);
			Utill.SleepThread(250);
			cubeManipulatorInst.MoveCube(0);
			
			//pick up another cube by switch
			driveInst.SetLeft(-0.5);
			driveInst.SetRight(-0.5);
			Utill.SleepThread(600);
			driveInst.SetLeft(0);
			driveInst.SetRight(0);
			elevatorInst.setDistancePercent(0);
			Utill.SleepThread(500);
			driveInst.TurnDegrees(-110, 3000);
			cubeManipulatorInst.MoveCube(1);
			cubeManipulatorInst.SetActuatorSetPoint(0);
			driveInst.SetLeft(0.7);
			driveInst.SetRight(0.7);
			Utill.SleepThread(1050);
			cubeManipulatorInst.MoveCube(0);
			
			//prep for teleop
			driveInst.SetLeft(-0.5);
			driveInst.SetRight(-0.5);
			Utill.SleepThread(300);
			driveInst.SetLeft(0);
			driveInst.SetRight(0);
			elevatorInst.setDistancePercent(100);
			driveInst.TurnDegrees(150, 3000);
			
		}
		//scale is left
		else
		{	
			//switch is right
			if(colors[0] == FieldElementSide.RIGHT)
			{			
				driveInst.DriveDistance(80);
				driveInst.TurnDegrees(-45, 1000);
				elevatorInst.setDistanceInches(24);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.SetLeft(0.5);
				driveInst.SetRight(0.5);
				Utill.SleepThread(1000);
				driveInst.SetLeft(0);
				driveInst.SetLeft(0);
				cubeManipulatorInst.MoveCube(-1);
				Utill.SleepThread(250);
				cubeManipulatorInst.MoveCube(0);
				driveInst.DriveDistanceBackwards(-20);
				elevatorInst.setDistancePercent(0);
				if (!DriverStation.getInstance().isAutonomous())
				{
					return;
				}
				driveInst.TurnDegrees(45, 1000);
				driveInst.DriveDistance(128);
				
			}
			//switch is left
			else
			{
				driveInst.DriveDistance(150);
			}
		}
	}
	
	
	/**
	 * Performs operations on the left side of the field.
	 * 		If _L_: Two cube to scale
	 * 		If LR_: Cube to switch
	 * 		If RR_: Drive forward
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator.
	 * @param elevatorInst
	 * 		An instance of the elevator.
	 * @param driveInst
	 * 		An instance of the drive.
	 */
	private static void Red(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Red");
		elevatorInst.setDistanceInches(12);
		
		FieldElementSide[] colors = getFieldElementSideColor();
		
		if(colors[1] == FieldElementSide.LEFT) //scale is left
		{	
			//cube in scale
			elevatorInst.setDistanceInches(12);
			cubeManipulatorInst.SetActuatorSetPoint(0);
			driveInst.DriveDistance(210);
			driveInst.TurnDegrees(37, 1000);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			elevatorInst.setDistancePercent(100);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			Utill.SleepThread(100);
			cubeManipulatorInst.SetActuatorSetPoint(0.7);
			driveInst.DriveDistance(43);
			cubeManipulatorInst.MoveCube(-0.8);
			Utill.SleepThread(250);
			cubeManipulatorInst.MoveCube(0);
			
			//pick up cube by switch
			elevatorInst.setDistancePercent(0);
			cubeManipulatorInst.SetActuatorSetPoint(0);
			driveInst.TurnDegrees(120, 3000);
			cubeManipulatorInst.MoveCube(1);
			driveInst.SetLeft(0.5);
			driveInst.SetRight(0.5);
			Utill.SleepThread(1900);
			driveInst.SetLeft(0);
			driveInst.SetRight(0);
			cubeManipulatorInst.MoveCube(0);
			
			//second cube in scale
			elevatorInst.setDistanceInches(12);
			if (!DriverStation.getInstance().isAutonomous())
			{
				return;
			}
			driveInst.SetLeft(-0.4);
			driveInst.SetRight(-0.4);
			Utill.SleepThread(400);
			driveInst.SetLeft(0);
			driveInst.SetRight(0);
			elevatorInst.setDistancePercent(100);
			driveInst.TurnDegrees(-150, 3000);
			cubeManipulatorInst.SetActuatorSetPoint(0.7);
			driveInst.SetLeft(0.7);
			driveInst.SetRight(0.7);
			Utill.SleepThread(850);
			driveInst.SetLeft(0);
			driveInst.SetRight(0);
			cubeManipulatorInst.MoveCube(-0.6);
			Utill.SleepThread(250);
			cubeManipulatorInst.MoveCube(0);
			
			//pick up another cube by switch
			driveInst.SetLeft(-0.5);
			driveInst.SetRight(-0.5);
			Utill.SleepThread(400);
			driveInst.SetLeft(0);
			driveInst.SetRight(0);
			elevatorInst.setDistancePercent(0);
			Utill.SleepThread(500);
			driveInst.TurnDegrees(140, 3000);
			cubeManipulatorInst.MoveCube(1);
			cubeManipulatorInst.SetActuatorSetPoint(0);
			driveInst.SetLeft(0.7);
			driveInst.SetRight(0.7);
			Utill.SleepThread(1050);
			cubeManipulatorInst.MoveCube(0);
			
			//prep for teleop
			driveInst.SetLeft(-0.5);
			driveInst.SetRight(-0.5);
			Utill.SleepThread(300);
			driveInst.SetLeft(0);
			driveInst.SetRight(0);
			elevatorInst.setDistancePercent(100);
			driveInst.TurnDegrees(-150, 3000);
		}	
		else //scale is right
		{	
			if(colors[0] == FieldElementSide.LEFT) //switch is left
			{			
				//cube in switch
				elevatorInst.setDistanceInches(12);
				cubeManipulatorInst.SetActuatorSetPoint(0.6);
				driveInst.DriveDistance(140);
				driveInst.TurnDegrees(85, 1000);
				elevatorInst.setDistanceInches(24);
				driveInst.SetLeft(0.6);
				driveInst.SetRight(0.6);
				Utill.SleepThread(650);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				cubeManipulatorInst.MoveCube(-0.7);
				Utill.SleepThread(500);
				cubeManipulatorInst.MoveCube(0);
				
				//pick up cube by switch
				driveInst.DriveDistanceBackwards(-15);
				driveInst.TurnDegrees(-85, 1000);
				elevatorInst.setDistancePercent(0);
				driveInst.DriveDistance(85);
				driveInst.TurnDegrees(135, 1000);
				cubeManipulatorInst.SetActuatorSetPoint(0);
				cubeManipulatorInst.MoveCube(1);
				driveInst.SetLeft(0.7);
				driveInst.SetRight(0.7);
				Utill.SleepThread(750);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				Utill.SleepThread(250);
				cubeManipulatorInst.MoveCube(0);
				
				//second cube in switch
				driveInst.SetLeft(-0.5);
				driveInst.SetRight(-0.5);
				Utill.SleepThread(400);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				elevatorInst.setDistanceInches(24);
				cubeManipulatorInst.SetActuatorSetPoint(0.6);
				Utill.SleepThread(750);
				driveInst.SetLeft(0.5);
				driveInst.SetRight(0.5);
				Utill.SleepThread(450);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				cubeManipulatorInst.MoveCube(-0.7);
				Utill.SleepThread(500);
				cubeManipulatorInst.MoveCube(0);
				
				//pick up another cube by switch
				driveInst.SetLeft(-0.5);
				driveInst.SetRight(-0.5);
				Utill.SleepThread(650);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				elevatorInst.setDistancePercent(0);
				cubeManipulatorInst.SetActuatorSetPoint(0);
				driveInst.TurnDegrees(-30, 500);
				driveInst.SetLeft(0.7);
				driveInst.SetRight(0.7);
				Utill.SleepThread(650);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				driveInst.TurnDegrees(45, 1000);
				cubeManipulatorInst.MoveCube(1);
				driveInst.SetLeft(0.6);
				driveInst.SetRight(0.6);
				Utill.SleepThread(750);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				cubeManipulatorInst.MoveCube(0);
			}
			else //switch is right
			{	
				driveInst.DriveDistance(150);
			}
		}
		
	}
	
	/**
	 * Autonomous mode made for testing.
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive.
	 */
	public static void Test(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		driveInst.DriveDistanceBackwards(-20);
	}
	

	/**
	 *  Will get the sides of the field element to go to in autonomous based on 
	 * the game message. 
	 * 
	 * @return
	 * 		An array with two elements corresponding to the sides of the 
	 * 		switch and scale that match the alliance color.  
	 */
	private static FieldElementSide[] getFieldElementSideColor()
	{
		FieldElementSide[] fieldElements = new FieldElementSide[2];
		
		DriverStation driverStationInst = DriverStation.getInstance();
		String gameData = "";

		long timeBeforeLoop = System.currentTimeMillis();
		
		while (driverStationInst.getGameSpecificMessage() == null && driverStationInst.isAutonomous())
		{
			if (System.currentTimeMillis() - timeBeforeLoop > 5000)
			{
				// Idk if this should be a default 
				//PickAutonomousMode(AutonomousColor.BLUE, ManipulatorInst, elevatorInst, driveInst);
			}
			
			Utill.SleepThread(1);
		}
		        
		while (gameData.length() == 0)
		{
			gameData = driverStationInst.getGameSpecificMessage();

			Utill.SleepThread(1);
		}

		if (gameData.charAt(0) == 'R')
		{
			fieldElements[0] = FieldElementSide.RIGHT;
		}
		else if (gameData.charAt(0) == 'L')
		{
			fieldElements[0] = FieldElementSide.LEFT;
		}

		if (gameData.charAt(1) == 'R')
		{
			fieldElements[1] = FieldElementSide.RIGHT;
		}
		else if (gameData.charAt(0) == 'L')
		{
			fieldElements[1] = FieldElementSide.LEFT;
		}
		
		return fieldElements;
	}
}

