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

public class SwitchCamera extends SubsystemBase {
  /** Creates a new SwitchCamera. */
  private UsbCamera IntakeCamera;
  protected static CvSource dualStream;
  protected ToggleButton switchCamera;
  private XboxController m_controller;
  private boolean frontCamera;
  private String limelight_url = "http://10.1.59.11:5800/";
  private VideoCapture limelight;
  private boolean limelight_connected;
  private CvSink UsbCameraSink;

  public SwitchCamera(XboxController x) {
  m_controller = x;
  IntakeCamera = CameraServer.startAutomaticCapture(0);
  IntakeCamera.setResolution(320, 240);
  IntakeCamera.setFPS(25);
  dualStream =  CameraServer.putVideo("SwitchCamera", 320, 240);
  switchCamera = new ToggleButton(new JoystickButton(m_controller, 4));
  limelight = new VideoCapture();
  limelight_connected = limelight.open(limelight_url);
  UsbCameraSink = CameraServer.getVideo(IntakeCamera);
  }

  public void switchView() {
    boolean newstate = switchCamera.newState();

    if (newstate) {
      if (frontCamera) {
        setCameraBack();
      } else {
        setCameraFront();
      }
    }
  }
  
  public void putFrame(CvSource source, Mat m) {
    source.putFrame(m);
  }

  public Mat getLimelightFrame() {
    Mat mat =  new Mat();
    limelight.read(mat);
    return mat;
  }

  private Mat getUsbCameraFrame() {
    Mat mat =  new Mat();
    UsbCameraSink.grabFrame(mat);
    return mat;
  }

  private void setCameraFront() {;
    frontCamera = true;
  }

  private void setCameraBack() {
    frontCamera = false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    switchView();
    
  }
}
