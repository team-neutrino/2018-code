package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CubeManipulator implements Printer
{
	/**
	 * Three states for the cube manipulator, intakeing, 
	 * outtakeing, and off.   
	 * 
	 * @author NicoleEssner
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
	private TalonSRX IntakeMotor;
	
	/**
	 * The first time the current on the motor is above
	 * 55 amps. 
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
		IntakeMotor = new TalonSRX(Constants.INTAKE_MOTOR_1); 
		
		stoppedIntaking = false;
		FirstTimeOverThreshold = 0;
		
		new ValuePrinter(this);
	}
	
	public void MoveCube(IntakeState isIntaking)
	{
		if(isIntaking == IntakeState.OFF)
		{
			MoveCube(0);
		}
		else if(isIntaking == IntakeState.INTAKE)
		{
			MoveCube(Constants.CUBE_MANIPULATOR_MOTOR_POWER);
		}
		else
		{
			MoveCube(-Constants.CUBE_MANIPULATOR_MOTOR_POWER);
		}
	}
	
	/**
	 * Will make the cube manipulator intake, outtake, or 
	 * turn off the motor.
	 * 
	 * @param state
	 * 		An enum that is either INTAKE, OUTTAKE, or 
	 * 		OFF.
	 */
	public void MoveCube(double motorPower) //IntakeState state
	{
//		double motorPower = 0;
//		
//		if (state == IntakeState.INTAKE)
//		{
//			motorPower = Constants.CUBE_MANIPULATOR_MOTOR_POWER; 
//		}
//		else if (state == IntakeState.OUTTAKE)
//		{
//			motorPower = -Constants.CUBE_MANIPULATOR_MOTOR_POWER; 
//		}
		if(Math.abs(motorPower) < 0.1)
		{
			motorPower = 0;
			FirstTimeOverThreshold = 0;
			stoppedIntaking = false;
		}
		
		if (IntakeMotor.getOutputCurrent() > Constants.STALLED_MOTOR_CURRENT && FirstTimeOverThreshold == 0) 
		{
			FirstTimeOverThreshold = System.currentTimeMillis();
		}
		else if (IntakeMotor.getOutputCurrent() < Constants.STALLED_MOTOR_CURRENT && stoppedIntaking == false) 
		{
			FirstTimeOverThreshold = 0;
		}
		
		if ((FirstTimeOverThreshold != 0) && (System.currentTimeMillis() - FirstTimeOverThreshold > Constants.CURRENT_OVER_THRESHOLD_TIME_MILLIS)) 
		{
			stoppedIntaking = true;
			motorPower = 0;
		}
		
		IntakeMotor.set(ControlMode.PercentOutput, motorPower);	
	}

	@Override
	public void PrintValues() 
	{
		SmartDashboard.putNumber("Intake motor current: ", IntakeMotor.getOutputCurrent());
	}
}
