// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.List;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;

public class DriveToPath extends CommandBase implements Constants {
  private final RamseteController m_ramsete = new RamseteController();
  private final Timer m_timer = new Timer();
  private final DriveTrain m_drive;
  private Trajectory m_trajectory;
  private double elapse;
  public DriveToPath(DriveTrain drive) {
    m_drive = drive;
    addRequirements(m_drive);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    m_trajectory = getStraightPath(2.0);
    m_drive.resetOdometry(m_trajectory.getInitialPose());
    m_timer.reset();
    m_timer.start();
    elapse = 0;
    System.out.println("Works?");
  }
  
  public static Trajectory getStraightPath(double distance) {
    Trajectory Traj = TrajectoryGenerator.generateTrajectory(
      new Pose2d(0, 0, new Rotation2d(0.0)),
      List.of(),
      new Pose2d(distance, 0, new Rotation2d(0.0)),
      new TrajectoryConfig(DriveTrain.kMaxSpeed, DriveTrain.kMaxAcceleration)
    ); 
    return Traj;
  }
  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    elapse = m_timer.get();
    Trajectory.State reference = m_trajectory.sample(elapse);
    ChassisSpeeds speeds = m_ramsete.calculate(m_drive.getPose(), reference);

    m_drive.odometryDrive(speeds.vxMetersPerSecond, speeds.omegaRadiansPerSecond);
  }

  /*
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {}
} */
}