// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Aiming extends SubsystemBase implements Constants {

  private final PIDController m_turnController = new PIDController(0.02, 0.0, 0.0);
  private final PIDController m_moveController = new PIDController(0.03, 0.005, 0.0);
  public double aimX;
  DriveTrain m_drive;
  Limelight m_limelight;
  public final double idealDistance = 102; // Ideal Distance limelight to target, inches
  // public final double limelightArea =
  // 4*idealDistance*idealDistance*Math.tan(29.8)*Math.tan(29.2); //inches^2
  public final double iA = 75.1;
  public final double iB = 0.989;
  public final double iC = -24.5;
  public final double idealX = 0; // future robotics problem
  public final double idealY = (!flagPancake)? -1.3 : 9;
  //iA * Math.pow(iB, idealDistance) + iC; //in the format a^b + c

  public Aiming(DriveTrain D, Limelight limelight) {
    m_drive = D;
    m_limelight = limelight;
    m_turnController.enableContinuousInput(-29.8, 29.8);
    m_turnController.setTolerance(1.0, 0.05);
    m_moveController.enableContinuousInput(-24.85, 24.85);
    m_moveController.setTolerance(1.0, 0.1);
  }

  public void adjust() {
    // double heading = m_drive.getHeading();
    double correctionX = m_turnController.calculate(m_limelight.limeX, idealX);
    double correctionY = m_moveController.calculate(m_limelight.limeY, idealY);
    m_drive.arcadeDrive(correctionY, -correctionX);
    //System.out.println(m_limelight.limeX + ": " + m_limelight.limeY);
    //System.out.println(correctionX + ": " + correctionY);
  }


  public boolean turnDoneX() {
    return m_turnController.atSetpoint();
  }

  public boolean turnDoneY() {
    return m_moveController.atSetpoint();
  }

  public boolean seeTarget() {
    return m_limelight.limeV == 1;
  }

  
  public boolean onTarget() {
    boolean onTargetTurn = turnDoneX();
    boolean onTargetMove = turnDoneY();
    return onTargetTurn && onTargetMove;
  }


  public void aimOff() {
    m_limelight.limelightOff();
  }


  
  public void aimOn() {
    m_limelight.limelightOn();
    m_turnController.reset();
    m_moveController.reset();
  }
  

  public void aimDrive() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
