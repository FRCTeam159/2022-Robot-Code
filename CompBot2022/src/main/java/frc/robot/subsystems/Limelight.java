// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  public NetworkTableEntry tx = table.getEntry("tx");
  public NetworkTableEntry ty = table.getEntry("ty");
  public NetworkTableEntry ta = table.getEntry("ta");
  public NetworkTableEntry tv = table.getEntry("tv");

  public double limeX;
  public double limeA;
  public double limeY;
  public double limeV;

  public Limelight() {

  }
  
  public void limelightOff() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").getDouble(1);
  }
  public void limelightOn() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").getDouble(3);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

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
