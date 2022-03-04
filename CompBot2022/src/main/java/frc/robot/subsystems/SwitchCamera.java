// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import utils.MJpegClient;

public class SwitchCamera extends Thread {
  /** Creates a new SwitchCamera. */
  private UsbCamera IntakeCamera;
  protected static CvSource dualStream;
  protected ToggleButton switchCamera;
  private XboxController m_controller;
  private boolean frontCamera;
  private String limelight_url = "http://10.1.59.11:5800/";
  private MJpegClient limelight;
  private boolean limelight_connected;
  private CvSink UsbCameraSink;
  private static Mat m1, m2;

  public SwitchCamera(XboxController x) {
    m_controller = x;
    IntakeCamera = CameraServer.startAutomaticCapture(0);
    IntakeCamera.setResolution(640, 480);
    IntakeCamera.setFPS(25);
    dualStream = CameraServer.putVideo("SwitchCamera", 640, 480);
    switchCamera = new ToggleButton(new JoystickButton(m_controller, 4));
    limelight = new MJpegClient(limelight_url);
    limelight_connected = limelight.isConnected();
    UsbCameraSink = CameraServer.getVideo(IntakeCamera);
    setCameraFront();
  }

  public void switchView() {
    boolean newstate = switchCamera.newState();

    if (newstate) {
      if (frontCamera) {
        setCameraBack();
        System.out.println("back cam");
      } else {
        setCameraFront();
        System.out.println("front cam");
      }
    }
  }

  public void putFrame(CvSource source, Mat m) {
    if (m != null) {
      source.putFrame(m);
    }
  }

  public Mat getLimelightFrame() {
    Mat mat = new Mat();
    mat = limelight.read();
    return mat;
  }

  private Mat getUsbCameraFrame() {
    Mat mat = new Mat();
    UsbCameraSink.grabFrame(mat);
    return mat;
  }

  private void setCameraFront() {
    frontCamera = true;
  }

  private void setCameraBack() {
    frontCamera = false;
  }

  @Override
  public void run() {
    // This method will be called once per scheduler run
    while (true) {
      try {
        Thread.sleep(20);
        switchView();
        m1 = getLimelightFrame();
        m2 = getUsbCameraFrame();
        if (frontCamera) {
          putFrame(dualStream, m1);
        } else {
          putFrame(dualStream, m2);
        }
      } catch (Exception exception) {
        System.out.println("error " + exception);
      }

    }
  }
}