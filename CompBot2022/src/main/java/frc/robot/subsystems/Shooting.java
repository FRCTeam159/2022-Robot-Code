// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
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
  public static double kIntakeBackward = 0.9; //change this in the smartdashboard it is called "spinback speed"
  public static double kShootSpeed = -0.90; //decrease (to like -0.9) this if you want it to shoot higher, was -0.6 at comp
  public static double kIdealShoot = -30; // was -45
  public DoubleSolenoid intakePiston;
  public double kRunUpTime = 5;
  public double kInputHoldTime = 1;

  public Shooting() {
    //PIDController shootPID = new PIDController(0.1, 0, 0);
    if (!flagPancake){
    intake = new SparkMotor(INTAKE);
    shoot = new SparkMotor(SHOOTER);
    intakePiston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 5, 4);
    }
    SmartDashboard.putNumber("shooter speed", 0);
    SmartDashboard.putNumber("intake speed", 0);
    SmartDashboard.putNumber("spinback Speed", 0.19);
    SmartDashboard.putNumber("shoot speed", -0.60);
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

  public boolean shootOnTarget(){
    return Math.abs(getShoot()-kIdealShoot) < 2;
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

  public void purgeIntake(){
    intake.set(0.5);
  }

  public void setIntakeArmsOut() {
    intakePiston.set(Value.kForward);
  }

  public void setIntakeArmsIn() {
    intakePiston.set(Value.kReverse);
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
    SmartDashboard.putNumber("intake temp", intake.getMotorTemperature());
    SmartDashboard.putNumber("intake cURRENCT", intake.getOutputCurrent());
    kIntakeBackward = SmartDashboard.getNumber("spinback Speed", 0.10);
    kShootSpeed = SmartDashboard.getNumber("shoot Speed", -0.60);
  }
}
