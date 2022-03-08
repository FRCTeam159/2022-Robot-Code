// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.SparkMaxLimitSwitch;

public class Climber extends SubsystemBase {
  /** Creates a new Climber. */
  SparkMotor leftHook = new SparkMotor(5);
  SparkMotor rightHook = new SparkMotor(6);
  DoubleSolenoid arms = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 3);
  double setVal = 0;
  double delta = 0.005;
  double max = 0.52;
  double min = 0;
  boolean armsOut;
  final PIDController m_rightcontroller = new PIDController(2, 0.5, 0.0);
  final PIDController m_leftcontroller = new PIDController(2, 0.5, 0.0);
  double inchesPerRev=0.250;
  SparkMaxLimitSwitch m_rightforwardLimit;
  SparkMaxLimitSwitch m_rightreverseLimit;
  SparkMaxLimitSwitch m_leftforwardLimit;
  SparkMaxLimitSwitch m_leftreverseLimit;
  public boolean foundZero;

  public Climber() {
    leftHook.setDistancePerRotation(1);
    rightHook.setDistancePerRotation(1);
    m_rightcontroller.setTolerance(0.01, 0.001);
    m_rightcontroller.setIntegratorRange(-4, 4);
    m_leftcontroller.setTolerance(0.01, 0.001);
    m_leftcontroller.setIntegratorRange(-4, 4);
    leftHook.setInverted();
    rightHook.setInverted();
    leftHook.setDistancePerRotation(0.0254*inchesPerRev);
    rightHook.setDistancePerRotation(0.0254*inchesPerRev);
    m_rightforwardLimit = rightHook.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    m_rightreverseLimit = rightHook.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    m_rightforwardLimit.enableLimitSwitch(true);
    m_rightreverseLimit.enableLimitSwitch(true);
    m_leftforwardLimit = leftHook.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    m_leftreverseLimit = leftHook.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    m_leftforwardLimit.enableLimitSwitch(true);
    m_leftreverseLimit.enableLimitSwitch(true);

    setVal = 0;
    foundZero = false;

    leftHook.reset();
    rightHook.reset();
  }

  public void adjustSetpoint(double d){
    setVal += d*delta;
    setVal = setVal<min?min:setVal;
    setVal = setVal>max?max:setVal;
  }

  public void extendHook(double v){
    adjustSetpoint(v);
  }

  public void contractHook(double v){
    adjustSetpoint(-v);
  }

  public void extendHook(){
    setVal=max;
  }

  public void contractHook(){
    setVal=0;
  }

  public void armsOut(){
    armsOut=true;
    arms.set(Value.kForward);
  }

  public void armsIn(){
    armsOut=false;
    arms.set(Value.kReverse);
  }

  public void init(){
    leftHook.reset();
    rightHook.reset();
  }

  public void findZero(){
    if(!atZero()){
      contractHook();
      foundZero = false;
    } else {
      foundZero = true;
    }
  }

  public boolean atZero(){
    return m_rightreverseLimit.isPressed() && m_leftreverseLimit.isPressed();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(!foundZero){
      findZero();
    }


    double distance = -rightHook.getDistance();
    double correction = m_rightcontroller.calculate(distance, setVal);
    rightHook.set(correction);
    SmartDashboard.putNumber("right distance",distance);
    distance = -leftHook.getDistance();
    correction = m_leftcontroller.calculate(distance, setVal);
    leftHook.set(correction);
    SmartDashboard.putNumber("left distance",distance);
    SmartDashboard.putNumber("Setval",setVal);
    SmartDashboard.putBoolean("Arms out",armsOut);
    SmartDashboard.putBoolean("Right Forward Limit Switch", m_rightforwardLimit.isPressed());
    SmartDashboard.putBoolean("Right Reverse Limit Switch", m_rightreverseLimit.isPressed());
    SmartDashboard.putBoolean("left Forward Limit Switch", m_leftforwardLimit.isPressed());
    SmartDashboard.putBoolean("left Reverse Limit Switch", m_leftreverseLimit.isPressed());
    //SmartDashboard.putBoolean("Forward Limit Enabled", m_forwardLimit.isLimitSwitchEnabled());
    //SmartDashboard.putBoolean("Reverse Limit Enabled", m_reverseLimit.isLimitSwitchEnabled());
  
  }
}
