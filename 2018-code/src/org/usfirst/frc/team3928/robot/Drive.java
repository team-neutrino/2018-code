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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is for the information associated with the 
 * dive train.
 * 
 * @author NicoleEssner
 *
 */
public class Drive implements PIDSource, PIDOutput, Printer 
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
		
		new ValuePrinter(this);
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
		RightMotor2.set(ControlMode.PercentOutput, -motorPower); 
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
		LeftMotor2.set(ControlMode.PercentOutput, motorPower);
	}

	/**
	 * Method that will drive forward a given distance. 
	 * 
	 * @param targetDistance
	 * 		The distance to drive. 
	 */
	public void DriveDistance(double targetDistance)
	{
		double pVal = 0.02;
		LeftEncoder.reset();
		RightEncoder.reset();
		Navx.zeroYaw();
		
		boolean isFirstTimeOnTarget = false;
		long firstTimeOnTarget = 0;
		long timeOnTarget = 0; 
		
		while ((timeOnTarget - firstTimeOnTarget < 100) && !DriverStation.getInstance().isDisabled()) 
		{
			if ((targetDistance - LeftEncoder.getDistance() < 1) && (targetDistance - RightEncoder.getDistance() < 1))
			{
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
				firstTimeOnTarget = 0;
				timeOnTarget = 0;
			}

			double leftDifference = Math.abs(LeftEncoder.getDistance() - targetDistance);
			double rightDifference = Math.abs(RightEncoder.getDistance() - targetDistance);
			
			double leftMotorPower = leftDifference * pVal;
			double rightMotorPower = rightDifference * pVal;
			
			if (leftMotorPower > 0.8)
			{
				leftMotorPower = 0.8;
			}
			else if (leftMotorPower < -0.8)
			{
				leftMotorPower = -0.8;
			}
			
			if (rightMotorPower > 0.8)
			{
				rightMotorPower = 0.8;
			}
			else if (rightMotorPower < -0.8)
			{
				rightMotorPower = -0.8;
			}
			
			if (LeftEncoder.getRate() < 9 && leftMotorPower < 0.15)
			{
				leftMotorPower = 0.15;
			}
			
			if (RightEncoder.getRate() < 9 && rightMotorPower < 0.15)
			{
				rightMotorPower = 0.15;
			}
			
			double maxAngle = 5;
			double proportionalDifference = 1 / maxAngle;
					
			if(Navx.getYaw() < 0)
			{
				if(Math.abs(Navx.getYaw()) > maxAngle)
				{
					leftMotorPower = leftMotorPower + 0.2;
				}
				else
				{
					leftMotorPower = leftMotorPower + Navx.getYaw() * -proportionalDifference;
				}
			}
			else
			{
				if(Math.abs(Navx.getYaw()) > maxAngle)
				{
					rightMotorPower = rightMotorPower + 0.2;
				}
				else
				{
					rightMotorPower = rightMotorPower + Navx.getYaw() * proportionalDifference;
				}
			}
			
			if (targetDistance - LeftEncoder.getDistance() < 1)
			{
				SetLeft(0);
			}
			else
			{
				SetLeft(leftMotorPower);
			}
			
			if (targetDistance - RightEncoder.getDistance() < 1)
			{
				SetRight(0);
			}
			else
			{
				SetRight(rightMotorPower);
			}
			
			Utill.SleepThread(1);
		}
		
		System.out.println("\n\nThe loop exited\n\n");
	}
	
	/**
	 * Method that will drive backward a given distance. 
	 * 
	 * @param targetDistance
	 * 		The distance to drive. 
	 */
	public void DriveDistanceBackwards(double targetDistance)
	{
		double pVal = 0.02;
		LeftEncoder.reset();
		RightEncoder.reset();
		Navx.zeroYaw();
		
		boolean isFirstTimeOnTarget = false;
		long firstTimeOnTarget = 0;
		long timeOnTarget = 0; 
		
		while ((timeOnTarget - firstTimeOnTarget < 100) && !DriverStation.getInstance().isDisabled()) 
		{
			if ((targetDistance - LeftEncoder.getDistance() > -1) && (targetDistance - RightEncoder.getDistance() > -1))
			{
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
				firstTimeOnTarget = 0;
				timeOnTarget = 0;
			}

			double leftDifference = Math.abs(LeftEncoder.getDistance() - targetDistance);
			double rightDifference = Math.abs(RightEncoder.getDistance() - targetDistance);
			
			double leftMotorPower = -(leftDifference * pVal);
			double rightMotorPower = -(rightDifference * pVal);
			
			if (leftMotorPower > 0.8)
			{
				leftMotorPower = 0.8;
			}
			else if (leftMotorPower < -0.8)
			{
				leftMotorPower = -0.8;
			}
			
			if (rightMotorPower > 0.8)
			{
				rightMotorPower = 0.8;
			}
			else if (rightMotorPower < -0.8)
			{
				rightMotorPower = -0.8;
			}
			
			if (LeftEncoder.getRate() > -9 && leftMotorPower > -0.15)
			{
				leftMotorPower = -0.15;
			}
			
			if (RightEncoder.getRate() < -9 && rightMotorPower > -0.15)
			{
				rightMotorPower = -0.15;
			}
			
			double maxAngle = 5;
			double proportionalDifference = 1 / maxAngle;
			
			if(Navx.getYaw() < 0)
			{
				if(Math.abs(Navx.getYaw()) > maxAngle)
				{
					rightMotorPower = rightMotorPower - 0.5;
					
				}
				else
				{
					rightMotorPower = rightMotorPower - Navx.getYaw() * -proportionalDifference;
				}
			}
			else
			{
				if(Math.abs(Navx.getYaw()) > maxAngle)
				{
					leftMotorPower = leftMotorPower - 0.5;
				}
				else
				{

					leftMotorPower = leftMotorPower - Navx.getYaw() * proportionalDifference;
				}
			}
			
			if (targetDistance - LeftEncoder.getDistance() > -1)
			{
				SetLeft(0);
			}
			else
			{
				SetLeft(leftMotorPower);
			}
			
			if (targetDistance - RightEncoder.getDistance() > -1)
			{
				SetRight(0);
			}
			else
			{
				SetRight(rightMotorPower);
			}
			
			Utill.SleepThread(1);
		}
		
		System.out.println("\n\nThe loop exited\n\n");
	}

	/**
	 * Method that will turn a given number of degrees.
	 * 
	 * @param degree
	 * 		The amount to turn. 
	 */
	public void TurnDegrees(double degree, int timeOut)
	{
		Navx.zeroYaw();
		TurnDegreesPIDController.reset();
		TurnDegreesPIDController.setSetpoint(degree);
		TurnDegreesPIDController.enable();
		TurnDegreesTimeInPID = System.currentTimeMillis();
	
		while (!DriverStation.getInstance().isDisabled() && !TurnDegreesPIDController.onTarget())
		{
			if (System.currentTimeMillis() - TurnDegreesTimeInPID > timeOut)
			{
				System.out.println("PID loop had to be broken");
				break;
			}
						
			Utill.SleepThread(1);
		}
		System.out.println("left turn PID");
		TurnDegreesPIDController.disable();
	}

	@Override
	public void pidWrite(double output)
	{		
		SetRight(-output);
		SetLeft(output);
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

	@Override
	public void PrintValues() 
	{
		SmartDashboard.putNumber("Right Drive Encoder: ", RightEncoder.getDistance());
		SmartDashboard.putNumber("Left Drive Encoder: ", LeftEncoder.getDistance());
		SmartDashboard.putNumber("Navx yaw: ", Navx.getYaw());
	}
}