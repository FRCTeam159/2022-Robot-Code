// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Controller;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Aiming;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooting;
import frc.robot.subsystems.ToggleButton
;

public class ShootingCommand extends CommandBase {
  public boolean test = false;
  Shooting m_shoot;
  XboxController m_controller;
  Aiming m_aim;
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
  private final int state_RUNUP = 4;
  private final int state_SHOOT = 5;
  private final Timer m_timer = new Timer();
  private final Timer m_timeOut = new Timer();
  private boolean autoAim;

  private boolean goodVarNamesAreNotForAlpineRobotics;

  public ShootingCommand(Shooting shoot, XboxController controller, Aiming aim, DriveTrain drive) {
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
        m_shoot.setIntakeHold();
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
        System.out.println("shooter is off");
      } else {
        m_shoot.setShooterOn();
        System.out.println("shooter is on");
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
    m_timer.reset();
    m_timeOut.reset();
    m_timer.start();
    m_timeOut.start();
    autoAim = false;
    m_aim.aimOff();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (test == true) {
      testIntake();
      testShooter();
      m_aim.aimOn();
      m_shoot.ballCapture();
    } else {
      switch (state) { //dont make any changes here unless nothing else works, and text me if you do
        case state_OFF:
          //System.out.print(".");
          m_shoot.setIntakeArmsIn();
          m_shoot.setShooterOff();
          m_shoot.setIntakeOff();
          //press b to go into looking
          if (m_controller.getRawButtonPressed(2)) {
            autoAim = false;
            m_timer.reset();
            state = state_LOOKING;
          }
          break;
        case state_LOOKING:
          m_shoot.setShooterOff();
          m_shoot.setIntakeOn();
          m_shoot.setIntakeArmsOut();
          System.out.println("lookingn for ball");
          if (m_shoot.ballCapture()) {
            System.out.println("going to aim");
            state = state_AIM;
          }
          if (m_controller.getRawButtonPressed(3)) {
            m_timeOut.reset();
            state = state_OFF;
          }
          break;
        case state_AIM:
          m_shoot.setIntakeArmsIn();
          m_shoot.setShooterOff();
          m_shoot.setIntakeOff();
          System.out.println("ready to aim");
          if (m_controller.getRawButtonPressed(5)) {
            System.out.println("ready for auto aim");
            autoAim = true;
            m_aim.aimOn();
            m_timeOut.reset();
          }
          if (m_controller.getRawButtonPressed(3)) {
            m_timeOut.reset();
            state = state_OFF;
          }
          if (!m_aim.seeTarget()) {
            System.out.println("no target");
          }
          if (autoAim && m_aim.seeTarget()) {
            // do aiming stuff her
            System.out.println("aiming");
            m_aim.adjust();
            if (m_aim.onTarget()) {
              state = state_FOUND;
              System.out.println("on target");
            }
            if (m_timeOut.get() > 10) {
              state = state_OFF;
            }
          }
          if (m_controller.getRawButtonPressed(6)) {
            System.out.println("isacc shooty shoot");
            m_aim.aimOn();
            state = state_FOUND;
            // isaac thing here
          }
          break;
        case state_FOUND:
          m_shoot.setIntakeHold();
          if (m_shoot.isIntakeOn()) {
            System.out.println(m_shoot.getIntake());
          }
          if (!m_shoot.ballCapture() || m_timer.get() > m_shoot.kInputHoldTime) {
            System.out.println("goin to run up");
            m_timer.reset();
            state = state_RUNUP;
          }
          break;
        case state_RUNUP:
        //System.out.println("time: " + m_timer.get());
        System.out.println("speed: " + m_shoot.getShoot());
        System.out.println("running up");
          m_shoot.setShooterOn();
        if (m_timer.get() > m_shoot.kRunUpTime) {
          state = state_SHOOT;
        }
        if (m_controller.getRawButtonPressed(3)) {
          m_timeOut.reset();
          state = state_OFF;
        }
        break;
        case state_SHOOT:
          // arcadeDrive();
          m_shoot.setShooterOn();
          m_shoot.setIntakeOn();
          autoAim = false;
          if (m_timer.get() > 1) {
            System.out.println("Test end");
            m_timer.reset();
            state = state_OFF;
          }
          m_aim.aimOff();
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
