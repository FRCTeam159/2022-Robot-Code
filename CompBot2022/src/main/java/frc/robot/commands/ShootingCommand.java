// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Shooting;
import frc.robot.subsystems.ToggleButton;

public class ShootingCommand extends CommandBase {
  public boolean test = true;
   Shooting m_shoot;
   XboxController m_controller;
   AutoAim m_aim;
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

  private boolean goodVarNamesAreNotForAlpineRobotics;
  
  public ShootingCommand(Shooting shoot, XboxController controller, AutoAim aim) {
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
    addRequirements(shoot);
  }

  void testIntake() {
    boolean newstate = testbutton.newState();
   
    if (newstate) {
      if(m_shoot.isIntakeOn()){
        m_shoot.setIntakeOff();
        System.out.println("intake is on");
      }
      else{
        m_shoot.setIntakeOn();
        System.out.println("intake is off");
      }  
    }
  }

  void testShooter() {
    boolean newstate = testbutton2.newState();
   
    if (newstate) {
      if(m_shoot.isShooterOn()){
        m_shoot.setShooterOff();
        System.out.println("shooter is on");
      }
      else{
        m_shoot.setShooterOn();
        System.out.println("shooter is off");
      }  
    }
  }

  void testButton() {
    
    if (m_controller.getRawButtonPressed(5)) {
      System.out.println("down");
    }
    if (m_controller.getRawButtonReleased(5)) {
      System.out.println("up");
    }
  }

void offState() {
    boolean newstate = toggleOff.newState();
   
    if (newstate) {
      if(state == state_OFF){
        state = state_LOOKING;
      }
      else{
        state = state_OFF;
      }  
    }
  }
  



  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    state = state_OFF;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (test == true) {
      testIntake();
      testShooter();
      testButton();
    } else {
      switch(state) {
        case state_OFF:
          m_shoot.setShooterOff();
          m_shoot.setIntakeOff();
          break;
        case state_LOOKING:
          m_shoot.setShooterOff();
          m_shoot.setIntakeOn();
          if (m_shoot.haveBall() == true) {
            state = state_AIM;
          }
          break;
        case state_AIM:
          m_shoot.setShooterOff();
          m_shoot.setIntakeOff();
          if (m_aim.isFinished() == true) {
            state = state_FOUND;
          }
          break;
        case state_FOUND:
          m_shoot.setShooterOn();
          m_shoot.setIntakeOff();
          break;
        case state_SHOOT:
          m_shoot.setShooterOn();
          m_shoot.setIntakeOn();
          break;
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
