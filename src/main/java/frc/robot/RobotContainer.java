/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants.DriveConstants;
import frc.robot.commands.arm.ArmLoop;
import frc.robot.commands.auto.DriveDistance;
import frc.robot.commands.auto.ScoreAndRun;
import frc.robot.commands.auto.ScoreAndRunWide;
import frc.robot.commands.drive.ArcadeDrive;
import frc.robot.commands.intake.IntakeLoop;
import frc.robot.singleton.SB;
import frc.robot.util.DS4;
import frc.robot.util.DS4.DSButton;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ColorWheelActuator;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

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
  private final Arm m_arm = new Arm();
  private final Intake m_intake = new Intake();
  private final Climber m_climber = new Climber();
  private final Drivetrain m_drivetrain = new Drivetrain();
  private final ColorWheelActuator m_colorWheelActuator = new ColorWheelActuator();

  // Sensors
  private final DS4 m_driveController = new DS4(0);
  private final DS4 m_mechanismController = new DS4(1, 0.1);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Set up subsystems
    m_drivetrain.setDefaultCommand(
        new ArcadeDrive(() -> m_driveController.getY(), () -> m_driveController.getZ(), m_drivetrain));

    // m_colorWheelActuator.setDefaultCommand(new ColorLoop(() ->
    // m_mechanismController.getY(true),
    // () -> m_mechanismController.getZ(true), m_colorWheelActuator));

    m_arm.setDefaultCommand(new ArmLoop(() -> m_mechanismController.getY(true), m_arm));

    m_intake.setDefaultCommand(new IntakeLoop(() -> m_mechanismController.getNetTriggers(), m_intake));
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

    m_driveController.getBtn(DSButton.lt).whenPressed(new InstantCommand(() -> m_drivetrain.slow(true), m_drivetrain))
        .whenReleased(new InstantCommand(() -> m_drivetrain.slow(false), m_drivetrain));

    m_driveController.getBtn(DSButton.rt).whenPressed(new InstantCommand(() -> m_drivetrain.boost(true), m_drivetrain))
        .whenReleased(new InstantCommand(() -> m_drivetrain.boost(false), m_drivetrain));

    // Configure Mechanism Controls
    m_mechanismController.getBtn(DSButton.povU).whenPressed(new RunCommand(() -> m_arm.set(1)));
    m_mechanismController.getBtn(DSButton.povD).whenPressed(new RunCommand(() -> m_arm.set(-1)));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    Command autonCommand = null;
    RamseteController controller = new RamseteController(DriveConstants.kRamseteB, DriveConstants.kRamseteZeta);

    switch (SB.AutonDat.getInstance().getRoutine()) {
    case 0:
      // Score and Run
      autonCommand = new ScoreAndRun(SB.AutonDat.getInstance().getStartingPosition(), controller, m_drivetrain, m_intake, m_arm);
      break;
    case 1:
      // Score and Run Wide]
      autonCommand = new ScoreAndRunWide(SB.AutonDat.getInstance().getStartingPosition(), controller, m_drivetrain, m_intake, m_arm);
    }
    if(autonCommand != null)
      if(SB.AutonDat.getInstance().getPushing())
        autonCommand = new DriveDistance(-DriveConstants.kFrameLength/2, m_drivetrain).andThen(autonCommand);
      autonCommand = autonCommand.withInterrupt(() -> m_driveController.getRawButton(DSButton.o) || m_mechanismController.getRawButton(DSButton.o));
    return null;
  }

  public void end() {
    m_drivetrain.stop();
  }
}
