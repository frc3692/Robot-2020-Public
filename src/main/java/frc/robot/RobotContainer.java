/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.drive.ArcadeDrive;
import frc.robot.misc.DS4;
import frc.robot.misc.DS4.DSButton;
import frc.robot.subsystems.Drivetrain;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  public static class Config {
    public static boolean Inverted = false;

    public static double SlowSpd = 0.5;
    public static double NormalSpd = 0.75;
    public static double BoostSpd = 1;
  }

  // Subsystems
  private final Drivetrain m_Drivetrain = new Drivetrain();

  // Sensors
  private final DS4 m_driveController = new DS4(0);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // Configure Driver Controls
    m_driveController.getBtn(DSButton.psBtn).whenPressed(new InstantCommand(() -> Config.Inverted = !Config.Inverted));

    m_driveController.getBtn(DSButton.lt).whenPressed(new InstantCommand(() -> m_Drivetrain.slow(true), m_Drivetrain))
        .whenReleased(new InstantCommand(() -> m_Drivetrain.slow(false), m_Drivetrain));

    m_driveController.getBtn(DSButton.rt).whenPressed(new InstantCommand(() -> m_Drivetrain.boost(true), m_Drivetrain))
        .whenReleased(new InstantCommand(() -> m_Drivetrain.boost(false), m_Drivetrain));

    m_Drivetrain.setDefaultCommand(
        new ArcadeDrive(() -> m_driveController.getY(), () -> m_driveController.getZ(), m_Drivetrain));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Build Route
    return null;
  }
}
