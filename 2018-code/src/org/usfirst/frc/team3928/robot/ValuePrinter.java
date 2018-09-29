package org.usfirst.frc.team3928.robot;

/**
 * Class used to start a thread to print values
 * at one second intervals.
 * @author NicoleEssner
 *
 */
public class ValuePrinter
{
	/**
	 * The time that the values were last printed.
	 */
	private long lastTimePrinted;

	/**
	 * Starts new thread to print values every second.
	 * @param valuesToPrint
	 * 	The values to be printed
	 */
	public ValuePrinter(Printer valuesToPrint)
	{
		new Thread(
				new Runnable() 
				{
					public void run() 
					{
						while (true)
						{
							if (System.currentTimeMillis() - lastTimePrinted > 1000)
							{
								valuesToPrint.PrintValues();
								lastTimePrinted = System.currentTimeMillis();
							}
							
							Utill.SleepThread(1);
						}
					}
				}
		).start();
	}
}
