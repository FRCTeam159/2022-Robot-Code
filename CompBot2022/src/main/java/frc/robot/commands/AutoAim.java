// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;

public class AutoAim extends CommandBase implements Constants {

  public final double idealDistance = 103.26; //Ideal Distance limelight to target, inches
  public final double limelightArea = 4*idealDistance*idealDistance*Math.tan(29.8)*Math.tan(29.2); //inches^2
  public String aimMode;
  Limelight m_limelight;



  public AutoAim(Limelight limelight) {
    m_limelight = limelight;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    addRequirements(limelight);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
  }
  public static Trajectory getStraightPath(double distance) {
    //maybe use idk
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
    double xLime = m_limelight.limeX;
    double areaLime = m_limelight.limeA;
    aimMode = "Angle";
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {

  }
}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
 /* @Override
  protected void interrupted() {}
}
*/
