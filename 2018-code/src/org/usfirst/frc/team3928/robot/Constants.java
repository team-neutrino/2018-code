package org.usfirst.frc.team3928.robot;

public class Constants 
{	
	// CubeManipulator
	public static final int INTAKE_MOTOR_1 = 8;
	public static final double CUBE_MANIPULATOR_MOTOR_POWER = 1;
	public static final int STALLED_MOTOR_CURRENT = 45; //10
	public static final int CURRENT_OVER_THRESHOLD_TIME_MILLIS = 500;
	
	// Drive
	public static final int DRIVE_RIGHT_1_CHANNEL = 0;
	public static final int DRIVE_RIGHT_2_CHANNEL = 1;
	public static final int DRIVE_LEFT_1_CHANNEL = 2;
	public static final int DRIVE_LEFT_2_CHANNEL = 3;
	
	public static final int DRIVE_RIGHT_ENCODER_POWER_CHANNEL = 4; 
	public static final int DRIVE_RIGHT_ENCODER_DATA_CHANNEL = 5; 
	public static final int DRIVE_LEFT_ENCODER_POWER_CHANNEL = 2;
	public static final int DRIVE_LEFT_ENCODER_DATA_CHANNEL = 3;
	
	public static final int DRIVE_WHEEL_RADUIS = 2;
	public static final int ENCODER_COUNTS_PER_ROTATION = 360;
	
	public static final double DRIVE_NAVX_ANGLE_ADJUSTMENT = 5.5;
	
	public static final double DRIVE_P_VALUE_DEGREE_TURN = 0.03; //0.0275
	public static final double DRIVE_I_VALUE_DEGREE_TURN = 0.0;
	public static final double DRIVE_D_VALUE_DEGREE_TURN = 0.045; 
	public static final int DRIVE_ABSOLUTE_VALUE_TOLERANCE_DEGREE_TURN = 2;
	public static final int DRIVE_PID_INPUT_RANGE_MIN_DEGREE_TURN = -180;
	public static final int DRIVE_PID_INPUT_RANGE_MAX_DEGREE_TURN = 180;
	public static final double DRIVE_PID_OUTPUT_RANGE_MIN_DEGREE_TURN = -0.4;
	public static final double DRIVE_PID_OUTPUT_RANGE_MAX_DEGREE_TURN  = 0.4;
	
	public static final double DRIVE_P = 0.5; //0.272, 0.2, 0.19, 0.18, 0.15, 0.1, 0.2, 0.4, 0.5
	public static final double DRIVE_I = 0.014125;
	public static final double DRIVE_D = 0.15; //0.471, 0.3, 0.2, 0.1, 0.6, 0, 0.3
	
	public static final double DRIVE_P_VALUE_RIGHT = DRIVE_P;
	public static final double DRIVE_I_VALUE_RIGHT = DRIVE_I;
	public static final double DRIVE_D_VALUE_RIGHT = DRIVE_D;
	
	public static final double DRIVE_P_VALUE_LEFT = DRIVE_P;
	public static final double DRIVE_I_VALUE_LEFT = DRIVE_I;
	public static final double DRIVE_D_VALUE_LEFT = DRIVE_D;
	
	public static final int DRIVE_ABSOLUTE_VALUE_TOLERANCE = 1;
	public static final int DRIVE_PID_INPUT_RANGE_MIN = -200;
	public static final int DRIVE_PID_INPUT_RANGE_MAX = 200;
	public static final double DRIVE_PID_OUTPUT_RANGE_MIN = -0.8;
	public static final double DRIVE_PID_OUTPUT_RANGE_MAX = 0.6; //0.8
	
	// Elevator
	public static final int ELEVATOR_ENCODER_POWER_CHANNEL = 0; 
	public static final int ELEVATOR_ENCODER_DATA_CHANNEL = 1; 
	public static final double ELEVATOR_DISTANCE_TRAVELED_PER_ROTATION = 8.0;
	
	public static final int ELEVATOR_MOTOR_1 = 4;
	public static final int ELEVATOR_MOTOR_2 = 5;
	public static final int ELEVATOR_MOTOR_3 = 6;
	public static final int ELEVATOR_MOTOR_4 = 7;
	
	public static final int ELEVATOR_BUTTON = 8;

	public static final double ELEVATOR_P_VALUE = 0.2;
	public static final double ELEVATOR_I_VALUE = 0.0;
	public static final double ELEVATOR_D_VALUE = 0.0;
	public static final double ELEVATOR_ABSOLUTE_VALUE_TOLERANCE = 0.5;
	public static final int ELEVATOR_PID_INPUT_RANGE_MIN = 1;
	public static final int ELEVATOR_PID_INPUT_RANGE_MAX = 70;
	public static final double ELEVATOR_PID_OUTPUT_RANGE_MIN = -1;
	public static final double ELEVATOR_PID_OUTPUT_RANGE_MAX = 1;
	
	public static final int ELEVATOR_CLIMBER_SOLENOID_OUT = 0;
	public static final int ELEVATOR_CLIMBER_SOLENOID_IN = 1;
	
	// Joysticks 
	public static final int THRUST_MASTER_CONTROLLER = 3;
	public static final int LEFT_JOYSTICK = 1;
	public static final int RIGHT_JOYSTICK = 2;
	
	// Misc
	public static final int PRINT_SPEED_DIVIDER = 10;	
}
