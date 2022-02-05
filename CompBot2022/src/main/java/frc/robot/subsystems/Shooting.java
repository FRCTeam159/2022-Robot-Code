// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooting extends SubsystemBase implements Constants {
  /** Creates a new Intake. */
  private SparkMotor intake;
  private SparkMotor shoot;
  private boolean intake_is_on = false;
  private boolean shooter_is_on = false;

  public Shooting() {
    intake = new SparkMotor(INTAKE);
    shoot = new SparkMotor(SHOOTER);
  }

  public void setIntakeOn() {
    intake.set(-0.5);
    intake_is_on = true;
  }

  public void setIntakeOff() {
    intake.set(0);
    intake_is_on = false;
  }

  public void setShooterOn() {
    shoot.set(-0.5);
    shooter_is_on = true;
  }

  public void setShooterOff() {
    shoot.set(0);
    shooter_is_on = false;
  }

  public boolean isIntakeOn() {
    return intake_is_on;
  }

  public boolean isShooterOn() {
    return shooter_is_on;
  }

  public boolean haveBall() {
    return true; //change this stuff whatnot
  }

  public boolean dummyAimDone() {
    return false; //change this stuff whatnot
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
