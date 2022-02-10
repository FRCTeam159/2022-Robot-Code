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
  public static double kIntakeForward = -0.3;
  public static double kIntakeBackward = 0.3;
  public static double kShootSpeed = -0.1;

  public Shooting() {
    intake = new SparkMotor(INTAKE);
    shoot = new SparkMotor(SHOOTER);
    SmartDashboard.putNumber("shooter speed", 0);
    SmartDashboard.putNumber("intake speed", 0);
  }
  public boolean ballCapture() {
    return m_dio.get();
  }

  public void setIntake(double speed) {
    intake.set(speed);
    SmartDashboard.putNumber("intake speed", intake.getRate());
  }

  public void setShooter(double speed) {
    shoot.set(speed);
    SmartDashboard.putNumber("shooter speed", shoot.getRate());
  }

  public void setIntakeOn(double speed) {
    setIntake(speed); //base val -0.5
    intake_is_on = true;
  }

  public void setIntakeOff() {
    setIntake(0);
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
  }
}
