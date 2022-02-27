// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.objects.Piston;
import frc.robot.objects.SparkMotor;

public class Climber extends SubsystemBase implements Constants{
  /** Creates a new Climber. */
  SparkMotor m_lifter=new SparkMotor(CLIMBER);
  Piston m_piston=new Piston(1);
  boolean m_arms_out;
  double setval=0;
  double adjust_delta=0.01;
  double max_setpoint=1;
  double min_setpoint=0;
  final PIDController m_controller=new PIDController(0.5,0.5,0);
  
  public Climber() {
    SmartDashboard.putBoolean("Arms out",m_arms_out);
    SmartDashboard.putNumber("Lifter",0);
    m_lifter.setDistancePerRotation(1);
    m_controller.setTolerance(0.02, 0.05);
    m_controller.setIntegratorRange(-5, 1.0);
    m_lifter.setInverted();
    m_arms_out=false;
    reset();
  }

  public void adjustSetpoint(double f){
    setval+=f*adjust_delta;
    setval=setval<min_setpoint?min_setpoint:setval;
    setval=setval>max_setpoint?max_setpoint:setval;
  }
  public void init(){
    m_lifter.reset();
    m_lifter.enable();
    armsIn();
  }
  public void enable(){
    m_lifter.enable();
    m_piston.enable();
  }

  public void reset(){
    m_lifter.reset();
    setval=0.0;
    m_controller.reset();
    //setLifterDown(0.1);
  }
  public void disable(){
    m_lifter.disable();
    m_piston.disable();
  }
 
  public void setLifterUp(double f){
    adjustSetpoint(f);
  }
  public void setLifterDown(double f){
    adjustSetpoint(-f);
  }

  public void setLifter(double f){
    setval=f;
  }
  public void armsOut(){
    m_arms_out=true;
    m_piston.setForward();
  }
  public void armsIn(){
    m_arms_out=false;
    m_piston.setReverse();
  }

  @Override
  public void periodic() {
    double distance=-m_lifter.getDistance();
    SmartDashboard.putNumber("Lifter",distance);
    SmartDashboard.putNumber("Setval",setval);
    SmartDashboard.putBoolean("Arms out",m_arms_out);
    double correction=m_controller.calculate(distance,setval);
    m_lifter.set(-correction);
  }
}
