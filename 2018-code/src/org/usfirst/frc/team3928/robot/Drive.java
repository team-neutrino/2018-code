package org.usfirst.frc.team3928.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is for the information associated with the 
 * dive train.
 * 
 * @author NicoleEssner
 *
 */
public class Drive implements PIDSource, PIDOutput 
{
	/**
	 * The first speed controller on the right side of drive 
	 * train.  
	 */
	private TalonSRX RightMotor1;

	/**
	 * The second speed controller on the right side of drive 
	 * train.   
	 */
	private TalonSRX RightMotor2;

	/**
	 * The first speed controller on the left side of drive 
	 * train.  
	 */
	private TalonSRX LeftMotor1;

	/**
	 * The second speed controller on the left side of drive 
	 * train.  
	 */
	private TalonSRX LeftMotor2;

	/**
	 * The encoder for the right side of the drive train. 
	 */
	private Encoder RightEncoder;

	/**
	 * The encoder for the left side of the drive train. 
	 */
	private Encoder LeftEncoder;

	/**
	 * The gyroscope and accelerometer board. 
	 */
	private AHRS Navx;

	/**
	 * The PIDController for using the navx to turn a number 
	 * of degrees. 
	 */
	private PIDController TurnDegreesPIDController;

	/**
	 * The time that the PID Loop for turning degrees 
	 * has been executing. 
	 */
	private long TurnDegreesTimeInPID;

	/**
	 * Constructor for the Drive class. 
	 */
	public Drive()
	{
		RightMotor1 = new TalonSRX(Constants.DRIVE_RIGHT_1_CHANNEL); 
		RightMotor2 = new TalonSRX(Constants.DRIVE_RIGHT_2_CHANNEL); 
		LeftMotor1 = new TalonSRX(Constants.DRIVE_LEFT_1_CHANNEL); 
		LeftMotor2 = new TalonSRX(Constants.DRIVE_LEFT_2_CHANNEL); 

		RightEncoder = new Encoder(Constants.DRIVE_RIGHT_ENCODER_POWER_CHANNEL, Constants.DRIVE_RIGHT_ENCODER_DATA_CHANNEL); 
		LeftEncoder = new Encoder(Constants.DRIVE_LEFT_ENCODER_POWER_CHANNEL, Constants.DRIVE_LEFT_ENCODER_DATA_CHANNEL); 

		RightEncoder.setDistancePerPulse(2 * Constants.DRIVE_WHEEL_RADUIS * Math.PI / Constants.ENCODER_COUNTS_PER_ROTATION);
		LeftEncoder.setDistancePerPulse(2 * Constants.DRIVE_WHEEL_RADUIS * Math.PI / Constants.ENCODER_COUNTS_PER_ROTATION);

		RightEncoder.reset();
		LeftEncoder.reset();

		Navx = new AHRS(SPI.Port.kMXP);
		Navx.setAngleAdjustment(Constants.DRIVE_NAVX_ANGLE_ADJUSTMENT); 

		TurnDegreesPIDController =  new PIDController(Constants.DRIVE_P_VALUE_DEGREE_TURN, Constants.DRIVE_I_VALUE_DEGREE_TURN, 
				Constants.DRIVE_D_VALUE_DEGREE_TURN, Navx, this); 
		TurnDegreesPIDController.setAbsoluteTolerance(Constants.DRIVE_ABSOLUTE_VALUE_TOLERANCE_DEGREE_TURN);
		TurnDegreesPIDController.setInputRange(Constants.DRIVE_PID_INPUT_RANGE_MIN_DEGREE_TURN, Constants.DRIVE_PID_INPUT_RANGE_MAX_DEGREE_TURN);
		TurnDegreesPIDController.setOutputRange(Constants.DRIVE_PID_OUTPUT_RANGE_MIN_DEGREE_TURN, Constants.DRIVE_PID_OUTPUT_RANGE_MAX_DEGREE_TURN);

		TurnDegreesTimeInPID = 0;

		//		CreateButtonsOnDashborad();	
	}

	/**
	 * This method sets the motor speed on the right side of the
	 * drive train. 
	 * 
	 * @param motorPower
	 * 		A double value between -1 and 1 to set the motor to. 
	 */
	public void SetRight(double motorPower)
	{
		RightMotor1.set(ControlMode.PercentOutput, -motorPower); 
		//RightMotor2.set(ControlMode.PercentOutput, -motorPower); 
	}

	/**
	 * This method sets the motor speed on the left side of the
	 * drive train. 
	 * 
	 * @param motorPower
	 * 		A double value between -1 and 1 to set the motor to.
	 */
	public void SetLeft(double motorPower)
	{
		LeftMotor1.set(ControlMode.PercentOutput, motorPower);
		//LeftMotor2.set(ControlMode.PercentOutput, motorPower);
	}

