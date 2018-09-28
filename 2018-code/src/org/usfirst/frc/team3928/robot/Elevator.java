package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
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
	public void setMotorSpeed(double speed)
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
				Utill.SleepThread(1000);
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
	}

	/**
	 * Will set the elevator to a position based on a percent. 
	 * 
	 * @param percent
	 * 		The percent the elevator position should be. 
	 */
	public void setDistancePercent(double percent)
	{
		double distance = percent * Constants.ELEVATOR_PID_INPUT_RANGE_MAX;
		setDistanceInches(distance);
	}
	
	/**
	 * Enables or disables elevator PID controller.
	 * @param isEnabled
	 * 	Enables PID controller if true, disables if false
	 */
	public void EnableElevatorPIDController(boolean isEnabled)
	{
		if(isEnabled)
		{
			ElevatorPIDController.enable();
		}
		else
		{
			ElevatorPIDController.disable();
		}
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
	}
}
