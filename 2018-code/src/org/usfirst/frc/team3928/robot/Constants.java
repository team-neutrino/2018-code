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
	public static final int WHEEL_DIAMETER = 4;
	public static final int ENCODER_COUNTS_PER_ROTATION = 360;
	public static final int RIGHT_MOTOR_1 = 1;
	public static final int RIGHT_MOTOR_2 = 2;
	public static final int LEFT_MOTOR_1 = 3;
	public static final int LEFT_MOTOR_2 = 4;
	
	public static final int RIGHT_ENCODER_POWER_CHANNEL = 7;
	public static final int RIGHT_ENCODER_DATA_CHANNEL = 8;
	public static final int LEFT_ENCODER_POWER_CHANNEL = 0;
	public static final int LEFT_ENCODER_DATA_CHANNEL = 1;
	public static final double ENCODER_DISTANCE_PER_PULSE = WHEEL_DIAMETER * Math.PI / ENCODER_COUNTS_PER_ROTATION;
	
	public static final double NAVX_ANGLE_ADJUSTMENT = 5.5;
	
	public static final double P_VALUE_DEGREE_TURN = 0.0275;
	public static final double I_VALUE_DEGREE_TURN = 0.0;
	public static final double D_VALUE_DEGREE_TURN = 0.045;
	public static final int ABSOLUTE_VALUE_TOLERANCE_DEGREE_TURN = 2;
	
	//Elevator
	public static final int ELEVATOR_ENCODER_POWER_CHANNEL = 4;
	public static final int ELEVATOR_ENCODER_DATA_CHANNEL = 5;
	public static final double DISTANCE_TRAVELED_PER_ROTATION = 9.0;
	
	public static final int ELEVATOR_MOTOR_1 = 3;
	
	//Robot
	public static final int XBOX_CONTROLLER = 0;
	public static final int LEFT_JOYSTICK = 1;
	public static final int RIGHT_JOYSTICK = 2;
	
	public static final int PRINT_SPEED_DIVIDER = 10;
	
	
	
			
}
