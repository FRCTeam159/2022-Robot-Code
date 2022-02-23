// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class Timing extends SubsystemBase {
  private static final Timer m_timer = new Timer();
  /** Creates a new Timing. */
  public Timing() {
    m_timer.start();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public static void reset(){

    m_timer.reset();
    if(!Robot.isReal())
      Simulation.start();
  }
  public static double getTime(){
    if(Robot.isReal())
      return m_timer.get();
    else
      return Simulation.getSimTime();
  }
}
