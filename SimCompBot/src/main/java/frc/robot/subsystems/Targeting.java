// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.objects.TargetData;

public class Targeting extends SubsystemBase {

  protected static final NetworkTableInstance inst = NetworkTableInstance.getDefault();
  protected static final NetworkTable m_target_table=inst.getTable("TargetData");

  private final PIDController m_turnController = new PIDController(0.03, 0.02, 0);
  private final PIDController m_moveController = new PIDController(0.02, 0.0, 0.0);
  public double aimX;
  DriveTrain m_drive;

  public final double idealDistance = 57; // Ideal Distance limelight to target, inches
  // public final double limelightArea =
  // 4*idealDistance*idealDistance*Math.tan(29.8)*Math.tan(29.2); //inches^2
  public final double iA = 102.15;
  public final double iB = 0.95;
  public final double iC = 3.22;
  public final double idealX = -2; // future robotics problem
  public final double idealY = 4;//iA * Math.pow(iB, idealDistance) + iC; //in the format a^b + c
  public boolean isTurn;

  private NetworkTableEntry ta;
  private NetworkTableEntry tx;
  private NetworkTableEntry ty;
  private NetworkTableEntry tv;
  private NetworkTableEntry tr;

  protected TargetData target=new TargetData();

  public Targeting(DriveTrain D) {
    m_drive = D;
    m_turnController.enableContinuousInput(-50, 50);
    m_turnController.setTolerance(1, 0.1);
    m_moveController.enableContinuousInput(-50, 50);
    m_moveController.setTolerance(1, 0.1);
    SmartDashboard.putNumber("Xoffset", 0);
    SmartDashboard.putNumber("Yoffset", 0);
    SmartDashboard.putBoolean("Target", false);
  }

  public void adjust() {
    // double heading = m_drive.getHeading();
    double correctionX = -m_turnController.calculate(targetOffsetX(), idealX);
    double correctionY = -m_moveController.calculate(targetOffsetY(), idealY);
    //m_drive.arcadeDrive(correctionY, -correctionX);
    m_drive.arcadeDrive(correctionY, -correctionX);

    //System.out.println(targetOffsetX() + ": " + correctionX);
  }

 public boolean haveTarget(){
   return target.tv;
 }
  public boolean turnDoneX() {
    return m_turnController.atSetpoint();
  }

  public boolean turnDoneY() {
    return m_moveController.atSetpoint();
  }

  public double targetOffsetX() {
    return target.tx;
  }

  public double targetOffsetY() {
    return target.ty;
  }

  public boolean onTarget() {
    boolean onTargetTurn = turnDoneX();
    boolean onTargetMove = turnDoneY();
    return onTargetTurn && onTargetMove;
  }

  @Override
  public void periodic() {
    ta= m_target_table.getEntry("ta");
    target.ta=ta.getDouble(0);
    tx= m_target_table.getEntry("tx");
    target.tx=tx.getDouble(0);
    ty= m_target_table.getEntry("ty");
    target.ty=ty.getDouble(0);
    tr= m_target_table.getEntry("tr");
    target.tr=tr.getDouble(0);
    tv= m_target_table.getEntry("tv");
    target.tv=tv.getBoolean(false);
    SmartDashboard.putNumber("Xoffset", target.tx);
    SmartDashboard.putNumber("Yoffset", target.ty);
    SmartDashboard.putBoolean("Target", target.tv);
  }

  public void aimOn() {
  }
}
