// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  /** Creates a new Climber. */
  SparkMotor leftClimber = new SparkMotor(5);
  SparkMotor rightClimber = new SparkMotor(6);
  DoubleSolenoid pivot = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 3);
  double setVal = 0;
  double delta = 0.005;
  double max = 1.05;
  double min = 0;
  boolean armsIn;
  final PIDController m_controller = new PIDController(1, 0.5, 0.0);

  public Climber() {
    leftClimber.setDistancePerRotation(1);
    rightClimber.setDistancePerRotation(1);
    m_controller.setTolerance(0.05, 0.05);
    m_controller.setIntegratorRange(-4, 4);
    //are we gonna have to do this?
    leftClimber.setInverted();
    rightClimber.setInverted();
  }

  public void adjustSetpoint(double d){
    setVal += d*delta;
    setVal = setVal<min?min:setVal;
    setVal = setVal>max?max:setVal;
  }

  public void init(){
    leftClimber.reset();
    rightClimber.reset();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double distance = -leftClimber.getDistance();
    double correction = m_controller.calculate(distance, setVal);
    leftClimber.set(-correction);
    rightClimber.set(-correction);
  }
}
