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
	
	private PIDController PIDControllerInst;
	
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
		
		PIDControllerInst =  new PIDController(Constants.DRIVE_P_VALUE_DEGREE_TURN, Constants.DRIVE_I_VALUE_DEGREE_TURN, 
				 							  Constants.DRIVE_D_VALUE_DEGREE_TURN, 0.0, this, this);
		PIDControllerInst.setAbsoluteTolerance(Constants.DRIVE_ABSOLUTE_VALUE_TOLERANCE_DEGREE_TURN);
		
		PIDControllerInst.setInputRange(Constants.DRIVE_PID_INPUT_RANGE_MIN, Constants.DRIVE_PID_INPUT_RANGE_MAX);
		PIDControllerInst.setOutputRange(Constants.DRIVE_PID_OUTPUT_RANGE_MIN, Constants.DRIVE_PID_OUTPUT_RANGE_MAX);
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
	public void DriveDistance(double targetDistance)
	{
		RightEncoder.reset();
		LeftEncoder.reset();
		
		int numTimesThroughLoop = 0;  
		
		while(Math.abs(targetDistance) > Math.abs(RightEncoder.getDistance()) && 
			  Math.abs(targetDistance) > Math.abs(LeftEncoder.getDistance()) &&
			  (DriverStation.getInstance().isAutonomous() && !DriverStation.getInstance().isDisabled()))
		{
			double rightMotorPower = PID.PIDControl(targetDistance, RightEncoder.getDistance(), 0.05, 0.2, 5, (numTimesThroughLoop % 2000 == -1));
			double leftMotorPower = PID.PIDControl(targetDistance, LeftEncoder.getDistance(), 0.05, 0.2, 5, (numTimesThroughLoop % 2000 == -1));

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
		PIDControllerInst.reset();
		PIDControllerInst.setSetpoint(degree);
		
		System.out.println("About to enable PIDControler");
		PIDControllerInst.enable();
		
		while (!DriverStation.getInstance().isDisabled())
			//(!PIDControllerInst.onTarget() && (DriverStation.getInstance().isAutonomous() && !DriverStation.getInstance().isDisabled()))
		{
			double navxYaw = Navx.getYaw();
			System.out.println("In pidGet, the Navx yaw: " + navxYaw);
			
//			PIDControllerInst.setP(SmartDashboard.getNumber("P: ", 0));
//			PIDControllerInst.setI(SmartDashboard.getNumber("I: ", 0));
//			PIDControllerInst.setD(SmartDashboard.getNumber("D: ", 0));
//			
			Timer.delay(1);
			
		}
		
		PIDControllerInst.disable();
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
		double navxYaw = Navx.getYaw();
		
		return navxYaw;
	}
}