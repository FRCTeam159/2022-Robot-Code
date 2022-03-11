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
  double delta = 0.005; //0.005
  double max = 0.53;
  double min = 0;
  boolean armsOut;
  final PIDController m_rightcontroller = new PIDController(3, 0.1, 0.0);
  final PIDController m_leftcontroller = new PIDController(3, 0.1, 0.0);
  double inchesPerRev=0.20;
  SparkMaxLimitSwitch m_rightforwardLimit;
  SparkMaxLimitSwitch m_rightreverseLimit;
  SparkMaxLimitSwitch m_leftforwardLimit;
  SparkMaxLimitSwitch m_leftreverseLimit;
  public boolean foundZero;
  public boolean terminated;

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
    m_rightforwardLimit.enableLimitSwitch(false);
    m_rightreverseLimit.enableLimitSwitch(true);
    m_leftforwardLimit = leftHook.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    m_leftreverseLimit = leftHook.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    m_leftforwardLimit.enableLimitSwitch(false);
    m_leftreverseLimit.enableLimitSwitch(true);

    setVal = 0;
    foundZero = false;
    terminated = false;

    leftHook.reset();
    rightHook.reset();
  }

  public void adjustSetpoint(double d){
    setVal += d*delta;
    setVal = setVal<min?min:setVal;
    setVal = setVal>max?max:setVal;
  }

  public void extendHook(double v){
    if(leftHook.getDistance() < max)
    adjustSetpoint(v);
  }

  public void contractHook(double v){
    if(!m_leftreverseLimit.isPressed() || !m_rightreverseLimit.isPressed())
    adjustSetpoint(-v);
  }

  public void extendHook(){
    if(leftHook.getDistance() < max)
    setVal=max;
  }

  public void contractHook(){
    if(!m_leftreverseLimit.isPressed() || !m_rightreverseLimit.isPressed())
    setVal=min;
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
      min -= 0.5*delta;
      max -= 0.5*delta;
      if(!m_rightreverseLimit.isPressed()) {
        double distance = -rightHook.getDistance();
        double correction = m_rightcontroller.calculate(distance, min);
        rightHook.set(correction);
      }
      if(!m_leftreverseLimit.isPressed()){
        double distance = -leftHook.getDistance();
        double correction = m_leftcontroller.calculate(distance, min);
        leftHook.set(correction);
      }
      foundZero = false;
    } else {
      leftHook.reset();
      rightHook.reset();
      min = 0;
      max = 0.555;
      setVal = 0;
      foundZero = true;
    }
  }

  public boolean atZero(){
    return m_rightreverseLimit.isPressed() && m_leftreverseLimit.isPressed();
  }

  public void terminate(){
    setVal = max;
    if (m_leftforwardLimit.isPressed() && m_rightforwardLimit.isPressed() && !terminated){
      leftHook.disable();
      rightHook.disable();
      terminated = true;
    }
    if(terminated){
      terminated = false;
      leftHook.disable();
      rightHook.disable();
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    

    if(foundZero){
      double distance = -rightHook.getDistance();
      double correction = m_rightcontroller.calculate(distance, setVal);
      rightHook.set(correction);
      SmartDashboard.putNumber("right climber distance",distance);
      distance = -leftHook.getDistance();
      correction = m_leftcontroller.calculate(distance, setVal);
      leftHook.set(correction);
      SmartDashboard.putNumber("left climber distance",distance);
      //System.out.println(setVal);
    }
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
