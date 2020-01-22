/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.misc.DS4;
import frc.robot.misc.DSButton;
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

  // The robot's subsystems and commands are defined here...
  public Drivetrain m_Drivetrain = new Drivetrain();

  public final DS4 driveController = new DS4(0);

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
    driveController.getBtn(DSButton.psBtn).whenPressed(() -> Config.Inverted = !Config.Inverted);

    driveController.getBtn(DSButton.lb)
        .whenPressed(() -> m_Drivetrain.slow(true))
        .whenReleased(() -> m_Drivetrain.slow(false));

    driveController.getBtn(DSButton.rb)
        .whenPressed(() -> m_Drivetrain.boost(true))
        .whenReleased(() -> m_Drivetrain.boost(false));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return null;
  }
}
