// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.AutoTarget;


public class Autonomous extends SubsystemBase {
DriveTrain m_drive;
Aiming m_aim;
Shooting m_shoot;
  /** Creates a new Automonous. */
  public Autonomous(DriveTrain drive, Aiming aim, Shooting shoot) {
  m_drive = drive;
  m_aim = aim;
  m_shoot = shoot;

  

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public CommandGroupBase getCommand() {
    
    return new SequentialCommandGroup(new AutoTarget(m_drive, m_aim, m_shoot));
  }
}
