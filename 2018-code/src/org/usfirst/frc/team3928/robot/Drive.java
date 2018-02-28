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
	 * Enum for which side the pidWrite function should run on. 
	 * 
	 * @author NicoleEssner
	 *
	 */
	private enum DriveSide
	{
		LEFT, RIGHT, NAVX;
	}

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
	 * The PIDController for the right side of the drive train 
	 * for driving a distance. 
	 */
	private PIDController RightDrivePIDController;

	/**
	 * The PIDController for the left side of the drive train 
	 * for driving a distance. 
	 */
	private PIDController LeftDrivePIDController;

	/**
	 * The time that the PID Loop for turning degrees 
	 * has been executing. 
	 */
	private long TurnDegreesTimeInPID;

	/**
	 * The time that the PID Loop for driving a distance 
	 * has been executing. 
	 */
	private long DriveDistanceTimeInPID;

	/**
	 * The motor output value for the Navx PID.
	 */
	private double NavxPIDOutput;

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

		RightDrivePIDController = new PIDController(Constants.DRIVE_P_VALUE_RIGHT, Constants.DRIVE_I_VALUE_RIGHT, 
				Constants.DRIVE_D_VALUE_RIGHT, RightEncoder, new DrivePID(DriveSide.RIGHT, true)); 
		RightDrivePIDController.setAbsoluteTolerance(Constants.DRIVE_ABSOLUTE_VALUE_TOLERANCE); 
		RightDrivePIDController.setInputRange(Constants.DRIVE_PID_INPUT_RANGE_MIN, Constants.DRIVE_PID_INPUT_RANGE_MAX); 

		LeftDrivePIDController = new PIDController(Constants.DRIVE_P_VALUE_LEFT, Constants.DRIVE_I_VALUE_LEFT, 
				Constants.DRIVE_D_VALUE_LEFT, LeftEncoder, new DrivePID(DriveSide.LEFT, true)); 
		LeftDrivePIDController.setAbsoluteTolerance(Constants.DRIVE_ABSOLUTE_VALUE_TOLERANCE); 
		LeftDrivePIDController.setInputRange(Constants.DRIVE_PID_INPUT_RANGE_MIN, Constants.DRIVE_PID_INPUT_RANGE_MAX); 


		DriveDistanceTimeInPID = 0;
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
		RightMotor1.set(ControlMode.PercentOutput, motorPower); 
		RightMotor2.set(ControlMode.PercentOutput, motorPower); 
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
		LeftMotor1.set(ControlMode.PercentOutput, -motorPower);
		LeftMotor2.set(ControlMode.PercentOutput, -motorPower);
	}

	/**
	 * Method that will drive a given distance. 
	 * 
	 * @param targetDistance
	 * 		The distance to drive. 
	 */
	public void DriveDistance(double targetDistance, double outputRange)
	{
		RightEncoder.reset();
		LeftEncoder.reset();

		// TODO this needs help at some point
		RightDrivePIDController.setOutputRange(-outputRange, outputRange); 
		LeftDrivePIDController.setOutputRange(-outputRange, outputRange); 

		RightDrivePIDController.reset();
		LeftDrivePIDController.reset();

		RightDrivePIDController.setSetpoint(targetDistance);
		LeftDrivePIDController.setSetpoint(targetDistance);

		System.out.println("About to enable PIDControler");
		RightDrivePIDController.enable();
		LeftDrivePIDController.enable();

		DriveDistanceTimeInPID = System.currentTimeMillis();

		while (!RightDrivePIDController.onTarget() && !LeftDrivePIDController.onTarget())
		{
			if (System.currentTimeMillis() - DriveDistanceTimeInPID > 10000)
			{
				System.out.println("PID loop had to be broken");
				break;
			}

			//					System.out.println("The right encoder value is: " + RightEncoder.getDistance());
			//					System.out.println("The left encoder value is: " + LeftEncoder.getDistance());
			//					
			Timer.delay(0.05);
		}

		RightDrivePIDController.disable();
		LeftDrivePIDController.disable();
	}	

	
	/**
	 * Will drive a distance with out PID.
	 * 
	 * @param targetDistance
	 */
	private void DriveDistanceNeutrinoPID(double targetDistance)
	{
		int numTimesThroughLoop = 0;  

		while(Math.abs(targetDistance) > Math.abs(RightEncoder.getDistance()) && 
				Math.abs(targetDistance) > Math.abs(LeftEncoder.getDistance()) &&
				(DriverStation.getInstance().isAutonomous() && !DriverStation.getInstance().isDisabled()))
		{
			System.out.println("Encoder right value: " + RightEncoder.getDistance());
			System.out.println("Encoder left value: " + LeftEncoder.getDistance());

			double rightMotorPower = PID.PIDControl(targetDistance, RightEncoder.getDistance(), 0.05, 0.2, 2, (numTimesThroughLoop % 2000 == -1));
			double leftMotorPower = PID.PIDControl(targetDistance, LeftEncoder.getDistance(), 0.05, 0.2, 2, (numTimesThroughLoop % 2000 == -1));

			SetRight(rightMotorPower);
			SetLeft(leftMotorPower);

			numTimesThroughLoop++; 

			Utill.SleepThread(1);
		}	
	}

	public void DriveDistanceWithNAVX(double targetDistance)
	{
		// TODO
		Navx.zeroYaw();
		TurnDegreesPIDController.reset();
		TurnDegreesPIDController.setSetpoint(0);

		RightEncoder.reset();
		LeftEncoder.reset();
		RightDrivePIDController.setOutputRange(-1, 1); 
		LeftDrivePIDController.setOutputRange(-1, 1); 
		RightDrivePIDController.reset();
		LeftDrivePIDController.reset();
		RightDrivePIDController.setSetpoint(targetDistance);
		LeftDrivePIDController.setSetpoint(targetDistance);

		while (!RightDrivePIDController.onTarget() && !LeftDrivePIDController.onTarget())
		{
			Utill.SleepThread(1);
		}

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
		NavxPIDOutput = output; 
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
	
	/**
	 * This is a private class for running multiple instance of PIDController 
	 * in the same class. 
	 * 
	 * @author NicoleEssner
	 *
	 */
	private class DrivePID implements PIDOutput
	{
		/**
		 * Enum value for the which side of the drive train 
		 * the PID should run on. 
		 */
		private DriveSide Side;

		/**
		 * Weather the drive distance with the navx is running.
		 */
		private boolean isNavxPID;

		/**
		 * Construction for DrivePID class.
		 * 
		 * @param state
		 * 		The side of the drive the PID should run
		 * 		on.  
		 */
		public DrivePID(DriveSide state, boolean isNavx)
		{
			Side = state;
			isNavxPID = isNavx;
		}

		@Override
		public void pidWrite(double output) 
		{
			output = output / 2; 
			double temp = NavxPIDOutput / 2;
			
			if (Side == DriveSide.LEFT && isNavxPID)
			{
				double sum = output - temp;

				SetLeft(-sum);
			}
			else if (Side == DriveSide.LEFT && isNavxPID)
			{
				double sum = output + temp;

				SetRight(-sum);
			}
			else if (Side == DriveSide.LEFT && !isNavxPID)
			{
				SetLeft(-output);
			}
			else if (Side == DriveSide.LEFT && !isNavxPID)
			{
				SetRight(output);
			}
		}
	}
}