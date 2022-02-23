// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
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
  private boolean intake_is_on = false;
  private boolean intake_is_holding = false;
  private boolean shooter_is_on = false;
  private boolean ball_captured = false;
  private double shooter_err_tol = 0.1;
  private double shooter_err_vel_tol = 0.01;
  private double last_err;

  public static double kIntakeSpeed = 7;
  public static double kIntakeHold = 1;
  public static double kShootSpeed = 17;
  public static double kShootHold = 0;
  public static double kRunUpTime = 4;
  public static double kSpinbackTime = 2;

  public static double ramp_time = 0.25;
  private double max_shooter_speed = 1;
  private double max_intake_speed = 1;

  private boolean shooter_ramp = true;
  private boolean intake_ramp = true;
  private Timer timer = new Timer();


  public Shooting() {
    intake = new SparkMotor(INTAKE);
    shoot = new SparkMotor(SHOOTER);
    limit_switch = new SimContact(0);
    intake.enable();
    shoot.enable();
    limit_switch.enable();
    setIntakeHold();
    setShooterOff();
    intake.setScale(1);
    shoot.setScale(1);
    // SmartDashboard.putNumber("Intake speed", 0);
    SmartDashboard.putNumber("Shooter speed", 0);
    SmartDashboard.putNumber("Target speed", kShootSpeed);
    SmartDashboard.putBoolean("Ball captured", ball_captured);
    SmartDashboard.putBoolean("Intake on", intake_is_on);
    SmartDashboard.putBoolean("Shooter on", shooter_is_on);
    timer.start();
  }

  public void setShooterOn() {
    if (shooter_ramp) {
      if (!shooter_is_on)
        timer.reset();
    } else
      shoot.set(-kShootSpeed);
    shooter_is_on = true;
  }

  public void setShooterOff() {
    if (shooter_ramp) {
      if (shooter_is_on)
        timer.reset();
    } else
      shoot.set(kShootHold);
    shooter_is_on = false;
  }

  public void setIntakeOn() {
    if (intake_ramp) {
      if (!intake_is_on)
        timer.reset();
    } else
      intake.set(kIntakeSpeed);
    intake_is_on = true;
    intake_is_holding=false;
  }

  public void setIntakeOff() {
    intake.set(0);
    intake_is_on = false;
    intake_is_holding=false;
  }

  public void setIntakeHold() {
    intake.set(-kIntakeHold);
    intake_is_on = false;
    intake_is_holding=true;
  }

  public boolean isIntakeOn() {
    return intake_is_on;
  }
  public boolean isIntakeHolding() {
    return intake_is_holding;
  }

  public boolean isShooterOn() {
    return shooter_is_on;
  }

  public double aveShooterAcc() {
    return shoot.aveAcceleration();
  }

  public double aveShooterVel() {
    return shoot.aveVelocity();
  }

  public boolean shooterReady() {

    boolean ready = false;
    double r = -aveShooterVel();
    double err = Math.abs(r - kShootSpeed);
    double delta_err = Math.abs(err - last_err);
    if (shooter_is_on && err < shooter_err_tol && delta_err < shooter_err_vel_tol)
      ready = true;
    last_err = err;
    return ready;
  }

  public boolean isBallCaptured() {
    return limit_switch.inContact();
  }

  private void setShooterSpeed(double v) {
    if (shooter_is_on) {
      max_shooter_speed = v;
    }
    shoot.set(v);
  }
  private void setIntakeSpeed(double v) {
    if (shooter_is_on) {
      max_intake_speed = v;
    }
    intake.set(v);
  }
  public void reset() {
    setShooterOff();
    setIntakeOff();
  }

  @Override
  public void periodic() {
    if (shooter_ramp) {
      double f = timer.get() / kRunUpTime;
      f = f > 1 ? 1 : f;
      if (shooter_is_on)
        setShooterSpeed(-kShootSpeed * f);
      else
        setShooterSpeed(-kShootHold*f+max_shooter_speed * (1 - f));
    }
    if (intake_ramp) {
      double f = timer.get() / kSpinbackTime;
      f = f > 1 ? 1 : f;
      if (intake_is_on)
        setIntakeSpeed(kIntakeSpeed * f);
      else
      setIntakeSpeed(-kIntakeHold*f+max_intake_speed * (1 - f));
    }
    //ave_accel = acc_averager.getAve(shoot.getAcceleration());
    //ave_vel = vel_averager.getAve(shoot.getRate());

    // SmartDashboard.putNumber("Intake speed", intake.getRate());
    SmartDashboard.putNumber("Shooter speed", -shoot.getRate());
    kShootSpeed = SmartDashboard.getNumber("Target speed", kShootSpeed);
    SmartDashboard.putBoolean("Ball captured", isBallCaptured());
    SmartDashboard.putBoolean("Intake on", intake_is_on);
    SmartDashboard.putBoolean("Shooter on", shooter_is_on);
  }
}
