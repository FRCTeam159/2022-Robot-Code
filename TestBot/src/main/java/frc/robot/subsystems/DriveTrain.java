// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrain extends SubsystemBase implements Constants {
	private MotorInterface frontLeft;
	private MotorInterface frontRight;
	private MotorInterface backLeft;
	private MotorInterface backRight;
  public DriveTrain() {
    frontLeft = new SparkMotor(FRONT_LEFT);
    frontRight = new SparkMotor(FRONT_RIGHT);
    backLeft = new SparkMotor(BACK_LEFT);
    backRight = new SparkMotor(BACK_RIGHT);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
