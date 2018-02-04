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
		IntakeMotor1 = new TalonSRX(Constants.INTAKE_MOTOR_1); 
		IntakeMotor2 = new TalonSRX(Constants.INTAKE_MOTOR_2); 
		
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
			motorPower = Constants.CUBE_MANIPULATOR_MOTOR_POWER; 
		}
		else if (state == IntakeState.OUTTAKE)
		{
			motorPower = -Constants.CUBE_MANIPULATOR_MOTOR_POWER; 
		}
		else
		{
			motorPower = 0;
			FirstTimeOverThreshold = 0;
			stoppedIntaking = false;
		}
		
		if (IntakeMotor1.getOutputCurrent() > Constants.STALLED_MOTOR_CURRENT && FirstTimeOverThreshold == 0) 
		{
			FirstTimeOverThreshold = System.currentTimeMillis();
		}
		else if (IntakeMotor1.getOutputCurrent() < Constants.STALLED_MOTOR_CURRENT && stoppedIntaking == false) 
		{
			FirstTimeOverThreshold = 0;
		}
		
		if ((FirstTimeOverThreshold != 0) && (System.currentTimeMillis() - FirstTimeOverThreshold > Constants.CURRENT_OVER_THRESHOLD_TIME_MILLIS)) 
		{
			stoppedIntaking = true;
			motorPower = 0;
		}
		
		if (Robot.NumTimesThroughLoop % Constants.PRINT_SPEED_DIVIDER == 0) 
		{
			//System.out.println("Current: " + IntakeMotor1.getOutputCurrent());
		}
		
		IntakeMotor1.set(ControlMode.PercentOutput, motorPower);
		IntakeMotor2.set(ControlMode.PercentOutput, motorPower);
	}
}
