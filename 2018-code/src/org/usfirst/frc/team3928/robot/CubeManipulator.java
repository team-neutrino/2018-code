package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class to control cube manipulator and related information.
 * @author JoelNeppel
 *
 */
public class CubeManipulator implements Printer, PIDSource, PIDOutput
{	
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
	 * Solenoid to open intake arms.
	 */
	public static Solenoid IntakeOpen;
	
	/**
	 * Solenoid to close intake arms.
	 */
	public static Solenoid IntakeClose;
	
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
		
		IntakeOpen = new Solenoid(Constants.INTAKE_ACTUATION_SOLENOID_OPEN);
		IntakeClose = new Solenoid(Constants.INTAKE_ACTUATION_SOLENOID_CLOSED);
		
		stoppedIntaking = false;
		FirstTimeOverThreshold = 0;
		SetActuatorSetPoint(0);
		
		new ValuePrinter(this);
	}
	
	/**
	 * Sets cube mapipulator actuator point from value between -1 and 1
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
	
	/**
	 * Enables cube manipulator PID controller if true, disables if false.
	 * @param isEnabled
	 * 	True to enable cube manipulator PID, false to disble
	 */
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
	
	/**
	 * Controls cube manipulator wheels to intake and outtake cube.
	 * @param motorPower
	 * 	The power to set the motors to
	 */
	public void MoveCube(double motorPower)
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
	
	/**
	 * Sets actuation motor power for cube manipulator.
	 * @param power
	 * 	Power to set motor to
	 */
	public void ActuationMotorSetPower(double power)
	{
		IntakeActuationMotor.set(ControlMode.PercentOutput, power);

	}
	
	/**
	 * Sets pneumatics to open or close cube manipulator arms.
	 * @param open
	 * 	True to open arms, false to close
	 */
	public void ArmPosition(boolean open)
	{
		if (open)
		{
			IntakeOpen.set(true);
			IntakeClose.set(false);
		}
		else
		{
			IntakeOpen.set(false);
			IntakeClose.set(true);
		}
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
		
		
//		double value;
//		
//		if (IntakeEncoder.get() >= 0)
//		{
//			value = -360 + IntakeEncoder.get();
//		}
//		else
//		{
//			value = IntakeEncoder.get();
//		}
//
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
