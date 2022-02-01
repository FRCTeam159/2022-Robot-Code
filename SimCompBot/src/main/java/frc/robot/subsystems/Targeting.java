// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.objects.AxonDetector;
import frc.robot.objects.DetectorInterface;
import frc.robot.objects.SimFrontCamera;
import frc.robot.objects.LimelightDetector;
import frc.robot.objects.SimBackCamera;
import frc.robot.objects.TargetData;
import frc.robot.objects.TargetDetector;

public class Targeting extends SubsystemBase {
  TargetDetector m_front_detector;
  TargetDetector m_back_detector;
  NetworkTable table;

  public Targeting() {
    System.out.println("new Targeting " + Robot.isReal());
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    table = inst.getTable("targetdata");
    if (Robot.isReal()) {
      m_front_detector = new LimelightDetector(DetectorInterface.FRONT);
      m_back_detector = new AxonDetector(DetectorInterface.BACK);
    } else {
      m_front_detector = new SimFrontCamera(DetectorInterface.FRONT);
      m_back_detector=new SimBackCamera(DetectorInterface.BACK);
    }
  }

  public void start() {
    if (m_front_detector != null)
      m_front_detector.start();
    if (m_back_detector != null)
      m_back_detector.start();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
