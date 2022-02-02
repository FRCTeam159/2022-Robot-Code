// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

import org.opencv.core.Mat;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class CameraStreams extends Thread {
  TargetDetector m_front_detector;
  TargetDetector m_back_detector;
  boolean front_camera = true;
  NetworkTable table;

  protected static CvSource dualStream;
  public int image_width = 640;
  public int image_height = 480;

  protected final Timer m_timer = new Timer();

  public CameraStreams() {
    System.out.println("new Targeting " + Robot.isReal());
    SmartDashboard.putBoolean("Front Camera", true);
    SmartDashboard.putBoolean("Show HVS threshold", false);

    dualStream = CameraServer.putVideo("SwitchedCamera", image_width, image_height);

    if (Robot.isReal()) {
      m_front_detector = new LimelightDetector();
      m_back_detector = new AxonDetector();
    } else {
      m_back_detector = new GripBackCamera();
      m_front_detector = new GripFrontCamera();
    }
  }

  void putFrame(CvSource source, Mat m) {
    if (m != null)
      source.putFrame(m);
  }

  @Override
  public void run() {
    m_timer.start();
    while (true) {
      try {
        Thread.sleep(20);
      } catch (InterruptedException ex) {
        System.out.println("exception)");
      }
      TargetDetector.show_hsv_threshold = SmartDashboard.getBoolean("Show HVS threshold", true);
      boolean is_front = SmartDashboard.getBoolean("Front Camera", true);
      if (is_front != front_camera) {
        if (is_front)
          System.out.println("setting front camera");
        else
          System.out.println("setting back camera");
      }
      if (is_front) {
        putFrame(dualStream, m_front_detector.getFrame());
        m_front_detector.publish();
      } else {
        putFrame(dualStream, m_back_detector.getFrame());
        m_back_detector.publish();
      }
      front_camera = is_front;
    }
  }
}
