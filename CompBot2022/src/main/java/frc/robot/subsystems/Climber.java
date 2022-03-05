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
  final PIDController m_controller = new PIDController(2, 0.5, 0.0);
  double inchesPerRev=0.250;
  SparkMaxLimitSwitch m_forwardLimit;
  SparkMaxLimitSwitch m_reverseLimit;

  public Climber() {
    leftHook.setDistancePerRotation(1);
    rightHook.setDistancePerRotation(1);
    m_controller.setTolerance(0.01, 0.001);
    m_controller.setIntegratorRange(-4, 4);
    //are we gonna have to do this?
    leftHook.setInverted();
    rightHook.setInverted();
    leftHook.setDistancePerRotation(0.0254*inchesPerRev);
    rightHook.setDistancePerRotation(0.0254*inchesPerRev);
    m_forwardLimit = rightHook.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    m_reverseLimit = rightHook.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    m_forwardLimit.enableLimitSwitch(true);
    m_reverseLimit.enableLimitSwitch(true);

    setVal = 0;
 

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

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
    double distance = -rightHook.getDistance();
    double correction = m_controller.calculate(distance, setVal);
    leftHook.set(correction);
    rightHook.set(correction);
    SmartDashboard.putNumber("Lifter",distance);
    SmartDashboard.putNumber("Setval",setVal);
    SmartDashboard.putBoolean("Arms out",armsOut);
    SmartDashboard.putBoolean("Right Forward Limit Switch", m_forwardLimit.isPressed());
    SmartDashboard.putBoolean("Right Reverse Limit Switch", m_reverseLimit.isPressed());
    //SmartDashboard.putBoolean("Forward Limit Enabled", m_forwardLimit.isLimitSwitchEnabled());
    //SmartDashboard.putBoolean("Reverse Limit Enabled", m_reverseLimit.isLimitSwitchEnabled());
  
  }
}
