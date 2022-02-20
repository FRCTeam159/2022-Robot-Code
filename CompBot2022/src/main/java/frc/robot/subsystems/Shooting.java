// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
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
  public static double kIntakeBackward = 0.12;
  public static double kShootSpeed = -1.5;

  public Shooting() {
    intake = new SparkMotor(INTAKE);
    shoot = new SparkMotor(SHOOTER);
    SmartDashboard.putNumber("shooter speed", 0);
    SmartDashboard.putNumber("intake speed", 0);
    SmartDashboard.putNumber("spinback Speed", 0.12);
  }

  public boolean ballCapture() {
    return m_dio.get();
  }

  public void setIntakeHold() {
    intake.set(kIntakeBackward);
    intake_is_on = true;
  }

  public void setIntakeOn() {
    intake.set(kIntakeForward);
    intake_is_on = true;
  }

  public void setShooter(double speed) {
    shoot.set(speed);

  }

  public double getIntake() {
    return intake.get();
  }

  public double getShoot() {
    return shoot.getRate();
  }

  public void setIntakeOff() {
    intake.set(0);
    intake_is_on = false;
  }

  public void setShooterOn() {
    setShooter(kShootSpeed);
    shooter_is_on = true;
  }

  public void setShooterOff() {
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
    SmartDashboard.putNumber("intake speed", intake.getRate());
    SmartDashboard.putNumber("shooter speed", shoot.getRate());
    kIntakeBackward = SmartDashboard.getNumber("spinback Speed", 0.12);
  }
}
