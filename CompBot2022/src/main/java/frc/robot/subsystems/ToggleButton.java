// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.


package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class ToggleButton extends SubsystemBase {
  private JoystickButton button;
private boolean resetReady = true;
  /** Creates a new ToggleButton. */
  public ToggleButton(JoystickButton givenbutton){
      button = givenbutton; 
  }
  public boolean newState(){
      boolean state = button.get();
      if(state && resetReady){
          resetReady = false;
          return true;
      } else if(!state && !resetReady){
          resetReady = true;
          return false;
      } else{
          return false;
      }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
