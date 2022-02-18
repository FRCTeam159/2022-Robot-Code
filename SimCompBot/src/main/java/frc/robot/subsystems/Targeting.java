// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.objects.TargetData;
import frc.robot.objects.TargetSpecs;

public class Targeting extends SubsystemBase {

  protected static final NetworkTableInstance inst = NetworkTableInstance.getDefault();
  protected static final NetworkTable m_target_data = inst.getTable("TargetData");
  protected static final NetworkTable m_target_specs = inst.getTable("TargetSpecs");

  private final PIDController m_moveXController = new PIDController(0.03, 0.01, 0);
  private final PIDController m_moveYController = new PIDController(0.03, 0.00, 0.0);
  private final PIDController m_moveAController = new PIDController(0.01, 0.00, 0.0);

  DriveTrain m_drive;

  private NetworkTableEntry tc; // camera (0=front 1=back)
  private NetworkTableEntry ta; // area
  private NetworkTableEntry tx; // x-offset to target
  private NetworkTableEntry ty; // y-offset to target
  private NetworkTableEntry tr; // tarket skew
  private NetworkTableEntry tv; // true if target is present

  private NetworkTableEntry idealX;
  private NetworkTableEntry idealY;
  private NetworkTableEntry idealA;
  private NetworkTableEntry useArea;
  private NetworkTableEntry xTol;
  private NetworkTableEntry yTol;
  private NetworkTableEntry aTol;

  protected TargetData target = new TargetData();
  protected TargetSpecs target_info = new TargetSpecs();

  protected double correctionMove = 0;
  protected double correctionTurn = 0;

  public Targeting(DriveTrain D) {
    m_drive = D;
    m_moveXController.enableContinuousInput(-50, 50);
    m_moveXController.setTolerance(1, 0.1);
    m_moveYController.enableContinuousInput(-50, 50);
    m_moveYController.setTolerance(2, 0.1);
    //m_moveAController.enableContinuousInput(0, 101);
    m_moveAController.setTolerance(10, 1);
    SmartDashboard.putNumber("Xoffset", 0);
    SmartDashboard.putNumber("Yoffset", 0);
    SmartDashboard.putNumber("Area", 0);
    SmartDashboard.putBoolean("Target", false);
    SmartDashboard.putString("Status", "");
  }

  public void adjust() {
    correctionTurn = m_moveXController.calculate(targetOffsetX(), target_info.idealX);
    if (target_info.useArea){
      correctionMove = -m_moveAController.calculate(targetArea(), target_info.idealA);
      //System.out.println(targetArea()+" "+correctionMove);
    }
    else
      correctionMove = -m_moveYController.calculate(targetOffsetY(), target_info.idealY);
    
    
    m_drive.arcadeDrive(correctionMove, correctionTurn);
  }

  public boolean haveTarget() {
    return target.tv;
  }

  public boolean frontTarget() {
    return target.tc;
  }

  public boolean turnDoneX() {
    return m_moveXController.atSetpoint();
  }

  public boolean turnDoneY() {
    if (target_info.useArea)
      return m_moveAController.atSetpoint();
    else
      return m_moveYController.atSetpoint();
  }

  public double targetOffsetX() {
    return target.tx;
  }

  public double targetOffsetY() {
    return target.ty;
  }

  public double targetArea() {
    return target.ta;
  }

  public boolean onTarget() {
    boolean onTargetTurn = turnDoneX();
    boolean onTargetMove = turnDoneY();
    return onTargetTurn && onTargetMove;
  }

  @Override
  public void periodic() {
    getTargetSpecs();
    getTargetData();

    SmartDashboard.putNumber("Xoffset", target.tx);
    SmartDashboard.putNumber("Yoffset", target.ty);
    SmartDashboard.putNumber("Area", target.ta);
    SmartDashboard.putBoolean("Target", target.tv);
  }

  protected void getTargetData() {
    ta = m_target_data.getEntry("ta");
    target.ta = ta.getDouble(0);
    tx = m_target_data.getEntry("tx");
    target.tx = tx.getDouble(0);
    ty = m_target_data.getEntry("ty");
    target.ty = ty.getDouble(0);
    tr = m_target_data.getEntry("tr");
    target.tr = tr.getDouble(0);
    tv = m_target_data.getEntry("tv");
    target.tv = tv.getBoolean(false);
    tc = m_target_data.getEntry("tc");
    target.tc = tc.getBoolean(true);
  }

  protected void getTargetSpecs() {
    idealX = m_target_specs.getEntry("idealX");
    target_info.idealX = idealX.getDouble(0);
    idealY = m_target_specs.getEntry("idealY");
    target_info.idealY = idealY.getDouble(0);
    idealA = m_target_specs.getEntry("idealA");
    target_info.idealA = idealA.getDouble(0);
    useArea = m_target_specs.getEntry("useArea");
    target_info.useArea = useArea.getBoolean(false);

    xTol = m_target_specs.getEntry("xTol");
    target_info.xTol = xTol.getDouble(1.0);
    yTol = m_target_specs.getEntry("yTol");
    target_info.yTol = yTol.getDouble(1.0);
    aTol = m_target_specs.getEntry("aTol");
    target_info.aTol = aTol.getDouble(1.0);
  }

  public void enable() {
    m_moveXController.setTolerance(target_info.xTol, 0.1 * target_info.xTol);
    m_moveYController.setTolerance(target_info.yTol, 0.1 * target_info.yTol);
    m_moveAController.setTolerance(target_info.aTol, 0.1 * target_info.aTol);
  }

  public void disable() {
  }
}
