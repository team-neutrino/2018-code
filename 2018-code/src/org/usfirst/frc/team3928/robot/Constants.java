package org.usfirst.frc.team3928.robot;

public class Constants 
{	
	//CubeManipulator
	public static final int INTAKE_MOTOR_1 = 8;
	public static final int INTAKE_MOTOR_2 = 9;
	public static final double CUBE_MANIPULATOR_MOTOR_POWER_MAXIMUM = 1;
	public static final int MOTOR_CURRENT_STALLED = 10;
	public static final int POWER_OVER_THRESHOLD_TIME_MILLIS = 500;
	
	//Drive
	public static final int DRIVE_WHEEL_DIAMETER = 4;
	public static final int DRIVE_ENCODER_COUNTS_PER_ROTATION = 360;
	public static final int DRIVE_RIGHT_1_CHANNEL = 1;
	public static final int DRIVE_RIGHT_2_CHANNEL = 2;
	public static final int DRIVE_EFT_1_CHANNEL = 3;
	public static final int DRIVE_LEFT_2_CHANNEL = 4;
	
	public static final int DRIVE_RIGHT_ENCODER_POWER_CHANNEL = 7;
	public static final int DRIVE_RIGHT_ENCODER_DATA_CHANNEL = 8;
	public static final int DRIVE_LEFT_ENCODER_POWER_CHANNEL = 0;
	public static final int DRIVE_LEFT_ENCODER_DATA_CHANNEL = 1;
	public static final double DRIVE_ENCODER_DISTANCE_PER_PULSE = DRIVE_WHEEL_DIAMETER * Math.PI / DRIVE_ENCODER_COUNTS_PER_ROTATION;
	
	public static final double DRIVE_NAVX_ANGLE_ADJUSTMENT = 5.5;
	
	public static final double DRIVE_P_VALUE_DEGREE_TURN = 0.0275;
	public static final double DRIVE_I_VALUE_DEGREE_TURN = 0.0;
	public static final double DRIVE_D_VALUE_DEGREE_TURN = 0.045;
	public static final int DRIVE_ABSOLUTE_VALUE_TOLERANCE_DEGREE_TURN = 2;
	
	//Elevator
	public static final int ELEVATOR_ENCODER_POWER_CHANNEL = 4;
	public static final int ELEVATOR_ENCODER_DATA_CHANNEL = 5;
	public static final double ELEVATOR_DISTANCE_TRAVELED_PER_ROTATION = 9.0;
	
	public static final int ELEVATOR_MOTOR_1 = 3;
	
	//Robot
	public static final int XBOX_CONTROLLER = 0;
	public static final int LEFT_JOYSTICK = 1;
	public static final int RIGHT_JOYSTICK = 2;
	
	public static final int PRINT_SPEED_DIVIDER = 10;
	
	
	
			
}
