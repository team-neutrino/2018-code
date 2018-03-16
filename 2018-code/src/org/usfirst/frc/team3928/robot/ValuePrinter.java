package org.usfirst.frc.team3928.robot;

public class ValuePrinter
{
	private long lastTimePrinted;

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
