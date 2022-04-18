// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.AutoTarget;
import frc.robot.commands.DriveBack;

public class Autonomous extends SubsystemBase {
  public int mode;
  public final int mode_BACK = 0;
  public final int mode_ONEBALL = 1;
  public final int mode_TWOBALL = 2;
  private final Timer m_timer = new Timer();
  public static double totalRuntime;
  SendableChooser<Integer> m_chooser = new SendableChooser<Integer>();
  DriveTrain m_drive;
  Aiming m_aim;
  Shooting m_shoot;

  /** Creates a new Automonous. */
  public Autonomous(DriveTrain drive, Aiming aim, Shooting shoot) {
    m_drive = drive;
    m_aim = aim;
    m_shoot = shoot;

    m_chooser.addOption("drive back", mode_BACK);
    m_chooser.addOption("one ball", mode_ONEBALL);
    m_chooser.setDefaultOption("two ball", mode_TWOBALL);
    SmartDashboard.putData(m_chooser);

  }

  private CommandGroupBase twoBall() {
    System.out.println("2 ball selected");
    return new SequentialCommandGroup(
        new AutoTarget(m_drive, m_aim, m_shoot),
        new DriveBack(m_drive, m_shoot, -1),
        new AutoTarget(m_drive, m_aim, m_shoot));
  }

  private CommandGroupBase oneBall() {
    System.out.println("1 ball selected");
    return new SequentialCommandGroup(
        new AutoTarget(m_drive, m_aim, m_shoot));
  }

  private CommandGroupBase back() {
    System.out.println("drive back selected");
    return new SequentialCommandGroup(
        new DriveBack(m_drive, m_shoot, -1));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public CommandGroupBase getCommand() {
    mode = m_chooser.getSelected();
    m_timer.start();
    m_timer.reset();
    totalRuntime = 0;
    System.out.println("mode = " + mode);
    switch (mode) {
      case mode_BACK:
        return back();
      default:
      case mode_ONEBALL:
        return oneBall();
      case mode_TWOBALL:
        return twoBall();
    }
  }
}
