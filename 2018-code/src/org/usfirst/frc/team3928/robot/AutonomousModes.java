package org.usfirst.frc.team3928.robot;

/**
 * Class the holds all the autonomous methods.
 * 
 * @author NicoleEssner
 *
 */
public class AutonomousModes 
{
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
	public static void DriveForward(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
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
	public static void SwitchRight(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
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
	public static void SwitchLeft(CubeManipulator cubeManipulatorInst, Elevator elevatorInst, Drive driveInst)
	{
		elevatorInst.setDistanceInches(30);
		driveInst.DriveDistance(140);
		Utill.SleepThread(1000);
		driveInst.TurnDegrees(90);
		Utill.SleepThread(1000);
		driveInst.DriveDistance(40);
	}
}
