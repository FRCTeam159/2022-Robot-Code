// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Aiming extends SubsystemBase {

  private final PIDController m_turnController = new PIDController(0.03, 0.01, 0);
  private final PIDController m_moveController = new PIDController(0.1, 0, 0.0);
  public double aimX;
  DriveTrain m_drive;
  Limelight m_limelight;
  public final double idealDistance = 57; // Ideal Distance limelight to target, inches
  // public final double limelightArea =
  // 4*idealDistance*idealDistance*Math.tan(29.8)*Math.tan(29.2); //inches^2
  public final double idealX = 0; // future robotics problem
  public final double idealY = 102.15 * Math.pow(0.95, idealDistance) + 3.22; // finish later
  public boolean isTurn;

  public Aiming(DriveTrain D, Limelight limelight) {
    m_drive = D;
    m_limelight = limelight;
    m_turnController.enableContinuousInput(-29.8, 29.8);
    m_turnController.setTolerance(1.5, 0.1);
    m_moveController.enableContinuousInput(-24.85, 24.85);
    m_moveController.setTolerance(0.25, 0.1);
  }

  public void adjust() {
    // double heading = m_drive.getHeading();
    double correctionX = m_turnController.calculate(m_limelight.limeX, idealX);
    double correctionY = m_moveController.calculate(m_limelight.limeY, idealY);
    m_drive.arcadeDrive(correctionY, -correctionX);
    //System.out.println(correctionX + ": " + correctionY);
  }


  public boolean turnDoneX() {
    return m_turnController.atSetpoint();
  }

  public boolean turnDoneY() {
    return m_moveController.atSetpoint();
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
  }
  

  public void aimDrive() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
