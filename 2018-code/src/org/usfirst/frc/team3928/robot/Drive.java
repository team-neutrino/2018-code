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
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.SerialPort;
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
	
	private PIDController TurnDegreesPIDController;
	
	private PIDController RightDrivePIDController;
	
	private PIDController LeftDrivePIDController;
	
	private long TurnDegreesTimeInPID;
	
	private long DriveDistanceTimeInPID;
	
	private boolean isDrivingDistance; 
	
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
		
		int WheelRadius = Constants.DRIVE_WHEEL_RADUIS;
		int CountsPerRotation = Constants.ENCODER_COUNTS_PER_ROTATION; 
		
		RightEncoder.setDistancePerPulse(2 * WheelRadius * Math.PI / CountsPerRotation);
		LeftEncoder.setDistancePerPulse(2 * WheelRadius * Math.PI / CountsPerRotation);
		
		RightEncoder.reset();
		LeftEncoder.reset();
	
		Navx = new AHRS(SPI.Port.kMXP);
		Navx.setAngleAdjustment(Constants.DRIVE_NAVX_ANGLE_ADJUSTMENT); // Do we need this still? 
		
//		if (!SmartDashboard.getKeys().contains("P: "))
//		{
//			System.out.println("Added the P");
//			SmartDashboard.putNumber("P: ", 0);
//		}
//	
//		if (!SmartDashboard.getKeys().contains("I: "))
//		{
//			System.out.println("Added the I");
//			SmartDashboard.putNumber("I: ", 0);
//		}
//		
//		if (!SmartDashboard.getKeys().contains("D: "))
//		{
//			System.out.println("Added the D");
//			SmartDashboard.putNumber("D: ", 0);
//		}
//		
//		double Pval = SmartDashboard.getNumber("P: ", 0.0);
//		double Ival = SmartDashboard.getNumber("I: ", 0.0);
//		double Dval = SmartDashboard.getNumber("D: ", 0.0);
//		
//		System.out.println("P: " + Pval + " I: " + Ival + " D: " + Dval);
		
		TurnDegreesPIDController =  new PIDController(Constants.DRIVE_P_VALUE_DEGREE_TURN, Constants.DRIVE_I_VALUE_DEGREE_TURN, 
				 							  Constants.DRIVE_D_VALUE_DEGREE_TURN, this, this);
		TurnDegreesPIDController.setAbsoluteTolerance(Constants.DRIVE_ABSOLUTE_VALUE_TOLERANCE_DEGREE_TURN);
		TurnDegreesPIDController.setInputRange(Constants.DRIVE_PID_INPUT_RANGE_MIN, Constants.DRIVE_PID_INPUT_RANGE_MAX);
		TurnDegreesPIDController.setOutputRange(Constants.DRIVE_PID_OUTPUT_RANGE_MIN, Constants.DRIVE_PID_OUTPUT_RANGE_MAX);
		
		RightDrivePIDController = new PIDController(0.025, 0.01, 0.4, RightEncoder, new DrivePID(DriveSide.RIGHT)); 
		RightDrivePIDController.setAbsoluteTolerance(1);
		RightDrivePIDController.setInputRange(-200, 200);
		RightDrivePIDController.setOutputRange(-0.8, 0.8);
		
		LeftDrivePIDController = new PIDController(0.025, 0.01, 0.4, LeftEncoder, new DrivePID(DriveSide.LEFT)); 
		LeftDrivePIDController.setAbsoluteTolerance(1);
		LeftDrivePIDController.setInputRange(-200, 200);
		LeftDrivePIDController.setOutputRange(-0.8, 0.8);
		
		isDrivingDistance = false;
		
		DriveDistanceTimeInPID = 0;
		TurnDegreesTimeInPID = 0;
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
		LeftMotor1.set(ControlMode.PercentOutput, -motorPower);
		LeftMotor2.set(ControlMode.PercentOutput, -motorPower);
	}
	
	/**
	 * Method that will drive a given distance. 
	 * 
	 * @param targetDistance
	 * 		The distance to drive. 
	 */
	public void DriveDistance(double targetDistance)
	{
//		isDrivingDistance = true;
//		
		RightEncoder.reset();
		LeftEncoder.reset();
//		
//		RightDrivePIDController.reset();
//		LeftDrivePIDController.reset();
//		
//		RightDrivePIDController.setSetpoint(targetDistance);
//		LeftDrivePIDController.setSetpoint(targetDistance);
//		
//		System.out.println("About to enable PIDControler");
//		RightDrivePIDController.enable();
//		LeftDrivePIDController.enable();
//		
//		DriveDistanceTimeInPID = System.currentTimeMillis();
//		
//		while (true)//!RightDrivePIDController.onTarget() || !LeftDrivePIDController.onTarget())
//		{
//			if (System.currentTimeMillis() - DriveDistanceTimeInPID > 10000)
//			{
//				System.out.println("PID loop had to be broken");
//				break;
//			}
//			
//			System.out.println("The right encoder value is: " + RightEncoder.getDistance());
//			System.out.println("The left encoder value is: " + LeftEncoder.getDistance());
//			
//			Timer.delay(0.05);
//		}
//		
//		RightDrivePIDController.disable();
//		LeftDrivePIDController.disable();
//		
//		isDrivingDistance = false;
		
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
			
			try 
			{
				Thread.sleep(1);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
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
			//(!PIDControllerInst.onTarget() && (DriverStation.getInstance().isAutonomous() && !DriverStation.getInstance().isDisabled()))
		{
			if (System.currentTimeMillis() - TurnDegreesTimeInPID > 3000)
			{
				System.out.println("PID loop had to be broken");
				break;
			}
			
			double navxYaw = Navx.getYaw();
			System.out.println("In pidGet, the Navx yaw: " + navxYaw);
			
//			PIDControllerInst.setP(SmartDashboard.getNumber("P: ", 0));
//			PIDControllerInst.setI(SmartDashboard.getNumber("I: ", 0));
//			PIDControllerInst.setD(SmartDashboard.getNumber("D: ", 0));
//			
			Timer.delay(0.1);
			
		}
		
		TurnDegreesPIDController.disable();
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
	
	private enum DriveSide
	{
		LEFT, RIGHT;
	}
	
	private class DrivePID implements PIDOutput
	{
		
		private DriveSide Side;
		
		public DrivePID(DriveSide state)
		{
			Side = state;
		}
		
		@Override
		public void pidWrite(double output) 
		{
			if (Side == DriveSide.LEFT)
			{
				SetLeft(-output);
			}
			else
			{
				SetRight(-output);
			}
		}
		
	}
}