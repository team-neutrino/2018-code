package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * This class is for the information associated with the 
 * elevator.
 * 
 * @author NicoleEssner
 *
 */
public class Elevator implements Runnable, PIDSource, PIDOutput
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

	private AnalogPotentiometer IntakeEncoder;

	private PIDController IntakePIDController;

	private TalonSRX IntakeMotor;

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

		ElevatorPIDController = new PIDController(Constants.ELEVATOR_P_VALUE, Constants.ELEVATOR_I_VALUE, Constants.ELEVATOR_D_VALUE, 0, 
				this, this); 
		ElevatorPIDController.setAbsoluteTolerance(Constants.ELEVATOR_ABSOLUTE_VALUE_TOLERANCE); 
		ElevatorPIDController.setInputRange(Constants.ELEVATOR_PID_INPUT_RANGE_MIN, Constants.ELEVATOR_PID_INPUT_RANGE_MAX); 
		ElevatorPIDController.setOutputRange(Constants.ELEVATOR_PID_OUTPUT_RANGE_MIN, Constants.ELEVATOR_PID_OUTPUT_RANGE_MAX); 

		ClimbUp = new Solenoid(Constants.ELEVATOR_CLIMBER_SOLENOID_OUT); 
		StopClimb = new Solenoid(Constants.ELEVATOR_CLIMBER_SOLENOID_IN); 

		IsElevatorMovingDown = false;

		IntakeMotor = new TalonSRX(9);
		IntakeEncoder = new AnalogPotentiometer(0, 360, -180);

		// TODO add values
		IntakePIDController = new PIDController(0.015, 0.0, 0.0, 0.0, 
				new PIDSource() 
		{
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) 
			{
				//System.out.println("This is the PIDSourceType: " + pidSource.toString());
			}

			@Override
			public double pidGet() 
			{


				double value;

				if (IntakeEncoder.get() >= 0)
				{
					value = 180 - IntakeEncoder.get();
				}
				else
				{
					value = -180 - IntakeEncoder.get();
				}

				//System.out.println("Intake encoder: " + value);

				return value;
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
				//System.out.println("motor output: " + output);
				IntakeMotor.set(ControlMode.PercentOutput, output);
			}
		});

		IntakePIDController.setAbsoluteTolerance(5);
		IntakePIDController.setInputRange(-180, 180);
		IntakePIDController.setOutputRange(-0.7, 0.7);
		IntakePIDController.enable();

		// TODO 
		//setIntake(0);
		new Thread(this).start();
	}

	/**
	 * Method that will move the intake down from the starting 
	 * position. 
	 * 
	 * @param isMovingOut
	 * 		True when the intake is out, false when it is in.
	 */
	public void setIntake(double position)
	{
		// TODO I think this method will get changed at this point 

		//System.out.println("Set point: " + position);

		// TODO

		IntakePIDController.enable();

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

			//			System.out.println("zeroing elevator");
			setMotorSpeed(-0.15);
			while (!getButtonState())
			{
				Utill.SleepThread(1);
			}

			//			System.out.println("left while loop");
			setMotorSpeed(0);
			ElevatorEncoder.reset();
			ElevatorPIDController.enable();

			//while (DriverStation.getInstance().isEnabled())
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

		position = (position * 79) - 45;

		IntakePIDController.setSetpoint(position);
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

	public void setUpClimb()
	{
		//System.out.println("Cset up to climb was called");
		ElevatorPIDController.setSetpoint(70);
		IntakePIDController.setSetpoint((-1 * 79) - 45);
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
		//System.out.println("This is the PIDSourceType: " + pidSource.toString());
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
}
