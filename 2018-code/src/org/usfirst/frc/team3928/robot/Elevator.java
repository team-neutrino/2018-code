package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Talon;

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
	private PIDController PIDControllerInst;

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

		PIDControllerInst = new PIDController(0.2, 0, 0, 0, this, this); // Make constant
		PIDControllerInst.setAbsoluteTolerance(0.5); // Make constant
		PIDControllerInst.setInputRange(1, 70); // Make constant
		PIDControllerInst.setOutputRange(-1, 1); // Make constant

		new Thread(this).start();
	}

	/**
	 * Returns the state of the button. Is true when the 
	 * button is being pushed and false when the 
	 * button is not pushed. 
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
			PIDControllerInst.disable();
			
//			System.out.println("zeroing elevator");
			setMotorSpeed(-0.3);
			while (!getButtonState())
			{
				Utill.SleepThread(1);
			}
			
//			System.out.println("left while loop");
			setMotorSpeed(0);
			ElevatorEncoder.reset();
			PIDControllerInst.enable();
			
			while (DriverStation.getInstance().isEnabled())
			{
				Utill.SleepThread(1);
			}
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

	/**
	 * Will set the elevator to a distance in inches. Should this be made private?
	 * 
	 * @param distance
	 * 		The distance to move the elevator to. 
	 */
	public void setDistanceInches(double distance)
	{
		PIDControllerInst.setSetpoint(distance);
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

	

}
