// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;
//import utils.PathData;
//import utils.PlotUtils;

public class DriveToPath extends CommandBase implements Constants {
  private final RamseteController m_ramsete = new RamseteController();
  private final Timer m_timer = new Timer();
  private final DriveTrain m_drive;
  private Trajectory m_trajectory;
  private double runtime;
  private double elapse;
  private double xPath = 3;
  private double yPath = 2;
  private double rotPath = 0;
  //ArrayList<PathData> pathdata = new ArrayList<PathData>();
  public DriveToPath(DriveTrain drive) {
    m_drive = drive;
    addRequirements(m_drive);
    SmartDashboard.putNumber("xPath", xPath);
    SmartDashboard.putNumber("yPath", yPath);
    SmartDashboard.putNumber("rotPath", rotPath);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    xPath = SmartDashboard.getNumber("xPath", xPath);
    yPath = SmartDashboard.getNumber("yPath", yPath);
    rotPath = SmartDashboard.getNumber("rotPath", rotPath);
    //PlotUtils.initPlot();
    //m_trajectory = getStraightPath(2.0);
    // m_trajectory = getSTurn(4, -2);
    m_trajectory = getHookPath(xPath, yPath, rotPath); //y and angle are inverted from "narmal"
    runtime = m_trajectory.getTotalTimeSeconds();
    m_drive.resetOdometry(m_trajectory.getInitialPose());
    m_timer.reset();
    m_timer.start();
    //pathdata.clear();
    elapse = 0;
    //PlotUtils.setInitialPose(m_trajectory.sample(0).poseMeters, DriveTrain.kTrackWidth);
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

  
  public static Trajectory getHookPath(double xend, double yend, double rot) {
    Trajectory Traj = TrajectoryGenerator.generateTrajectory(
      new Pose2d(0, 0, new Rotation2d(0)),
      List.of(),
      new Pose2d(xend, yend, new Rotation2d(rot)),
      new TrajectoryConfig(DriveTrain.kMaxSpeed, DriveTrain.kMaxAcceleration)
    );
    return Traj;
  }

  public static Trajectory getSTurn(double xend, double yend) {
    Trajectory Traj = TrajectoryGenerator.generateTrajectory(
      new Pose2d(0, 0, new Rotation2d(0)),
      List.of(),
      new Pose2d(xend, yend, new Rotation2d(0.0)),
      new TrajectoryConfig(DriveTrain.kMaxSpeed, DriveTrain.kMaxAcceleration)
    );
    return Traj;
  }
  
/*
  private void plotPosition(Trajectory.State state) {
    PathData pd = PlotUtils.plotDistance(
      state.timeSeconds,
      state.poseMeters,
      m_drive.getLeftDistance(),
      m_drive.getRightDistance(),
      m_drive.getHeading(),
      DriveTrain.kTrackWidth);
      pathdata.add(pd);
  }
  */
  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    elapse = m_timer.get();
    Trajectory.State reference = m_trajectory.sample(elapse);
    ChassisSpeeds speeds = m_ramsete.calculate(m_drive.getPose(), reference);
    
    m_drive.odometryDrive(speeds.vxMetersPerSecond, speeds.omegaRadiansPerSecond); //add neg maybe?
    //plotPosition(reference);
  }

  /*
  @Override
  public boolean isFinished() {
    if (elapse >= 1.01*runtime) {
      return true;
    }
    return false;
  }
  
  @Override
  public void end(boolean interrupted) {
    final String label_list[] = {"position plot", "x", "y", "left wheels", "target", "center", "target", "right wheels", "target"};
    PlotUtils.genericXYPlot(pathdata, label_list, 6);
  }
*/

} 