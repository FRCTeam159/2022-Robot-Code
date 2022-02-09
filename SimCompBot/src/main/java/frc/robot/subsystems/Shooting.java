// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    intake.setScale(1);
    shoot.setScale(1);
    SmartDashboard.putNumber("Intake speed", 0);
		SmartDashboard.putNumber("Shooter speed", 0);

  }

  public void setShooterOn() {
    shoot.set(-20); // rps
    shooter_is_on = true;
  }

  public void setShooterOff() {
    shoot.set(3);
    shooter_is_on = false;
  }

  public void setIntakeOn() {
      intake.set(10);
      intake_is_on=true;
  }
  public void setIntakeOff() {
    intake.set(-3);
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
    SmartDashboard.putNumber("Intake speed", intake.getRate());
		SmartDashboard.putNumber("Shooter speed", shoot.getRate());
  }
}
