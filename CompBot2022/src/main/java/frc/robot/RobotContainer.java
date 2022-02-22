// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DriveWithGamepad;
import frc.robot.commands.ShootingCommand;
import frc.robot.commands.TurnToAngle;
import frc.robot.commands.DriveBack;
import frc.robot.commands.AutoTarget;
import frc.robot.commands.DriveToPath;
import frc.robot.subsystems.Aiming;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooting;
import frc.robot.subsystems.Limelight;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  //subsystem
  private final XboxController m_Controller = new XboxController(0);
  private final DriveTrain m_Drive = new DriveTrain();
  private final Limelight m_limelight = new Limelight(m_Controller);
  private final Shooting m_shoot = new Shooting();
  private final Aiming m_aim = new Aiming(m_Drive, m_limelight);
  private final Autonomous m_auto = new Autonomous(m_Drive, m_aim, m_shoot);
 

  //command
  private final TurnToAngle m_turnToAngle = new TurnToAngle(m_Drive, 90);
  private final DriveWithGamepad m_teleOP = new DriveWithGamepad(m_Drive, m_Controller);
  private final DriveToPath m_driveToPath = new DriveToPath(m_Drive);
  private final ShootingCommand m_shootingCommand = new ShootingCommand(m_shoot, m_Controller, m_aim, m_Drive);


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    m_Drive.setDefaultCommand(new DriveWithGamepad(m_Drive, m_Controller));
    m_shoot.setDefaultCommand(new ShootingCommand(m_shoot, m_Controller, m_aim, m_Drive));
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {}

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_auto.getCommand();
  }

  public void reset() {
    m_Drive.reset();
  }
}
