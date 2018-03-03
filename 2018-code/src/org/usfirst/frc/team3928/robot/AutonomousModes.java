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
	 * Which field element that the cube is going to be dropped in in auton. 
	 * 
	 * @author NicoleEssner
	 *
	 */
	private enum FieldElement
	{
		SWITCH, SCALE;
	}

	/**
	 * Will take in an integer value corresponding to the autonomous mode, also passed in the 
	 * objects needed to make the autonomous mode possible. 
	 * 
	 * @param autonomousMode
	 * 		The integer value of the autonomous mode that is going to be excuted. 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive. 
	 */
	public static void PickAutonomousMode(int autonomousMode, CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		switch (autonomousMode)
		{
			case 1:
			{
				Blue(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case 2:
			{
				Yellow(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case 3:
			{
				Orange(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case 4:
			{
				Green(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case 5:
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
	 * 
	 * TODO update the doc  
	 */
	private static void Blue(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Blue");
		Utill.SleepThread(7000);
		driveInst.DriveDistance(200);
		

	}

	/**
	 * 
	 * @param cubManipulatorInst
	 * @param elevatorInst
	 * @param driveInst
	 * 
	 * TODO update the doc 
	 */
	private static void Yellow(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Yellow");
		int turnDegreesSign = 1;
		if(getFieldElementSideColor(FieldElement.SWITCH) == FieldElementSide.LEFT)
		{
			turnDegreesSign = -1;
		}

		elevatorInst.setDistanceInches(10);
		driveInst.DriveDistance(24);
		Utill.SleepThread(200);
		driveInst.TurnDegrees(turnDegreesSign * 45);
		//Utill.SleepThread(100);
		driveInst.DriveDistance(72);
		//Utill.SleepThread(100);
		elevatorInst.setDistanceInches(24);
		driveInst.TurnDegrees(turnDegreesSign * -45);
		//Utill.SleepThread(100);
		driveInst.DriveDistance(12);
	}

	/**
	 * 
	 * @param cubeManipulatorInst
	 * @param elevatorInst
	 * @param driveInst
	 * 
	 * TODO update the doc
	 */
	private static void Orange(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Orange");
		int turnDegreesSign = 1;
		if(getFieldElementSideColor(FieldElement.SWITCH) == FieldElementSide.LEFT)
		{
			turnDegreesSign = -1;
		}
		elevatorInst.setDistanceInches(10);
		driveInst.DriveDistance(290); //324
		Utill.SleepThread(100);
		elevatorInst.setDistancePercent(1);
		driveInst.TurnDegrees(turnDegreesSign * -90);
		Utill.SleepThread(100);
		driveInst.DriveDistance(12);
		Utill.SleepThread(100);
		//eject cube
	}

	private static void Green(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		System.out.println("Green");
		elevatorInst.setDistanceInches(12);
		driveInst.DriveDistance(210);
		Utill.SleepThread(100);
		driveInst.TurnDegrees(-37);
		elevatorInst.setDistancePercent(100);
		Utill.SleepThread(100);
		driveInst.DriveDistance(43);
		//spit cube
		elevatorInst.setDistancePercent(0);
		driveInst.TurnDegrees(-120);
		driveInst.DriveDistance(50);
		//nom nom cube
		elevatorInst.setDistanceInches(30);
		driveInst.DriveDistance(20);
		//pew pew cube
	}
	/**
	 * Will get the side of the field element to go to in auton based on 
	 * the game message. 
	 * 
	 * @param fieldElement
	 * 		The field element that the auton mode will go to. 
	 * @return
	 * 		The side of the field element that matches the alliance color. 
	 */
	private static FieldElementSide getFieldElementSideColor(FieldElement fieldElement)
	{
		DriverStation driverStationInst = DriverStation.getInstance();
		String gameData = "";
		char sideColor = ' ';

		while (gameData.length() == 0)
		{
			gameData = driverStationInst.getGameSpecificMessage();

			Utill.SleepThread(1);
		}

		if (fieldElement == FieldElement.SWITCH)
		{
			sideColor = gameData.charAt(0);
		}
		else if (fieldElement == FieldElement.SCALE)
		{
			sideColor = gameData.charAt(1);
		}

		System.out.println("character" + sideColor);

		if (sideColor == 'R')
		{
			return FieldElementSide.RIGHT;
		}
		else if (sideColor == 'L')
		{
			return FieldElementSide.LEFT;
		}
		else 
		{
			System.out.println("Null case happened");
			// Might want to throw and error here 
			return null;
		}

	}
	
	public static void Test(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		driveInst.TurnDegrees(90);
	}
}

