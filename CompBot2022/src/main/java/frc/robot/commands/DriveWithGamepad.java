// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DriveWithGamepad extends CommandBase implements Constants {

  private final DriveTrain m_Drive;
  private final XboxController m_Controller;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public DriveWithGamepad(DriveTrain subsystem, XboxController stick) {
    m_Drive = subsystem;
    m_Controller = stick;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    double zs = -m_Controller.getRawAxis(LEFT_JOYSTICK);
    double xs = m_Controller.getRawAxis(RIGHT_JOYSTICK);

    if (Math.abs(zs) < 0.2)
      zs = 0;
    if (Math.abs(xs) < 0.2) {
      xs = 0;
    }

    m_Drive.arcadeDrive(zs, xs);
    // m_Drive.odometryDrive(zs, xs);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
