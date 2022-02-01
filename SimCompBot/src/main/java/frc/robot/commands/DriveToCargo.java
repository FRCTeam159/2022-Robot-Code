// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Targeting;

public class DriveToCargo extends CommandBase {
  boolean test=true;
  private final Drivetrain m_drive;
  private final Targeting m_targeting;
  public DriveToCargo(Drivetrain drive,Targeting targeting) {
    m_drive=drive;
    m_targeting=targeting;
    addRequirements(drive,targeting);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("DriveToCargo.start");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("DriveToCargo.end");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return test?true:false;
  }
}