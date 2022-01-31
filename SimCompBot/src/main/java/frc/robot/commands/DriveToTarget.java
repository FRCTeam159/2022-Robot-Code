// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Targeting;

public class DriveToTarget extends CommandBase {
  boolean test=true;

  // - add front target geometry (size of target, width/height, distance grom ground)
  // - add camera specs fov aspect
  // - add camera geometry height angle of camera position
  
  private final Drivetrain m_drive;
  private final Targeting m_targeting;
  public DriveToTarget(Drivetrain drive,Targeting targeting) {
    m_drive=drive;
    m_targeting=targeting;
    addRequirements(drive,targeting);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("DriveToTarget.start");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("DriveToTarget.end");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return test?true:false;
  }
}
