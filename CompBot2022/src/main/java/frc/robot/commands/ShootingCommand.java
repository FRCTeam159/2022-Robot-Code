// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Shooting;
import frc.robot.subsystems.ToggleButton;

public class ShootingCommand extends CommandBase {
  public boolean test = false;
   Shooting m_shoot;
   XboxController m_controller;
   public JoystickButton testjoybutton;
   public ToggleButton testbutton;
   public boolean lastState;
  
  public ShootingCommand(Shooting shoot, XboxController controller) {
    m_shoot = shoot;
    m_controller = controller;
    testjoybutton = new JoystickButton(controller, 2);
    testbutton = new ToggleButton(testjoybutton);
    addRequirements(shoot);
  }

  void testIntake() {
    boolean test = testbutton.newState();
    m_shoot.setIntake(test);
    if (lastState != test) {
      System.out.println("intake state = " + test);
    }
    lastState = test;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (test == true) {
      //testIntake();
    } else {

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
