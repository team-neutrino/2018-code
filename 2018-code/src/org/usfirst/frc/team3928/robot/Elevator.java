package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is for the information associated with the 
 * elevator.
 * 
 * @author NicoleEssner
 *
 */
public class Elevator implements Runnable, PIDSource, PIDOutput, Printer
{
	/**
	 * The encoder that is attached to the elevator mechanism.  
	 */
	private Encoder ElevatorEncoder;

	/**
	 * The first speed controller that is for the elevator motor.  
	 */
	private TalonSRX ElevatorMotor1;

	/**
	 * The second speed controller that is for the elevator motor.  
	 */
	private TalonSRX ElevatorMotor2;

	/**
	 * The third speed controller that is for the elevator motor.  
	 */
	private TalonSRX ElevatorMotor3;

	/**
	 * The fourth speed controller that is for the elevator motor.  
	 */
	private TalonSRX ElevatorMotor4;

	/**
	 * The sensor, arcade button, at the bottom of the elevator to know when it 
	 * is at its lowest point.
	 */
	private DigitalInput LiftButton;

	/**
	 * The PIDController for using the elevator encoder to raise the elevator 
	 * a distance. 
	 */
	private PIDController ElevatorPIDController;

	/**
	 * Actuation to trigger the climber. 
	 */
	private Solenoid ClimbUp;

	/**
	 * Actuation to stop triggering the climber.
	 */
	private Solenoid StopClimb;

	/**
	 * Weather the elevator is moving down with the climber. 
	 */
	private boolean IsElevatorMovingDown;

	/**
	 * The motor controller for moving the intake up and 
	 * down. 
	 */
	private TalonSRX IntakeMotor;
	
	/**
	 * The absolute encoder attached to the intake.
	 */
	private AnalogPotentiometer IntakeEncoder;

	/**
	 * The PID controler for the intake. 
	 */
	private PIDController IntakePIDController;

	/**
	 * Constructor for the elevator object.
	 */
	public Elevator()
	{
		ElevatorEncoder = new Encoder(Constants.ELEVATOR_ENCODER_POWER_CHANNEL, Constants.ELEVATOR_ENCODER_DATA_CHANNEL); 
		ElevatorEncoder.reset();
		ElevatorEncoder.setDistancePerPulse(Constants.ELEVATOR_DISTANCE_TRAVELED_PER_ROTATION/Constants.ENCODER_COUNTS_PER_ROTATION); 

		ElevatorMotor1 = new TalonSRX(Constants.ELEVATOR_MOTOR_1); 
		ElevatorMotor2 = new TalonSRX(Constants.ELEVATOR_MOTOR_2); 
		ElevatorMotor3 = new TalonSRX(Constants.ELEVATOR_MOTOR_3); 
		ElevatorMotor4 = new TalonSRX(Constants.ELEVATOR_MOTOR_4); 

		LiftButton = new DigitalInput(Constants.ELEVATOR_BUTTON);

		ElevatorPIDController = new PIDController(Constants.ELEVATOR_P_VALUE, Constants.ELEVATOR_I_VALUE, Constants.ELEVATOR_D_VALUE, 0.0, 
				this, this); 
		ElevatorPIDController.setAbsoluteTolerance(Constants.ELEVATOR_ABSOLUTE_VALUE_TOLERANCE); 
		ElevatorPIDController.setInputRange(Constants.ELEVATOR_PID_INPUT_RANGE_MIN, Constants.ELEVATOR_PID_INPUT_RANGE_MAX); 
		ElevatorPIDController.setOutputRange(Constants.ELEVATOR_PID_OUTPUT_RANGE_MIN, Constants.ELEVATOR_PID_OUTPUT_RANGE_MAX); 

		ClimbUp = new Solenoid(Constants.ELEVATOR_CLIMBER_SOLENOID_OUT); 
		StopClimb = new Solenoid(Constants.ELEVATOR_CLIMBER_SOLENOID_IN); 

		IsElevatorMovingDown = false;

		IntakeMotor = new TalonSRX(Constants.INTAKE_ACTUATE_MOTOR);
		IntakeEncoder = new AnalogPotentiometer(Constants.INTAKE_ENCODER_CHANNEL, Constants.INTAKE_ENCODER_RANGE, 
		Constants.INTAKE_ENCODER_OFFSET);
		IntakePIDController = new PIDController(Constants.INTAKE_P_VALUE, Constants.INTAKE_I_VALUE, Constants.INTAKE_D_VALUE, 0.0, 
		new PIDSource() 
		{
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) 
			{
				System.out.println("This is the PIDSourceType: " + pidSource.toString());
			}

			@Override
			public double pidGet() 
			{
//				double value;
//
//				if (IntakeEncoder.get() >= 0)
//				{
//					value = 180 - IntakeEncoder.get();
//				}
//				else
//				{
//					value = -180 - IntakeEncoder.get();
//				}
				
				return IntakeEncoder.get();
			}

			@Override
			public PIDSourceType getPIDSourceType() 
			{
				return PIDSourceType.kDisplacement;
			}
		}
		, 
		new PIDOutput() 
		{
			@Override
			public void pidWrite(double output) 
			{
				IntakeMotor.set(ControlMode.PercentOutput, -output);
			}
		});
		IntakePIDController.setAbsoluteTolerance(Constants.INTAKE_ABSOLUTE_VALUE_TOLERANCE);
		IntakePIDController.setInputRange(Constants.INTAKE_PID_INPUT_RANGE_MIN, Constants.INTAKE_PID_INPUT_RANGE_MAX);
		IntakePIDController.setOutputRange(Constants.INTAKE_PID_OUTPUT_RANGE_MIN, Constants.INTAKE_PID_OUTPUT_RANGE_MAX);
		IntakePIDController.enable();

