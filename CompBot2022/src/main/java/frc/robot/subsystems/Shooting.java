// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooting extends SubsystemBase implements Constants {
  /** Creates a new Intake. */
  private SparkMotor intake;
  private SparkMotor shoot;
  private boolean intake_is_on = false;
  private boolean shooter_is_on = false;
  private DigitalInput m_dio = new DigitalInput(0);
  public static double kIntakeForward = -0.4;
  public static double kIntakeBackward = 0.9;
  public static double kShootSpeed = -0.75;
  public double kRunUpTime = 1.9;
  public double kInputHoldTime = 1;
  public DoubleSolenoid intakePiston;

  public Shooting() {
    //PIDController shootPID = new PIDController(0.1, 0, 0);
    if (!flagPancake){
    intake = new SparkMotor(INTAKE);
    shoot = new SparkMotor(SHOOTER);
    intakePiston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 5, 4);
    }
    SmartDashboard.putNumber("shooter speed", 0);
    SmartDashboard.putNumber("intake speed", 0);
    SmartDashboard.putNumber("spinback Speed", 0.12);
  }


  public boolean ballCapture() {
    System.out.println(m_dio.get());
    return m_dio.get();
  }

  public void setIntakeHold() {
    if (!flagPancake)
    intake.set(kIntakeBackward);
    intake_is_on = false;
  }

  public void setIntakeOn() {
    if (!flagPancake)
    intake.set(kIntakeForward);
    intake_is_on = true;
  }

  public void setShooter(double speed) {
    if (!flagPancake)
    shoot.set(speed);
  }

  public double getIntake() {
    if (!flagPancake)
    return intake.get();
    else
    return 0.0;
  }

  public double getShoot() {
    if (!flagPancake)
    return shoot.getRate();
    else
    return 0.0;
  }

  public void setIntakeOff() {
    if (!flagPancake)
    intake.set(0);
    intake_is_on = false;
  }

  public void setShooterOn() {
    if (!flagPancake)
    setShooter(kShootSpeed);
    shooter_is_on = true;
  }

  public void setShooterOff() {
    if (!flagPancake)
    setShooter(0);
    shooter_is_on = false;
  }

  public boolean isIntakeOn() {
    return intake_is_on;
  }

  public boolean isShooterOn() {
    return shooter_is_on;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // System.out.println("ballCapture = " + ballCapture());
    SmartDashboard.putNumber("intake speed", (!flagPancake)? intake.getRate() : 0.0);
    SmartDashboard.putNumber("shooter speed", (!flagPancake)? shoot.getRate() : 0.0);
    kIntakeBackward = SmartDashboard.getNumber("spinback Speed", 0.12);
  }
}
