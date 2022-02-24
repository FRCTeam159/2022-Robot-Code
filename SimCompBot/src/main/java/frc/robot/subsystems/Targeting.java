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
import frc.robot.objects.TargetSpecs;

public class Targeting extends SubsystemBase {

  protected static final NetworkTableInstance inst = NetworkTableInstance.getDefault();
  protected static final NetworkTable m_target_data = inst.getTable("TargetData");
  protected static final NetworkTable m_target_specs = inst.getTable("TargetSpecs");

  private final static PIDController m_moveXController = new PIDController(0.08, 0.0, 0);
  private final static PIDController m_moveYController = new PIDController(0.1, 0.0, 0.0);
  private final static PIDController m_moveAController = new PIDController(0.01, 0.0, 0.0);

  DriveTrain m_drive;

  private static NetworkTableEntry tc; // camera (0=front 1=back)
  private static NetworkTableEntry ta; // area
  private static NetworkTableEntry tx; // x-offset to target
  private static NetworkTableEntry ty; // y-offset to target
  private static NetworkTableEntry tr; // tarket skew
  private static NetworkTableEntry tv; // true if target is present

  private static NetworkTableEntry idealX;
  private static NetworkTableEntry idealY;
  private static NetworkTableEntry idealA;
  private static NetworkTableEntry useArea;
  private static NetworkTableEntry xTol;
  private static NetworkTableEntry yTol;
  private static NetworkTableEntry aTol;
  private static NetworkTableEntry xScale;
  private static NetworkTableEntry yScale;

  protected static TargetData target = new TargetData();
  protected static TargetSpecs target_info = new TargetSpecs();

  protected static double correctionMove = 0;
  protected static double correctionTurn = 0;

  public Targeting(DriveTrain D) {
    m_drive = D;
    m_moveXController.enableContinuousInput(-50, 50);
    m_moveXController.setTolerance(2, 0.5);
    m_moveYController.enableContinuousInput(-50, 50);
    m_moveYController.setTolerance(2, 0.5);
    //m_moveAController.enableContinuousInput(0, 101);
    m_moveAController.setTolerance(20, 5);
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
    }
    else
      correctionMove = -m_moveYController.calculate(targetOffsetY(), target_info.idealY);
    m_drive.arcadeDrive(target_info.yScale*correctionMove, target_info.xScale*correctionTurn);
  }

  public static boolean haveTarget() {
    return target.tv;
  }

  public static boolean frontCamera() {
    return target.tc;
  }

  public static void setFrontTarget(boolean front) {
    target.tc=front;
    tc.setBoolean(target.tc);
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

  public static double targetOffsetX() {
    return target.tx;
  }

  public static double targetOffsetY() {
    return target.ty;
  }

  public static double targetArea() {
    return target.ta;
  }

  public boolean onTarget() {
    boolean onTargetTurn = turnDoneX();
    boolean onTargetMove = turnDoneY();
    return onTargetTurn && onTargetMove;
  }

  public static void setTargetSpecs(TargetSpecs specs) {
    target_info=specs;
    setTargetSpecs();
  }
  
  private static void setTargetSpecs() {
    idealX=m_target_specs.getEntry("idealX");
    idealX.setDouble(target_info.idealX);
    idealY=m_target_specs.getEntry("idealY");
    idealY.setDouble(target_info.idealY);
    idealA=m_target_specs.getEntry("idealA");
    idealA.setDouble(target_info.idealA);
    useArea=m_target_specs.getEntry("useArea");
    useArea.setBoolean(target_info.useArea);
    xTol=m_target_specs.getEntry("xTol");
    xTol.setDouble(target_info.xTol);
    yTol=m_target_specs.getEntry("yTol");
    yTol.setDouble(target_info.yTol);
    aTol=m_target_specs.getEntry("aTol");
    aTol.setDouble(target_info.aTol);
    xScale = m_target_specs.getEntry("xScale");
    xScale.setDouble(target_info.xScale);
    yScale = m_target_specs.getEntry("yScale");
    yScale.setDouble(target_info.yScale);
  }
  protected static void getTargetData() {
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

  protected static void getTargetSpecs() {
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
    xScale = m_target_specs.getEntry("xScale");
    target_info.xScale = xScale.getDouble(1.0);
    yScale = m_target_specs.getEntry("yScale");
    target_info.yScale = yScale.getDouble(1.0);
  }

  public static void reset(){
    m_moveXController.reset();
    m_moveYController.reset();
    m_moveAController.reset();
  }
  public static void enable() {
    reset();
    m_moveXController.setTolerance(target_info.xTol, 0.1 * target_info.xTol);
    m_moveYController.setTolerance(target_info.yTol, 0.1 * target_info.yTol);
    m_moveAController.setTolerance(target_info.aTol, 0.1 * target_info.aTol);
  }

  public static void disable() {
    reset();
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

}
