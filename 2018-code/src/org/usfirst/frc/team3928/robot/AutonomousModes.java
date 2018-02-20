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
	private enum FieldElementColorSide
	{
		LEFT, RIGHT;
	}
	
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
				DriveForward(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case 2:
			{
				SwitchRight(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			case 3:
			{
				SwitchLeft(cubeManipulatorInst, elevatorInst, driveInst);
				break;
			}
			default:
			{
				DriveForward(cubeManipulatorInst, elevatorInst, driveInst);
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
	private static void DriveForward(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		driveInst.DriveDistance(140);
	}
	
	/**
	 * Will put the cube in the right side of the switch. 
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive. 
	 */
	private static void SwitchRight(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		elevatorInst.setDistanceInches(30);
		driveInst.DriveDistance(140);
		Utill.SleepThread(1000);
		driveInst.TurnDegrees(-90);
		Utill.SleepThread(1000);
		driveInst.DriveDistance(40);
	}
	
	/**
	 * Will put the cube in the left side of the switch.
	 * 
	 * @param cubeManipulatorInst
	 * 		An instance of the cube manipulator. 
	 * @param elevatorInst
	 * 		An instance of the elevator. 
	 * @param driveInst
	 * 		An instance of the drive. 
	 */
	private static void SwitchLeft(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		elevatorInst.setDistanceInches(30);
		driveInst.DriveDistance(140);
		Utill.SleepThread(1000);
		driveInst.TurnDegrees(90);
		Utill.SleepThread(1000);
		driveInst.DriveDistance(40);
	}
	
	private static FieldElementColorSide getFieldElementSideColor(FieldElement fieldElement)
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
		
		if (sideColor == 'R')
		{
			return FieldElementColorSide.RIGHT;
		}
		else if (sideColor == 'L')
		{
			return FieldElementColorSide.LEFT;
		}
		else 
		{
			// Might want to throw and error here 
			return null;
		}
		
	}
}
