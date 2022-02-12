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
  public JoystickButton testjoybutton;
  public ToggleButton testbutton;
  public JoystickButton testjoybutton2;
  public ToggleButton testbutton2;
  public boolean lastState;
  public ToggleButton toggleOff;
  public JoystickButton joyToggleOff;
  public JoystickButton aimGo;
  public int state;
  private final int state_OFF = 0;
  private final int state_LOOKING = 1;
  private final int state_AIM = 2;
  private final int state_FOUND = 3;
  private final int state_SHOOT = 4;
  private final Timer m_timer = new Timer();
  private final Timer m_timeOut = new Timer();
  final double runUpTime = 3.0;
  private boolean autoAim;
  private boolean spinBack;

  public ShootingCommand(Shooting shoot, XboxController controller, Targeting aim, DriveTrain drive) {
    m_drive = drive;
    m_shoot = shoot;
    m_controller = controller;
    m_aim = aim;
    testjoybutton = new JoystickButton(controller, 2);
    testbutton = new ToggleButton(testjoybutton);
    testjoybutton2 = new JoystickButton(controller, 3);
    testbutton2 = new ToggleButton(testjoybutton2);
    joyToggleOff = new JoystickButton(controller, 4);
    toggleOff = new ToggleButton(joyToggleOff);
    aimGo = new JoystickButton(controller, 5);
    addRequirements(shoot, aim);
  }

  void testIntake() {
    boolean newstate = testbutton.newState();

    if (newstate) {
      if (m_shoot.isIntakeOn()) {
        m_shoot.setIntakeOff();
        System.out.println("intake is off");
      } else {
        m_shoot.setIntakeOn();
        System.out.println("intake is on");
      }
    }
  }

  void testShooter() {
    boolean newstate = testbutton2.newState();

    if (newstate) {
      if (m_shoot.isShooterOn()) {
        m_shoot.setShooterOff();
        System.out.println("shooter is on");
      } else {
        m_shoot.setShooterOn();
        System.out.println("shooter is off");
      }
    }
  }

  void offState() {
    boolean newstate = toggleOff.newState();

    if (newstate) {
      if (state == state_OFF) {
        state = state_LOOKING;
      } else {
        state = state_OFF;
      }
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    state = state_OFF;
    m_timeOut.start();
    m_timer.start();
    m_timer.reset();
    m_timeOut.reset();
    autoAim = false;
    spinBack = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (test == true) {
      testIntake();
      testShooter();
    } else {
      switch (state) {
        case state_OFF:
          // System.out.println("off");
          m_shoot.setShooterOff();
          m_shoot.setIntakeOff();
          m_timer.reset();
          if (m_controller.getRawButtonPressed(2)) {
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
              System.out.println("HAVE BALL");
              m_shoot.setIntakeOff();
              m_timer.reset();
              spinBack = true;
            } else if (m_timer.get() > 0.2) {
              state = state_AIM;
              System.out.println("ready to aim");
            }
          }
          break;
        case state_AIM:
          m_shoot.setShooterOff();
          m_shoot.setIntakeOff();

          if (m_controller.getRawButtonPressed(5)) {
            autoAim = true;
            m_timeOut.reset();
            m_aim.isTurn = true;
          }
          if (m_controller.getRawButtonPressed(2)) {
            state = state_OFF;
            m_timeOut.reset();
            autoAim = false;
          }
          if (autoAim) {
            // do aiming stuff here
            m_aim.aimOn();
            m_aim.adjust();
            if (m_aim.onTarget()) {
              state = state_FOUND;
              m_timer.reset();
              System.out.println("on target");
              // aimOff();
             // if (m_timeOut.get() > 10) 
              //  state = state_OFF;
             // else{
                System.out.println("running up");
                m_shoot.setIntakeOff();
                m_shoot.setShooterOn();
              //}
            }
          }
          /*
          if (m_controller.getRawButtonPressed(6)) {
            System.out.println("Finding Call");
            state = state_FOUND;
            // isaac thing here
            m_timer.reset();
            m_timer.start();
          }
          */
          break;
        case state_FOUND:
           // System.out.println("Finding Case");
          // odometryDrive();
          if (m_timer.get() > runUpTime) {
            state = state_SHOOT;
            m_timer.reset();
            System.out.println("Shooting");
          }
          break;
        case state_SHOOT:
          // arcadeDrive();
          //m_shoot.setShooterOn();
          m_shoot.setIntakeOn();
          autoAim = false;
          if (m_timer.get() > 1) {
            System.out.println("Test end");
            state = state_OFF;
            m_timer.reset();
          }
          break;
      }
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
