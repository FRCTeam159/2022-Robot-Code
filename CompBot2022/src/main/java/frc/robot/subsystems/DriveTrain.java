// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrain extends SubsystemBase implements Constants {
	
	private SparkMotor frontLeft;
	private SparkMotor frontRight;
	private SparkMotor backLeft;
	private SparkMotor backRight;

	NetworkTable table = NetworkTableInstance.getDefault().getTable("drivetrain");
	NetworkTableEntry lD = table.getEntry("leftDistance");
	NetworkTableEntry rD = table.getEntry("rightDistance");
	NetworkTableEntry vL = table.getEntry("velocity");

	private final MotorControllerGroup leftGroup;
	private final MotorControllerGroup rightGroup;

	private static final double LOW_GEARBOX_RATIO = 8.333;
	private static final double DRIVE_SPROCKET_RATIO = 38.0 / 22.0;
	private static final double FINAL_GEAR_RATIO = LOW_GEARBOX_RATIO * DRIVE_SPROCKET_RATIO;


	public static final double kMaxSpeed = i2M(50); // inches per second
	public static final double kMaxAngularSpeed = 180; // one rotation per second
    public static double kMaxAcceleration = 1; //mps

	private final AnalogGyro gyro = new AnalogGyro(0);

	private final PIDController leftPIDController = new PIDController(0.5, 0, 0);
	private final PIDController rightPIDController = new PIDController(0.5, 0, 0);

	private final DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(kTrackWidth);
	private final DifferentialDriveOdometry odometry;

	private final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(2, 3);

	private final SlewRateLimiter m_speedLimiter = new SlewRateLimiter(kMaxSpeed);
	private final SlewRateLimiter m_rotLimiter = new SlewRateLimiter(kMaxAngularSpeed);
	public boolean m_driveArcade = false; 
	public boolean useGyro = false;

	public static final double kTrackWidth = i2M(28.25); // inches
	private static final double kWheelRadius = i2M(2.13); // inches
	// public static final double WHEEL_DIAMETER = 4.26;
	private static final double distancePerRotationGearDown = (kWheelRadius * 2 * Math.PI)/FINAL_GEAR_RATIO;

	public DriveTrain() {
		frontLeft = new SparkMotor(FRONT_LEFT);
		frontRight = new SparkMotor(FRONT_RIGHT);
		backLeft = new SparkMotor(BACK_LEFT);
		backRight = new SparkMotor(BACK_RIGHT);
		frontLeft.setDistancePerRotation(distancePerRotationGearDown);
		frontRight.setDistancePerRotation(distancePerRotationGearDown);
		backLeft.setDistancePerRotation(distancePerRotationGearDown);
		backRight.setDistancePerRotation(distancePerRotationGearDown);
		gyro.reset();

		odometry = new DifferentialDriveOdometry(getRotation2d());

		leftGroup = new MotorControllerGroup(frontLeft, backLeft);
		rightGroup = new MotorControllerGroup(frontRight, backRight);
		rightGroup.setInverted(true);
		frontRight.setInverted();
		backRight.setInverted();
		SmartDashboard.putBoolean("ArcadeMode", m_driveArcade);
		
	}

	public void setSpeeds(DifferentialDriveWheelSpeeds speeds) {
		final double leftFeedforward = feedforward.calculate(speeds.leftMetersPerSecond);
		final double rightFeedforward = feedforward.calculate(speeds.rightMetersPerSecond);
		final double leftOutput = leftPIDController.calculate(frontLeft.getRate(), speeds.leftMetersPerSecond);
		final double rightOutput = rightPIDController.calculate(frontRight.getRate(),
				speeds.rightMetersPerSecond);
		leftGroup.setVoltage(leftOutput + leftFeedforward);
		rightGroup.setVoltage(rightOutput + rightFeedforward);
	}

	public double getHeading() {
			return getRotation2d().getDegrees();
	}

	public Rotation2d getRotation2d() {
		double delta = getLeftDistance() - getRightDistance();
		Rotation2d r = new Rotation2d(-delta/kTrackWidth); //remove neg if add neg at omega radianspersecond
		return r;
	}



	public void drive(double xSpeed, double rot) {
		var wheelSpeeds = kinematics.toWheelSpeeds(new ChassisSpeeds(xSpeed, 0.0, rot));
		setSpeeds(wheelSpeeds);
	}

	/** Updates the field-relative position. */
	public void updateOdometry() {
		odometry.update(getRotation2d(), (frontLeft.getDistance()), (frontRight.getDistance()));
	}

	public void resetOdometry(Pose2d pose) {
		gyro.reset();
		backLeft.reset();
		frontLeft.reset();
		backRight.reset();
		frontRight.reset();
		getRotation2d();
		odometry.resetPosition(pose, getRotation2d());
	}

	public void reset() {
		resetOdometry(new Pose2d());
	}

	public Pose2d getPose() {
		updateOdometry();
		return odometry.getPoseMeters();
	}

	private static double i2M(double inches) {
		return inches * 0.0254;
	}

	public void odometryDrive(double moveValue, double turnValue) {
		/*
		final var xSpeed = -m_speedLimiter.calculate(moveValue); 
		final var rot = -m_rotLimiter.calculate(turnValue);
		drive(xSpeed, rot);
		*/
		 
		drive(moveValue, turnValue);
		//System.out.println(moveValue + ": " + turnValue + " " + getHeading());
	}

	public void arcadeDrive(double moveValue, double turnValue) {
		double leftMotorOutput;
		double rightMotorOutput;
		// System.out.println("M:"+moveValue+" T:"+turnValue);
		moveValue *= 0.5;
		turnValue *= 0.5;
		if (moveValue > 0.0) {
			if (turnValue > 0.0) {
				leftMotorOutput = Math.max(moveValue, turnValue);
				rightMotorOutput = moveValue - turnValue;
			} else {
				leftMotorOutput = moveValue + turnValue;
				rightMotorOutput = Math.max(moveValue, -turnValue);
			}
		} else {
			if (turnValue > 0.0) {
				leftMotorOutput = moveValue + turnValue;
				rightMotorOutput = -Math.max(-moveValue, turnValue);
			} else {
				leftMotorOutput = -Math.max(-moveValue, -turnValue);
				rightMotorOutput = moveValue - turnValue;
			}
		}
		// Make sure values are between -1 and 1
		leftMotorOutput = coerce(-1, 1, leftMotorOutput);
		rightMotorOutput = coerce(-1, 1, rightMotorOutput);

		// lastMoveValue = moveValue;
		setRaw(leftMotorOutput, rightMotorOutput);
		// setRaw(0, rightMotorOutput);
	}

	private static double coerce(double min, double max, double value) {
		return Math.max(min, Math.min(value, max));
	}

	public void setRaw(double left, double right) {
		backLeft.set(left);
		frontLeft.set(left);
		frontRight.set(-right);
		backRight.set(-right);
		log();
	}

	public void log() {
		SmartDashboard.putNumber("leftDistance", frontLeft.getDistance());
		SmartDashboard.putNumber("leftSpeed", frontLeft.getRate());
		SmartDashboard.putNumber("rightDistance", frontRight.getDistance());
		SmartDashboard.putNumber("rightSpeed", frontRight.getRate());
		SmartDashboard.putNumber("Heading", getHeading());
		m_driveArcade = SmartDashboard.getBoolean("ArcadeMode", m_driveArcade);
		
SmartDashboard.putNumber("Rotations", frontRight.getRotations());
	}
	@Override
	public void periodic() {
		// This method will be called once per scheduler run
		updateOdometry();
		log();
	}

	@Override
	public void simulationPeriodic() {
		// This method will be called once per scheduler run during simulation
		log();
	}

    public double getLeftDistance() {
        return frontLeft.getDistance();
    }

    public double getRightDistance() {
        return frontRight.getDistance();
    }
}