		new ValuePrinter(this);
		
		new Thread(this).start();
	}

	/**
	 * Returns the state of the button. Is true when the button is being 
	 * pushed and false when the button is not pushed. 
	 * 
	 * @return
	 * 		The current state that the button is in. 
	 */
	private boolean getButtonState()
	{	
		return !LiftButton.get();
	}

	/**
	 * Will set the elevator motors to the given speed.
	 * This is a value between -1 and 1.
	 * 
	 * @param speed
	 * 		The value to set the elevator motors to. 
	 */
	private void setMotorSpeed(double speed)
	{
		if (getButtonState() && (speed < 0 ))
		{
			speed = 0;
		}

		ElevatorMotor1.set(ControlMode.PercentOutput, speed);
		ElevatorMotor2.set(ControlMode.PercentOutput, speed);
		ElevatorMotor3.set(ControlMode.PercentOutput, speed);
		ElevatorMotor4.set(ControlMode.PercentOutput, speed);
	}

	/**
	 * Is called once the robot is enabled. Will zero the elevator 
	 * to get the correct encoder value. 
	 */
	private void zeroElevator()
	{
		while (true)
		{
			ElevatorPIDController.disable();

			setMotorSpeed(-0.15);
			while (!getButtonState())
			{
				Utill.SleepThread(1);
			}

			setMotorSpeed(0);
			ElevatorEncoder.reset();
			ElevatorPIDController.enable();

			//while (DriverStation.getInstance().isEnabled())
			// This is if we want to go back to zeroing every time the robot is 
			// enabled.
			while(true)
			{
				Utill.SleepThread(1);
			}
		}
	}

	/**
	 * Will set the elevator to a distance in inches. Should this be made private?
	 * 
	 * @param distance
	 * 		The distance to move the elevator to. 
	 */
	public void setDistanceInches(double distance)
	{
		ElevatorPIDController.setSetpoint(distance);

		double position;

		if (distance < 12)
		{
			position = 0.0;
		}
		else 
		{
			position = 0.6;
		}
		
		IntakePIDController.setSetpoint(getIntakeSetpoint(position));
	}

	/**
	 * Will set the elevator to a position based on a percent. 
	 * 
	 * @param percent
	 * 		The percent the elevator position should be. 
	 */
	public void setDistancePercent(double percent)
	{
		double distance = percent * 70;
		setDistanceInches(distance);
	}

	/**
	 * Triggers the actuation that causes the climber to move,
	 * this also disables the PID as soon as the input is true.
	 * 
	 * @param isClimbing
	 * 		True when the climber is being triggered, false 
	 * 		when not. 
	 */
	public void Climb(boolean isClimbing)
	{
		if (isClimbing)
		{
			IsElevatorMovingDown = true;

			ElevatorPIDController.disable();
			IntakePIDController.disable();

			ElevatorMotor1.set(ControlMode.PercentOutput, -0.4);
			ElevatorMotor2.set(ControlMode.PercentOutput, -0.4);
			ElevatorMotor3.set(ControlMode.PercentOutput, -0.4);
			ElevatorMotor4.set(ControlMode.PercentOutput, -0.4);
		}
		else if (!isClimbing && IsElevatorMovingDown)	
		{
			IsElevatorMovingDown = false;

			ElevatorMotor1.set(ControlMode.PercentOutput, 0.0);
			ElevatorMotor2.set(ControlMode.PercentOutput, 0.0);
			ElevatorMotor3.set(ControlMode.PercentOutput, 0.0);
			ElevatorMotor4.set(ControlMode.PercentOutput, 0.0);
		}

		ClimbUp.set(!isClimbing);
		StopClimb.set(isClimbing);
	}

	/**
	 * Method that sets the intake all the way down and the elevator 
	 * all the way up.
	 */
	public void setUpClimb()
	{
		ElevatorPIDController.setSetpoint(70);
		IntakePIDController.setSetpoint(getIntakeSetpoint(-1));
	}

	/**
	 * Will get the value corresponding to the intake position 
	 * for the range -1 to 1.
	 * 
	 * @param position
	 * 		A value from -1 to 1
	 * @return
	 * 		The position the intake should be set to. 
	 */
	private double getIntakeSetpoint(double position)
	{
		double topPoint = -113; 
		double lowPoint = 35; 
		
		double m = (topPoint - lowPoint) / 2;
		double b = topPoint - (1 * m); 
		
		return (position * m) + b;
	}
	
	@Override
	public void run() 
	{
		zeroElevator();	
	}

	@Override
	public void pidWrite(double output) 
	{
		setMotorSpeed(output);
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
		return ElevatorEncoder.getDistance();
	}

	@Override
	public void PrintValues() 
	{
		SmartDashboard.putNumber("Elevator encoder: ", ElevatorEncoder.getDistance());
		
//		double value;
//
//		if (IntakeEncoder.get() >= 0)
//		{
//			value = 180 - IntakeEncoder.get();
//		}
//		else
//		{
//			value = -180 - IntakeEncoder.get();
//		}
		
		SmartDashboard.putNumber("Intake encoder: ", IntakeEncoder.get());
	}
}
