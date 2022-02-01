// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.Calibrate;
import frc.robot.commands.DrivePath;
import frc.robot.commands.DriveToCargo;
import frc.robot.commands.DriveToTarget;
import frc.robot.commands.GrabCargo;
import frc.robot.commands.Shoot;
import frc.robot.commands.TurnToAngle;
import utils.PlotUtils;

public class Autonomous extends SubsystemBase {
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  SendableChooser<Integer> m_auto_plot_option = new SendableChooser<>();

  Drivetrain m_drive;
  Targeting m_targeting;

  public static final int CALIBRATE = 0;
  public static final int PROGRAM = 1;
  public static final int AUTOTEST1 = 2;
  public static final int AUTOTEST2 = 3;
 
 
  public int selected_path=PROGRAM;

  SendableChooser<Integer> m_path_chooser = new SendableChooser<Integer>();
  /** Creates a new AutoCommands. */
  public Autonomous(Drivetrain drive,Targeting targeting) {
    m_drive=drive;
    m_targeting=targeting;
   
    m_auto_plot_option.setDefaultOption("No Plot", PlotUtils.PLOT_NONE);
    m_auto_plot_option.addOption("Plot Distance", PlotUtils.PLOT_DISTANCE);
    m_auto_plot_option.addOption("Plot Dynamics", PlotUtils.PLOT_DYNAMICS);
    m_auto_plot_option.addOption("Plot Position", PlotUtils.PLOT_POSITION);

    m_path_chooser.setDefaultOption("Program", PROGRAM);
    m_path_chooser.addOption("Calibrate", CALIBRATE);

	  m_path_chooser.addOption("AutoTest 1", AUTOTEST1);
    m_path_chooser.addOption("AutoTest 2", AUTOTEST2);

    SmartDashboard.putBoolean("reversed", false);
    SmartDashboard.putNumber("xPath", 4);
    SmartDashboard.putNumber("yPath", 0);
    SmartDashboard.putNumber("rPath", 0);
   
		SmartDashboard.putData(m_path_chooser);
    SmartDashboard.putData(m_auto_plot_option);
  }

  public CommandGroupBase getCommand(){
    //PlotUtils.auto_plot_option=m_auto_plot_option.getSelected();
    selected_path=m_path_chooser.getSelected();
    
    CommandGroupBase.clearGroupedCommands();
      
    switch (selected_path){
    case CALIBRATE:
      return new SequentialCommandGroup(new Calibrate(m_drive));
    case PROGRAM:
      return programPath();
    case AUTOTEST1:
      return autoTest1();
    case AUTOTEST2:
      return autoTest2();
    }
    return null;
  }
  CommandGroupBase programPath(){
    double x = SmartDashboard.getNumber("xPath", 4);
    double y = SmartDashboard.getNumber("yPath", 0);
    double r = SmartDashboard.getNumber("rPath", 0);
    boolean rev=SmartDashboard.getBoolean("reversed", false);
    SequentialCommandGroup commands = new SequentialCommandGroup();
    commands.addCommands(new DrivePath(m_drive,x,y,r,rev));
    return commands;
  }
  CommandGroupBase autoTest1(){
    SequentialCommandGroup commands = new SequentialCommandGroup();
    commands.addCommands(new TurnToAngle(m_drive,90.0));
    commands.addCommands(new DrivePath(m_drive,4.0,0.0,0.0,false));
    return commands;
  }
  CommandGroupBase autoTest2(){
    SequentialCommandGroup commands = new SequentialCommandGroup();
    commands.addCommands(new DrivePath(m_drive,0.75,0.0,0,true)); // backup 1 meter
    commands.addCommands(new DriveToTarget(m_drive,m_targeting)); // acquire target using limelight
    commands.addCommands(new Shoot());    // shoot
    commands.addCommands(new DriveToCargo(m_drive,m_targeting));  // drive to ball using Axon
    commands.addCommands(new DriveToTarget(m_drive,m_targeting)); // acquire target using limelight
    commands.addCommands(new Shoot());    // shoot
    return commands;
  }
}
