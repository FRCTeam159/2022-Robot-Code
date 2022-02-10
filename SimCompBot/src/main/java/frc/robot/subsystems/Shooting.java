// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.objects.SparkMotor;
import gazebo.SimContact;

public class Shooting extends SubsystemBase implements Constants {
  /** Creates a new Intake. */
  private SparkMotor intake;
  private SparkMotor shoot;
  private SimContact limit_switch;
  private boolean intake_is_on=false;
  private boolean shooter_is_on = false;
  private boolean ball_captured = false;
  private double shooter_speed=17;
  private double intake_speed=10;
  private double intake_hold=3;
  private double shooter_hold=3;

  public Shooting() {
    intake = new SparkMotor(INTAKE);
    shoot = new SparkMotor(SHOOTER);
    limit_switch=new SimContact(0);
    intake.enable();
    shoot.enable();
    limit_switch.enable();
    setIntakeOff();
    setShooterOff();
    intake.setScale(1);
    shoot.setScale(1);
    //SmartDashboard.putNumber("Intake speed", 0);
		SmartDashboard.putNumber("Shooter speed", 0);
    SmartDashboard.putNumber("Target speed", shooter_speed);
    SmartDashboard.putBoolean("Ball captured", ball_captured);
    SmartDashboard.putBoolean("Intake on", intake_is_on);
    SmartDashboard.putBoolean("Shooter on", shooter_is_on);
  }

  public void setShooterOn() {
    shoot.set(-shooter_speed); // rps
    shooter_is_on = true;
  }

  public void setShooterOff() {
    shoot.set(shooter_hold);
    shooter_is_on = false;
  }

  public void setIntakeOn() {
      intake.set(intake_speed);
      intake_is_on=true;
  }
  public void setIntakeOff() {
    intake.set(-shooter_hold);
    intake_is_on=false;
 } 
 public boolean isIntakeOn(){
   return intake_is_on;
 }
 public boolean isShooterOn() {
  return shooter_is_on;
 }
 public boolean isBallCaptured() {
  return limit_switch.inContact();
 }
 public void reset(){
  setIntakeOff();
  setIntakeOff();
 }
  @Override
  public void periodic() {
    //SmartDashboard.putNumber("Intake speed", intake.getRate());
		SmartDashboard.putNumber("Shooter speed", -shoot.getRate());
    shooter_speed=SmartDashboard.getNumber("Target speed", shooter_speed);
    SmartDashboard.putBoolean("Ball captured", isBallCaptured());
    SmartDashboard.putBoolean("Intake on", intake_is_on);
    SmartDashboard.putBoolean("Shooter on", shooter_is_on);
  }
}
