// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Targeting;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooting;
import frc.robot.objects.*;

public class ShootingCommand extends CommandBase {
  public boolean test = false;
  Shooting m_shoot;
  XboxController m_controller;
  Targeting m_aim;
  DriveTrain m_drive;

  public ToggleButton m_toggleShooter;
  public ToggleButton m_toggleIntake;
  public ToggleButton m_switchCamera;

  public JoystickButton aimGo;
  public boolean lastState;
  public int state;
  private final int state_OFF = 0;
  private final int state_LOOKING = 1;
  private final int state_AIM = 2;
  private final int state_FOUND = 3;
  private final int state_SHOOT = 4;
  private final Timer m_timer = new Timer();

  private boolean autoAim;
  private boolean spinBack;
  private boolean manualAim;

  public ShootingCommand(Shooting shoot, XboxController controller, Targeting aim, DriveTrain drive) {
    m_drive = drive;
    m_shoot = shoot;
    m_controller = controller;
    m_aim = aim;
    m_toggleShooter = new ToggleButton(new JoystickButton(controller, 3));
    m_toggleIntake = new ToggleButton(new JoystickButton(controller, 2));
    m_switchCamera = new ToggleButton(new JoystickButton(controller, 4));

    aimGo = new JoystickButton(controller, 5);
    addRequirements(shoot, aim);
  }

  void whichCamera() {
    boolean newstate = m_switchCamera.newState();
    if (newstate) {
      boolean is_front = SmartDashboard.getBoolean("Front Camera", true);
      SmartDashboard.putBoolean("Front Camera", !is_front);
    }
  }
  void testIntake() {
    boolean newstate = m_toggleIntake.newState();

    if (newstate) {
      m_timer.reset();
      if (m_shoot.isIntakeOn()) {
        m_shoot.setIntakeHold();
        SmartDashboard.putString("Status", "Intake Holding");
        System.out.println("intake is holding");
      } else {
        m_shoot.setIntakeOn();
        SmartDashboard.putString("Status", "Intake Pushing");
        System.out.println("intake is on");
      }
    }
  }

