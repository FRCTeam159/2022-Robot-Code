// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Shooting;
import frc.robot.objects.ToggleButton;

public class ShootingCommand extends CommandBase {
  public boolean test = true;
   Shooting m_shoot;
   XboxController m_controller;
   public JoystickButton testjoybutton;
   public ToggleButton testbutton;
   public ToggleButton testbutton2;
   public JoystickButton testjoybutton2;
   public boolean lastState;
  
  public ShootingCommand(Shooting shoot, XboxController controller) {
    m_shoot = shoot;
    m_controller = controller;
    testjoybutton = new JoystickButton(controller, 2);
    testbutton = new ToggleButton(testjoybutton);
    testjoybutton2 = new JoystickButton(controller, 3);
    testbutton2 = new ToggleButton(testjoybutton2);
    addRequirements(shoot);
  }

  void testIntake() {
    boolean newstate = testbutton.newState();
    if (newstate) {
      if(m_shoot.isIntakeOn()){
        m_shoot.setIntakeOff();
        System.out.println("intake is off");
      }
      else{
        m_shoot.setIntakeOn();
        System.out.println("intake is on");
      }  
    }
  }

  void testShooter() {
    boolean newstate = testbutton2.newState(); 
    if (newstate) {
      if(m_shoot.isShooterOn()){
        m_shoot.setShooterOff();
        System.out.println("shooter is off");
      }
      else{
        m_shoot.setShooterOn();
        System.out.println("shooter is on");
      }  
    }
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (test == true) {
      testIntake();
      testShooter();
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
