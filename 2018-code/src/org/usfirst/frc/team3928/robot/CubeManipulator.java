package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CubeManipulator implements Printer, PIDSource, PIDOutput
{
	/**
	 * Three states for the cube manipulator, intaking, 
	 * outtaking, and off.   
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
	 * The motor controller for moving the intake up and 
	 * down. 
	 */
	private TalonSRX IntakeActuationMotor;

	/**
	 * The absolute encoder attached to the intake.
	 */
	private AnalogPotentiometer IntakeEncoder;

	/**
	 * The PID controller for the intake. 
	 */
	private PIDController IntakePIDController;
	
	/**
	 * The first time the current on the motor is above 55 amps. 
	 */
	private long FirstTimeOverThreshold;
	
	/**
	 * True when the cube has been taken into the robot 
	 * and the motors have stopped. 
	 */
	private boolean stoppedIntaking;
	
	/**
	 * The constructor for a cube manipulator. 
	 */
	public CubeManipulator()
	{
		IntakeMotor = new TalonSRX(Constants.INTAKE_MOTOR_1); 
		IntakeActuationMotor = new TalonSRX(Constants.INTAKE_ACTUATE_MOTOR);
		
		IntakeEncoder = new AnalogPotentiometer(Constants.INTAKE_ENCODER_CHANNEL, Constants.INTAKE_ENCODER_RANGE, 
				Constants.INTAKE_ENCODER_OFFSET);
		
		IntakePIDController = new PIDController(Constants.INTAKE_P_VALUE, Constants.INTAKE_I_VALUE, Constants.INTAKE_D_VALUE, 0.0, this, this);
		IntakePIDController.setAbsoluteTolerance(Constants.INTAKE_ABSOLUTE_VALUE_TOLERANCE);
		IntakePIDController.setInputRange(Constants.INTAKE_PID_INPUT_RANGE_MIN, Constants.INTAKE_PID_INPUT_RANGE_MAX);
		IntakePIDController.setOutputRange(Constants.INTAKE_PID_OUTPUT_RANGE_MIN, Constants.INTAKE_PID_OUTPUT_RANGE_MAX);
		IntakePIDController.enable();
		
		stoppedIntaking = false;
		FirstTimeOverThreshold = 0;
		
		new ValuePrinter(this);
	}
	
	/**
	 * Sets value from -1 to 1
	 * 
	 * @param position
	 */
	public void SetActuatorSetPoint(double position)
	{
		double topPoint = Constants.ELEVATOR_ENCODER_MAX; 
		double lowPoint = Constants.ELEVATOR_ENCODER_MIN; 

		double m = (topPoint - lowPoint) / 2;
		double b = topPoint - (1 * m); 

		IntakePIDController.setSetpoint(position * m + b);
	}
	
	public void EnableCubeManipulatorPIDController(boolean isEnabled)
	{
		if(isEnabled)
		{
			IntakePIDController.enable();
		}
		else
		{
			IntakePIDController.disable();
		}
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
	
	public void ActuationMotorSetPower(double power)
	{
		IntakeActuationMotor.set(ControlMode.PercentOutput, power);
	}

	@Override
	public void PrintValues() 
	{
		SmartDashboard.putNumber("Intake motor current: ", IntakeMotor.getOutputCurrent());
		SmartDashboard.putNumber("Intake encoder: ", IntakeEncoder.get());
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource)
	{
		System.out.println("This is the PIDSourceType: " + pidSource.toString());
	}

	@Override
	public PIDSourceType getPIDSourceType()
	{
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet()
	{
		//for if the encoder breaks 180
//		double value;
//		if (IntakeEncoder.get() >= 0)
//		{
//			value = 180 - IntakeEncoder.get();
//		}
//		else
//		{
//			value = -180 - IntakeEncoder.get();
//		}
//		return value;

		return IntakeEncoder.get();
						
	}

	@Override
	public void pidWrite(double output)
	{
		// TODO make sure motor output is correct with the encoder, is sometimes negative but wedk exactly when
		IntakeActuationMotor.set(ControlMode.PercentOutput, -output);
		
	}
}
