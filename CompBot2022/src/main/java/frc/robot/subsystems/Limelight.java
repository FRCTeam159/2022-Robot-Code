// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class Limelight extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  public NetworkTableEntry tx = table.getEntry("tx");
  public NetworkTableEntry ty = table.getEntry("ty");
  public NetworkTableEntry ta = table.getEntry("ta");
  public NetworkTableEntry tv = table.getEntry("tv");
  public boolean testLimelight;

  public double limeX;
  public double limeA;
  public double limeY;
  public double limeV;
  public boolean frontCamera = true;

  public Limelight() {
    SmartDashboard.putBoolean("test limelight", testLimelight);
  }

  // public void setCameraFront(){
  //   //lr stands for lower right
  //   table.getEntry("stream").setNumber(1);
  //   frontCamera = true;
  // }

  // public void setCameraBack(){
  //   table.getEntry("stream").setNumber(2);
  //   frontCamera = false;
  // }

  // public void switchView() {
  //   boolean newstate = switchCamera.newState();

  //   if (newstate) {
  //     if (frontCamera) {
  //       setCameraBack();
  //     } else {
  //       setCameraFront();
  //     }
  //   }
  // }
  
  public void limelightOff() {
    table.getEntry("ledMode").setNumber(1);
    table.getEntry("camMode").setNumber(1);
    System.out.println("limelight off");
  }
  public void limelightOn() {
    table.getEntry("ledMode").setNumber(3);
    table.getEntry("camMode").setNumber(0);
    System.out.println("limelight on");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    boolean test = SmartDashboard.getBoolean("test limelight", testLimelight);
    if (test != testLimelight) {
      testLimelight = test;
      if(testLimelight){
        limelightOn();
      } else {
        limelightOff();
      }
    }

    

    // read values periodically
    limeX = tx.getDouble(0.0);
    limeY = ty.getDouble(0.0);
    limeA = ta.getDouble(0.0);
    limeV = tv.getDouble(0.0);

    // post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", limeX);
    SmartDashboard.putNumber("LimelightY", limeY);
    SmartDashboard.putNumber("LimelightArea", limeA);
  
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
