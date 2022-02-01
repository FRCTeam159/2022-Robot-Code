// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;

public class TurnToAngle extends CommandBase {
  DriveTrain m_drive;
  double m_angle;

  private final PIDController m_controller= 
    new PIDController(0.3, 0, 0.01);

  public TurnToAngle(DriveTrain drive, double angle) {
    m_drive = drive;
    m_angle = angle;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    System.out.println("TurnToAngle.initialize");
    m_controller.enableContinuousInput(-200, 200);
    m_controller.setTolerance(0.5, 0.1); //TurnToleranceDegree, Turn

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    double heading = m_drive.getHeading();
    double correction = m_controller.calculate(heading, m_angle);
    m_drive.arcadeDrive(0, -correction);

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    // End when the controller is at the reference.
    //return m_controller.atGoal();
    return m_controller.atSetpoint();
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    System.out.println("TurnToAngle.end");
    m_drive.reset();
  }
  /*
  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  public void interrupted() {}
  */
}
