// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.objects.SparkMotor;

public class Shooting extends SubsystemBase implements Constants {
  /** Creates a new Intake. */
  private SparkMotor intake;
  private SparkMotor shoot;
  private boolean intake_is_on=false;
  private boolean shooter_is_on = false;

  public Shooting() {
    intake = new SparkMotor(INTAKE);
    shoot = new SparkMotor(SHOOTER);
    intake.enable();
    shoot.enable();
    setIntakeOff();
    setShooterOff();
    intake.setScale(3.5);

  }

  public void setShooterOn() {
    shoot.set(-2);
    shooter_is_on = true;
  }

  public void setShooterOff() {
    shoot.set(0.1);
    shooter_is_on = false;
  }

  public void setIntakeOn() {
      intake.set(1);
      intake_is_on=true;
  }
  public void setIntakeOff() {
    intake.set(-0.5);
    intake_is_on=false;
 } 
 public boolean isIntakeOn(){
   return intake_is_on;
 }
 public boolean isShooterOn() {
  return shooter_is_on;
}
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
