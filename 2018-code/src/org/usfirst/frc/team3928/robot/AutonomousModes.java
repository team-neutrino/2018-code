package org.usfirst.frc.team3928.robot;

import org.usfirst.frc.team3928.robot.CubeManipulator.IntakeState;

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
		BLUE, YELLOW, GREEN, ORANGE, PURPLE, TEST;
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
				Blue(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case YELLOW:
			{
				Yellow(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case GREEN:
			{
				Green(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case ORANGE:
			{
				Orange(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case PURPLE:
			{
				Purple(cubeManipulatorInst, elevatorInst, driveInst);
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
	 * Single cube dropped in the switch 
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
			driveInst.DriveDistance(85);
			elevatorInst.setDistanceInches(24);
			driveInst.TurnDegrees(turnDegreesSign * -60, 1000);
		}
		else 
		{
			elevatorInst.setDistanceInches(10);
			driveInst.DriveDistance(24);
			Utill.SleepThread(200);
			driveInst.TurnDegrees(turnDegreesSign * 45, 1000);
			driveInst.DriveDistance(65);
			elevatorInst.setDistanceInches(24);
			driveInst.TurnDegrees(turnDegreesSign * -45, 1000);
		}
		driveInst.DriveDistance(12);
		cubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
		Utill.SleepThread(500);
		cubeManipulatorInst.MoveCube(IntakeState.OFF);
	}

	/**
	 * Method that does something at some point in time.
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive.
	 * 
	 * TODO someone help me here?
	 */
	private static void Green(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Green");
		int turnDegreesSign = 1;
		if(getFieldElementSideColor()[0] == FieldElementSide.LEFT)
		{
			turnDegreesSign = -1;
		}
		elevatorInst.setDistanceInches(10);
		driveInst.DriveDistance(290); //324
		Utill.SleepThread(100);
		elevatorInst.setDistancePercent(1);
		driveInst.TurnDegrees(turnDegreesSign * -90, 1000);
		Utill.SleepThread(100);
		driveInst.DriveDistance(24);
		Utill.SleepThread(100);
		cubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
		Utill.SleepThread(1000);
		cubeManipulatorInst.MoveCube(IntakeState.OFF);
		driveInst.SetLeft(-.5);
		driveInst.SetRight(-.5);
		elevatorInst.setDistancePercent(0);
		Utill.SleepThread(600);
		driveInst.SetLeft(0);
		driveInst.SetRight(0);
		Utill.SleepThread(1000);;
		driveInst.TurnDegrees(-90, 1000);
		driveInst.DriveDistance(48);
		
	}

	/**
	 * Cube dropped in the switch and scale with robot starting 
	 * at position 3.
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
		
		if(colors[1] == FieldElementSide.RIGHT)
		{
			driveInst.DriveDistance(210);
			driveInst.TurnDegrees(-37, 1000);
			elevatorInst.setDistancePercent(100);
			Utill.SleepThread(100);
			driveInst.DriveDistance(43);
			cubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
			Utill.SleepThread(250);
			cubeManipulatorInst.MoveCube(IntakeState.OFF);
			elevatorInst.setDistancePercent(0);
			
			if(colors[0] == FieldElementSide.RIGHT)
			{	
				driveInst.TurnDegrees(-110, 3000);
				cubeManipulatorInst.MoveCube(IntakeState.INTAKE);
				driveInst.SetLeft(0.5);
				driveInst.SetRight(0.5);
				Utill.SleepThread(1900);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				cubeManipulatorInst.MoveCube(IntakeState.OFF);
				elevatorInst.setDistanceInches(30);
				Utill.SleepThread(1000);
				cubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
				Utill.SleepThread(1000);
				cubeManipulatorInst.MoveCube(IntakeState.OFF);
			}
			else
			{	
				driveInst.DriveDistanceBackwards(-47);
				driveInst.TurnDegrees(-53, 1000);
				driveInst.DriveDistance(110);
				driveInst.DriveDistance(50);
				cubeManipulatorInst.MoveCube(IntakeState.INTAKE);
				driveInst.TurnDegrees(-70, 1000);
				driveInst.SetLeft(0.5);
				driveInst.SetRight(0.5);
				Utill.SleepThread(1000);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				Utill.SleepThread(200);
				driveInst.SetLeft(-0.2);
				driveInst.SetRight(-0.2);
				Utill.SleepThread(500);
				driveInst.SetLeft(0);
				driveInst.SetLeft(0);
				cubeManipulatorInst.MoveCube(IntakeState.OFF);
				Utill.SleepThread(200);
				elevatorInst.setDistanceInches(30);
				Utill.SleepThread(1000);
				cubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
				Utill.SleepThread(500);
				cubeManipulatorInst.MoveCube(IntakeState.OFF);
			}		
		}
		else
		{			
			if(colors[0] == FieldElementSide.RIGHT)
			{
				
			}
			else
			{
				driveInst.DriveDistance(200);
				driveInst.TurnDegrees(-85, 1000);
				Utill.SleepThread(500);
				driveInst.DriveDistance(110);
				driveInst.DriveDistance(130);
				driveInst.TurnDegrees(100, 1500);
				elevatorInst.setDistancePercent(100);
				Utill.SleepThread(100);
				driveInst.DriveDistance(24);
				cubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
				Utill.SleepThread(250);
				cubeManipulatorInst.MoveCube(IntakeState.OFF);
				elevatorInst.setDistancePercent(0);
				driveInst.TurnDegrees(130, 3000);
				cubeManipulatorInst.MoveCube(IntakeState.INTAKE);
				driveInst.SetLeft(0.8);
				driveInst.SetRight(0.8);
				Utill.SleepThread(800);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				Utill.SleepThread(100);
				driveInst.SetLeft(-0.5);
				driveInst.SetRight(-0.5);
				Utill.SleepThread(200);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				cubeManipulatorInst.MoveCube(IntakeState.OFF);
				elevatorInst.setDistanceInches(30);
				Utill.SleepThread(800);
				cubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
				Utill.SleepThread(250);
				cubeManipulatorInst.MoveCube(IntakeState.OFF);
			}
		}
		
	}
	
	/**
	 * Cube dropped in the switch and scale with robot starting 
	 * at position 1.
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
				Utill.SleepThread(500);
				driveInst.DriveDistance(80);
				driveInst.DriveDistance(155);
				driveInst.TurnDegrees(-110, 1500);
				elevatorInst.setDistancePercent(100);
				Utill.SleepThread(100);
				driveInst.DriveDistance(42);
				cubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
				Utill.SleepThread(250);
				cubeManipulatorInst.MoveCube(IntakeState.OFF);
				elevatorInst.setDistancePercent(0);
				driveInst.DriveDistanceBackwards(80);
				
			
				driveInst.TurnDegrees(-130, 3000);
				cubeManipulatorInst.MoveCube(IntakeState.INTAKE);
				driveInst.SetLeft(0.7);
				driveInst.SetRight(0.7);
				Utill.SleepThread(1400);
				driveInst.SetLeft(0);
				driveInst.SetRight(0);
				Utill.SleepThread(500);
				cubeManipulatorInst.MoveCube(IntakeState.OFF);
				elevatorInst.setDistanceInches(30);
				Utill.SleepThread(1500);
				cubeManipulatorInst.MoveCube(IntakeState.OUTTAKE);
				Utill.SleepThread(1000);
				cubeManipulatorInst.MoveCube(IntakeState.OFF);
				
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
	 * Autonomous mode make for testing.
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
		driveInst.TurnDegrees(90, 1000);
		Utill.SleepThread(3000);
		driveInst.TurnDegrees(-90, 1000);
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

