package org.usfirst.frc.team3928.robot;

/**
 * This is a utillity class for easier thread sleep usage.
 * 
 * @author NicoleEssner
 *
 */
public class Utill 
{
	/**
	 * This function will sleep the tread that is running.
	 * 
	 * @param milliseconds
	 * 		The number of milliseconds that the thread is 
	 * 		going to sleep for. 
	 */
	public static void SleepThread(long milliseconds)
	{
		try 
		{
			Thread.sleep(milliseconds);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
