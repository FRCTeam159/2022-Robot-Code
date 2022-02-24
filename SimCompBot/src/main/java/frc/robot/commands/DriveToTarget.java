// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.objects.TargetSpecs;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Shooting;
import frc.robot.subsystems.Targeting;
import frc.robot.subsystems.Timing;

public class DriveToTarget extends CommandBase {
  double runtime;
  boolean haveTarget;
  boolean shooting;
  boolean starting;
  
  private final Targeting m_aim;
  private final Shooting m_shoot;
  boolean have_ball=false;
 // Timer m_timer = new Timer();
  protected TargetSpecs target_info=new TargetSpecs();

  public DriveToTarget(Targeting targeting, Shooting shoot) {
    m_aim=targeting;
    m_shoot=shoot;

    //m_timer.start();
    addRequirements(shoot,targeting);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("DriveToTarget.start");
  
    have_ball=m_shoot.isBallCaptured();
    m_shoot.setIntakeHold();
    //m_timer.reset();
    Timing.reset();
    haveTarget=false;
    shooting=false;
    starting=false;
    runtime = 0;
    target_info.idealA=1;
    target_info.idealX=0;
    target_info.idealY=-7;
    target_info.yTol=2;
    target_info.xTol=2;
    target_info.xScale=1.5;
    target_info.yScale=1;
    Targeting.setTargetSpecs(target_info);
    Targeting.setFrontTarget(true);
    Targeting.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(!haveTarget)
      m_aim.adjust();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Targeting.disable();
    m_shoot.setShooterOff();
    m_shoot.setIntakeOff();
    runtime += Timing.get();
    Autonomous.totalRuntime += runtime;
    System.out.println("DriveToTarget.end " + runtime + " total time: " + Autonomous.totalRuntime);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(!have_ball || Autonomous.autoFailed)
      return true;
    boolean onTarget = m_aim.onTarget();
    if (onTarget && !haveTarget) {
      runtime = Timing.get();
      System.out.println("Target acquired time:" + runtime);
      Timing.reset();
      m_shoot.setShooterOn();
      haveTarget = true;  
    }
    if (!shooting && haveTarget && m_shoot.shooterReady()) {
      m_shoot.setIntakeOn();
      System.out.println("Shoot started:" + Timing.get() + " shooterspeeed: " + m_shoot.shooterSpeed());
      shooting=true;
      Timing.reset();
    }
    if (shooting && Timing.get() >Shooting.kIntakeRunUpTime && !m_shoot.isBallCaptured()) {
      System.out.println("Done time:" + Timing.get());
      return true;
    }
    return false;   
  }
}