  void testShooter() {
    boolean newstate = m_toggleShooter.newState();

    if (newstate) {
      m_timer.reset();
      if (m_shoot.isShooterOn()) {
        m_shoot.setShooterOff();
        SmartDashboard.putString("Status", "Shooter is On");
        System.out.println("shooter is on");
      } else {
        m_shoot.setShooterOn();       
        SmartDashboard.putString("Status", "Shooter is Off");
        System.out.println("shooter is off");
      }
    }
    //double a=m_shoot.shooterAcceleration();
    //if(Math.abs(a)>0.5)
    //System.out.println(m_shoot.shooterVelocity()+" "+m_shoot.shooterAcceleration());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    state = state_OFF;
    m_timer.start();
    m_timer.reset();
    autoAim = false;
    spinBack = false;
    manualAim = false;
    SmartDashboard.putString("Status", "Manual Driving");
    m_aim.disable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    if (test == true) {
      testIntake();
      testShooter();
      return;
    }
    whichCamera();
    if(m_aim.frontTarget())
      front_target_program();
    else
      back_target_program(); 
  }
  // tape target state machine
  private void back_target_program() {
    switch (state) {
      case state_OFF:
        m_shoot.setShooterOff();
        m_shoot.setIntakeHold();
        if (m_shoot.isBallCaptured()){
          state = state_FOUND;
        }
        if (m_controller.getRawButtonPressed(2)) {
          SmartDashboard.putString("Status", "Intake starting");
          m_shoot.setIntakeOn();
          state = state_LOOKING;
        }
        break;
      case state_LOOKING:
        if (m_controller.getRawButtonPressed(5)) {
          SmartDashboard.putString("Status", "Starting Auto intake");
          autoAim = true;
          m_aim.enable();
          state=state_AIM;
        }
        if (m_shoot.isBallCaptured()){
          m_shoot.setIntakeHold();
          state = state_FOUND;
        }
        break;
      case state_AIM:
        if(autoAim){
          m_aim.adjust();
        }
        if (m_shoot.isBallCaptured()) {
          state = state_FOUND;
          m_shoot.setIntakeHold();
          SmartDashboard.putString("Status", "Ball captured");
        }
        if (m_controller.getRawButtonPressed(2)) {
          SmartDashboard.putString("Status", "Intake cancelled");
          state = state_OFF;
          autoAim = false;
        }
        if (m_controller.getRawButtonPressed(6)) {
          SmartDashboard.putString("Status", "Auto Intake cancelled");
          autoAim = false;
        }
        break;
      case state_FOUND:
        if (!m_shoot.isBallCaptured()){
          SmartDashboard.putString("Status", "Ball Lost");
          state = state_OFF;
        }
        if (m_controller.getRawButtonPressed(2)){
          SmartDashboard.putString("Status", "Intake cancelled");
          state = state_OFF;
        }
      break;
    }
  }
  // tape target state machine
  private void front_target_program() {
    switch (state) {
      case state_OFF:
        manualAim = false;
        autoAim = false;

        if(m_shoot.isShooterOn())
          m_shoot.setShooterOff();
        if(m_shoot.isBallCaptured())
          m_shoot.setIntakeHold();
        SmartDashboard.putString("Status", "Manual Driving");
        if (m_controller.getRawButtonPressed(2)) {
            m_timer.reset();
            SmartDashboard.putString("Status", "Waiting for ball");
            spinBack = false;
            state = state_LOOKING;
        }
      
        break;
      case state_LOOKING:
        m_shoot.setShooterOff();
        if (!spinBack)
          m_shoot.setIntakeOn();
        if (m_shoot.isBallCaptured()) {
          if (!spinBack) {
            SmartDashboard.putString("Status", "Have Ball");
            m_shoot.setIntakeHold();
            m_timer.reset();
            spinBack = true;
          } else if (m_timer.get() > Shooting.kSpinbackTime) {
            state = state_AIM;
            SmartDashboard.putString("Status", "Ready to aim");
            System.out.println("ready to aim");
          }
        }
        break;
      case state_AIM:
        if(!manualAim && m_shoot.isShooterOn())
          m_shoot.setShooterOff();
        if(m_shoot.isIntakeOn())
          m_shoot.setIntakeHold();

        if (m_controller.getRawButtonPressed(5)) {
          SmartDashboard.putString("Status", "Aiming ...");
          autoAim = true;
        }
        if (!manualAim && m_controller.getRawButtonPressed(6)) {
          SmartDashboard.putString("Status", "Starting Manual-aiming");
          System.out.println("Starting Manual-aiming");
          autoAim = false;
          manualAim = true;
          m_shoot.setIntakeHold();
          m_shoot.setShooterOn();
        }
        if (m_controller.getRawButtonPressed(2)) {
          SmartDashboard.putString("Status", "Targeting cancelled");
          state = state_OFF;
          autoAim = false;
        }
        if (autoAim) {
          // do aiming stuff here
          m_aim.enable();
          m_aim.adjust();
          m_shoot.setIntakeHold();
          m_shoot.setShooterOff();
          if (m_aim.onTarget()) {
            SmartDashboard.putString("Status", "On Target");
            state = state_FOUND;
            m_timer.reset();
            System.out.println("on target - running up");
            m_aim.disable();
            m_shoot.setShooterOn();
          }
        }
        
        if (manualAim && m_controller.getRawButtonPressed(1)) {
          m_timer.reset();
          state = state_SHOOT;
          System.out.println("Shooting");
        }
        break;
      case state_FOUND:
      if (m_shoot.shooterReady() || m_timer.get() > Shooting.kRunUpTime) {
          state = state_SHOOT;
          SmartDashboard.putString("Status", "Shooting");
          System.out.println(m_timer.get()+" Shooting "+m_shoot.aveShooterVel());
          m_timer.reset();
        }
        break;
      case state_SHOOT:
        // m_shoot.setShooterOn();
        m_shoot.setIntakeOn();
        autoAim = false;
        if (m_timer.get() > 4) {
          if (m_shoot.isBallCaptured())
            SmartDashboard.putString("Status", "Shot Failed");
          else
            SmartDashboard.putString("Status", "Shot Complete");
          System.out.println("Test end");
          state = state_OFF;
          m_timer.reset();
        }
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