	/**
	 * Method that will drive a given distance. 
	 * 
	 * @param targetDistance
	 * 		The distance to drive. 
	 */
	public void DriveDistance(double targetDistance)
	{
		double pVal = 0.04;
		LeftEncoder.reset();
		RightEncoder.reset();
		
		boolean isFirstTimeOnTarget = false;
		long firstTimeOnTarget = 0;
		long timeOnTarget = 0; 
		
		while (( ((Math.abs(LeftEncoder.getDistance()) - targetDistance < 1) || (Math.abs(RightEncoder.getDistance()) - targetDistance < 1)) 
				&& ((timeOnTarget - firstTimeOnTarget < 500)) && !DriverStation.getInstance().isDisabled()))
		{
//			System.out.println("time first on target: " + firstTimeOnTarget);
//			System.out.println("time on target: " + timeOnTarget);
			
			if ((Math.abs(LeftEncoder.getDistance() - targetDistance) < 1) && (Math.abs(RightEncoder.getDistance() - targetDistance) < 1))
			{
				System.out.println("Was on target");
				if (!isFirstTimeOnTarget)
				{
					isFirstTimeOnTarget = true;
					firstTimeOnTarget = System.currentTimeMillis(); 
				}
				
				timeOnTarget = System.currentTimeMillis();
			}
			else
			{
				isFirstTimeOnTarget = false; 
				timeOnTarget = 0;
			}
			
//			System.out.println("Right encoder value: " + RightEncoder.getDistance());
//			System.out.println("Left encoder value: " + LeftEncoder.getDistance());
			
			double leftDifference = Math.abs(LeftEncoder.getDistance() - targetDistance);
			double rightDifference = Math.abs(RightEncoder.getDistance() - targetDistance);
			
			double leftMotorPower = leftDifference * pVal;
			double rightMotorPower = rightDifference * pVal;
			
			if (leftMotorPower > 0.5)
			{
				leftMotorPower = 0.5;
			}
			else if (leftMotorPower < -0.5)
			{
				leftMotorPower = -0.5;
			}
			
			if (rightMotorPower > 0.5)
			{
				rightMotorPower = 0.5;
			}
			else if (rightMotorPower < -0.5)
			{
				rightMotorPower = -0.5;
			}
			
			System.out.println("Left rate: " + LeftEncoder.getRate());
			System.out.println("Right rate: " + RightEncoder.getRate());
			
			// TODO make go backwards 
			if (LeftEncoder.getRate() < 9 && leftMotorPower < 0.15)
			{
				System.out.println("The speed of the encoder was less than 0.5, left");
				leftMotorPower = 0.15;
			}
			
			if (RightEncoder.getRate() < 9 && rightMotorPower < 0.15)
			{
				System.out.println("The speed of the encoder was less than 0.5, right");
				rightMotorPower = 0.15;
			}
			
			System.out.println("left side power: " + leftMotorPower);
			System.out.println("right side power: " + rightMotorPower);
			
			if (Math.abs(LeftEncoder.getDistance() - targetDistance) < 1)
			{
				SetLeft(0);
			}
			else
			{
				SetLeft(leftMotorPower);
			}
			
			if (Math.abs(RightEncoder.getDistance() - targetDistance) < 1)
			{
				SetRight(0);
			}
			else
			{
				SetRight(rightMotorPower);
			}
			
			Utill.SleepThread(1);
		}
		
		SetLeft(0);
		SetRight(0);
		
		System.out.println("Right encoder value: " + RightEncoder.getDistance());
		System.out.println("Left encoder value: " + LeftEncoder.getDistance());
	}	

	/**
	 * Method that will turn a given number of degrees.
	 * 
	 * @param degree
	 * 		The amount to turn. 
	 */
	public void TurnDegrees(double degree)
	{
		Navx.zeroYaw();
		TurnDegreesPIDController.reset();
		TurnDegreesPIDController.setSetpoint(degree);

		System.out.println("About to enable PIDControler");
		TurnDegreesPIDController.enable();

		TurnDegreesTimeInPID = System.currentTimeMillis();

		while (!DriverStation.getInstance().isDisabled() && !TurnDegreesPIDController.onTarget())
		{
			if (System.currentTimeMillis() - TurnDegreesTimeInPID > 3000)
			{
				System.out.println("PID loop had to be broken");
				break;
			}
						
			Utill.SleepThread(1);
		}

		TurnDegreesPIDController.disable();
	}

	/**
	 * Method to make the button inputs on the SmartDashboard.
	 */
	private void CreateButtonsOnDashborad()
	{
		if (!SmartDashboard.getKeys().contains("P: "))
		{
			System.out.println("Added the P");
			SmartDashboard.putNumber("P: ", 0);
		}

		if (!SmartDashboard.getKeys().contains("I: "))
		{
			System.out.println("Added the I");
			SmartDashboard.putNumber("I: ", 0);
		}

		if (!SmartDashboard.getKeys().contains("D: "))
		{
			System.out.println("Added the D");
			SmartDashboard.putNumber("D: ", 0);
		}

		double Pval = SmartDashboard.getNumber("P: ", 0.0);
		double Ival = SmartDashboard.getNumber("I: ", 0.0);
		double Dval = SmartDashboard.getNumber("D: ", 0.0);

		System.out.println("P: " + Pval + " I: " + Ival + " D: " + Dval);
	}

	@Override
	public void pidWrite(double output)
	{		
		SetRight(output);
		SetLeft(-output);
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
		return Navx.getYaw();
	}
}