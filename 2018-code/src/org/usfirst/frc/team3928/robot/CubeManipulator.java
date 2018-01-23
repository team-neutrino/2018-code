package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class CubeManipulator 
{
	/**
	 * Three states for the cube manipulator, intakeing, 
	 * outtakeing, and off.   
	 * 
	 * @author NicoleEssner
	 *
	 */
	public enum IntakeState
	{
		INTAKE,
		OUTTAKE,
		OFF
	}
	/**
	 * The first motor on the cube manipulator.
	 */
	private TalonSRX IntakeMotor1;
	
	/**
	 * The second motor on the cube manipulator. 
	 */
	private TalonSRX IntakeMotor2;
	
	/**
	 * The first time the current on the motor is above
	 * 10 amps. 
	 */
	private long FirstTimeOverThreshold;
	
	/**
	 * True when the cube had been taken into the robot 
	 * and the motors have stopped. 
	 */
	private boolean stoppedIntaking;
	
	/**
	 * The constructor for a cube manipulator. 
	 */
	public CubeManipulator()
	{
		IntakeMotor1 = new TalonSRX(8); // make constant
		IntakeMotor2 = new TalonSRX(9); // make constant
		
		stoppedIntaking = false;
		FirstTimeOverThreshold = 0;
	}
	
	/**
	 * Will make the cube manipulator intake, outtake, or 
	 * turn off the motor.
	 * 
	 * @param state
	 * 		An enum that is either INTAKE, OUTTAKE, or 
	 * 		OFF.
	 */
	public void MoveCube(IntakeState state)
	{
		double motorPower = 0;
		
		if (state == IntakeState.INTAKE)
		{
			motorPower = 1; // make constant
		}
		else if (state == IntakeState.OUTTAKE)
		{
			motorPower = -1; // make constant
		}
		else
		{
			motorPower = 0;
			FirstTimeOverThreshold = 0;
			stoppedIntaking = false;
		}
		
		if (IntakeMotor1.getOutputCurrent() > 10 && FirstTimeOverThreshold == 0) // make constant
		{
			FirstTimeOverThreshold = System.currentTimeMillis();
		}
		else if (IntakeMotor1.getOutputCurrent() < 10 && stoppedIntaking == false) // make constant
		{
			FirstTimeOverThreshold = 0;
		}
		
		if ((FirstTimeOverThreshold != 0) && (System.currentTimeMillis() - FirstTimeOverThreshold > 500)) // make constant
		{
			stoppedIntaking = true;
			motorPower = 0;
		}
		
		if (Robot.NumTimesThroughLoop % 10 == 0) // make constant
		{
			//System.out.println("Current: " + IntakeMotor1.getOutputCurrent());
		}
		
		IntakeMotor1.set(ControlMode.PercentOutput, motorPower);
		IntakeMotor2.set(ControlMode.PercentOutput, motorPower);
	}
}
