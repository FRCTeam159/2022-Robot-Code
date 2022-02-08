// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Aiming;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooting;

public class AutoTarget extends CommandBase {
  DriveTrain m_drive;
  Aiming m_aim;
  Shooting m_shoot;
  Timer m_timer = new Timer();
  double runtime;
  boolean haveTarget;


  /** Creates a new AutoTarget. */
  public AutoTarget(DriveTrain drive, Aiming aim, Shooting shoot) {
    m_drive = drive;
    m_aim = aim;
    m_shoot = shoot;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(aim, drive, shoot);
  
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    runtime = 0;
    m_aim.aimOn();
    m_timer.start();
    m_shoot.setIntakeReverse();
    haveTarget = false;
    System.out.println("Shoot started");

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   
    if (m_timer.get() > 0.5 && !m_shoot.isShooterOn()) {
      m_shoot.setShooterOn();
    }
    m_aim.adjust();
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_aim.aimOff();
    m_shoot.setShooterOff();
    m_shoot.setIntakeOff();
    runtime += m_timer.get();
    System.out.println("Shoot finished " + runtime);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean onTarget = m_aim.onTarget();
    if (onTarget && !haveTarget) {
      runtime = m_timer.get();
      System.out.println("Shoot, target acquired - 1 " + m_timer.get());
      m_timer.reset();
      m_shoot.setIntakeOn();
      haveTarget = true;
    
    }
    if (haveTarget && m_timer.get() > 1) {
      return true;
    }
    return false;
    

  }
}
