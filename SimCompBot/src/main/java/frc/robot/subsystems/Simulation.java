// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import gazebo.SimClock;
import gazebo.SimControl;

public class Simulation extends SubsystemBase {
  static int cnt=0;
  /** Creates a new SimulationControl. */
  private SimControl m_simcontrol = new SimControl();
  private Drivetrain m_drive;
  private boolean resetting = false;
  private final Field2d m_fieldSim = new Field2d();
  Pose2d field_offset= new Pose2d(7,2,Rotation2d.fromDegrees(90));

  private final Timer m_timer = new Timer();

  double simtime = 0;
  private boolean running = false;
  private boolean disabling = false;

  private SimClock m_simclock = new SimClock();

  public Simulation(Drivetrain drivetrain) {
    m_drive = drivetrain;
    SmartDashboard.putBoolean("Reset", false);
    SmartDashboard.putBoolean("Gazebo", false);
    SmartDashboard.putNumber("SimTime", 0);
    SmartDashboard.putNumber("SimClock", 0);
    SmartDashboard.putData("Field", m_fieldSim);
    m_timer.start();
  }

  public double getSimTime() {
    return m_simclock.getTime();
  }

  public double getClockTime() {
    return m_timer.get();
  }

  public void reset() {
    System.out.println("Simulation.reset");
    m_simclock.reset();
    m_timer.reset();
    SmartDashboard.putNumber("SimTime", 0);
   // running = false;
  }
  public void clear() {
    System.out.println("Simulation.clear");
    m_simclock.clear();
    m_timer.reset();
    SmartDashboard.putNumber("SimTime", 0);
    running = false;
  }

  public void start() {
    System.out.println("Simulation.start");
    m_simclock.reset();
    m_simclock.enable();
    running = true;
  }

  public void end() {
    System.out.println("Simulation.end");
    SmartDashboard.putNumber("SimTime", m_simclock.getTime());
    m_simclock.disable();
    running = false;
  }

  @Override
  public void periodic() {
    // SmartDashboard.putNumber("SimTime", m_simclock.getTime());
    // This method will be called once per scheduler run
  }

  public void simulationPeriodic() {
    
    Pose2d robot_pose=m_drive.getAbsPose();
    Transform2d transform=new Transform2d(robot_pose.getTranslation(), robot_pose.getRotation());
    Pose2d field_pose=field_offset.plus(transform);
    if((cnt%20)==0)
      System.out.println(field_pose);
    cnt++;
    m_fieldSim.setRobotPose(field_pose);
    simtime = getClockTime();
    if (running)
      SmartDashboard.putNumber("SimClock", getClockTime());
    else
      SmartDashboard.putNumber("SimClock", 0);
    boolean m = SmartDashboard.getBoolean("Gazebo", false);
    boolean b = SmartDashboard.getBoolean("Reset", false);
    if (b) {
      if (!resetting) {
        resetting = true;
        m_drive.reset();
        if (m){
          clear();
          m_drive.resetOdometry(new Pose2d(0,0,new Rotation2d(0)));
        }
       
        m_timer.reset();
      } else if (m_timer.get() > 0.1) {
        if (!disabling) {
         // m_drive.reset();
          m_drive.disable();
          disabling = true;
        } else if (m_timer.get() > 0.5) {
          SmartDashboard.putBoolean("Reset", false);
          resetting = false;
          disabling = false;
          m_drive.enable();
          run();
          running = true;
        }
      }
    }
  }

  public void init() {
    m_simcontrol.init();
    m_simclock.disable();
  }

  public void run() {
    running = true;
    m_simcontrol.run();
  }
}
