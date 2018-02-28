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
	private enum FieldElementSide
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
				Yellow(cubeManipulatorInst, elevatorInst, driveInst);
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
	public static void Blue(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		try {
			Thread.sleep(7000);
			driveInst.DriveDistance(140, 0.6);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param cubManipulatorInst
	 * @param elevatorInst
	 * @param driveInst
	 */
	public static void Yellow(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		try
		{
			int turnDegreesSign = 1;
			if(getFieldElementSideColor(FieldElement.SWITCH) == FieldElementSide.LEFT)
			{
				turnDegreesSign = -1;
			}
			
			elevatorInst.setDistanceInches(10);
			driveInst.DriveDistance(24, 0.6);
			Thread.sleep(200);
			driveInst.TurnDegrees(turnDegreesSign * 45);
			Thread.sleep(100);
			driveInst.DriveDistance(72, 0.6);
			Thread.sleep(100);
			elevatorInst.setDistanceInches(24);
			driveInst.TurnDegrees(turnDegreesSign * -45);
			Thread.sleep(100);
			driveInst.DriveDistance(12, 0.6);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void Orange(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		try
		{
			elevatorInst.setDistanceInches(10);
			driveInst.DriveDistance(324, 0.6);
			Thread.sleep(2000);
			elevatorInst.setDistancePercent(1);
			driveInst.TurnDegrees(-90);
			Thread.sleep(2000);
			driveInst.DriveDistance(12, 0.3);
			Thread.sleep(2000);
			//eject cube
		}
		catch(InterruptedException e)
		{
			
		}
	}
	
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
}
