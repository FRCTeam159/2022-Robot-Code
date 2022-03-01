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

public class DriveToCargo extends CommandBase {
  boolean test=true;
  private final Targeting m_aim;
  private final Shooting m_shoot;
  protected TargetSpecs target_info=new TargetSpecs();
  boolean have_ball;
  double runtime;
  boolean haveTarget=false;
  private double init_delay=0.05;
  private boolean initialized=false;

  public DriveToCargo(Targeting targeting, Shooting shoot) {
    m_aim=targeting;
    m_shoot=shoot;
    //m_timer.start();
    addRequirements(shoot,targeting);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("DriveToCargo.start");
    target_info.idealA=110;
    target_info.idealX=0;
    target_info.idealY=0;
    target_info.useArea=true;
    target_info.aTol=10;
    target_info.xScale=0.25;
    target_info.yScale=0.7;
    have_ball=false;
    Targeting.setFrontTarget(false);
    Targeting.setTargetSpecs(target_info);
    m_shoot.setIntakeOn();
    Timing.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    have_ball=m_shoot.isBallCaptured();
    if(Timing.get()>init_delay && !initialized){
      Targeting.enable();
      initialized=true;
    }
    else if(!have_ball)
      m_aim.adjust();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    runtime = Timing.get();
    //if (m_shoot.isBallCaptured())
      m_shoot.setIntakeHold();
    //else
    //  m_shoot.setIntakeOff();
    Autonomous.totalRuntime += runtime;
    System.out.println("DriveToCargo.end " + runtime + " total time: " + Autonomous.totalRuntime);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(have_ball || Autonomous.autoFailed)
      return true;
    boolean onTarget = m_aim.onTarget();
    if (onTarget && !haveTarget) {
      runtime = Timing.get();
      System.out.println("Target acquired time:" + runtime);
      haveTarget = true;  
    }
    if(Timing.get()>10){
      runtime=Timing.get();
      Autonomous.autoFailed=true;
      System.out.println("Failed to capture ball:" + runtime);
    }
    return false;
  }
}
