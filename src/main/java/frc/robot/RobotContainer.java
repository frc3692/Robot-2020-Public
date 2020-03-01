/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Map;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.DriveConstants;
import frc.robot.commands.auto.DriveDistance;
import frc.robot.commands.auto.SimpleScore;
import frc.robot.commands.auto.ScoreAndRun;
import frc.robot.commands.auto.ScoreAndRunWide;
import frc.robot.commands.auto.SimpleDriveDist;
import frc.robot.commands.auto.Spin;
import frc.robot.commands.colorwheel.ColorLoop;
import frc.robot.commands.colorwheel.PositionControl;
import frc.robot.commands.colorwheel.RotationControl;
import frc.robot.commands.drive.ArcadeDrive;
import frc.robot.commands.intake.IntakeLoop;
import frc.robot.commands.lift.WinchLoop;
import frc.robot.singleton.SB;
import frc.robot.singleton.SB.AutoDat;
import frc.robot.singleton.SB.Cameras;
import frc.robot.singleton.SB.LightingDat;
import frc.robot.util.DS4;
import frc.robot.util.RobotState;
import frc.robot.util.DS4.DSAxis;
import frc.robot.util.DS4.DSButton;
import frc.robot.util.RobotState.State;
import frc.robot.util.pneumatics.SolState;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.Logger;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.ColorWheelManipulator;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer implements Loggable {
  //private AutoDat autoDat = new AutoDat();
  //private LightingDat lightingDat = LightingDat.getInstance();
  //private Cameras cameras = Cameras.getInstance();
  
  public static class Config {
    public static boolean Inverted = false;

    public static double SlowSpd = 0.5;
    public static double NormalSpd = 0.75;
    public static double BoostSpd = 1;
  }

  // Subsystems
  private final Arm m_arm = new Arm();
  private final Intake m_intake = new Intake();
  private final Lift m_lift = new Lift();
  private final Drivetrain m_drivetrain = new Drivetrain();
  private final ColorWheelManipulator m_colorWheelManipulator = new ColorWheelManipulator();

  // Sensors
  private final DS4 m_driveController = new DS4(0, 0.03);
  private final DS4 m_mechanismController = new DS4(1, 0.03);

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
    // Configure Axis
    m_drivetrain.setDefaultCommand(
        new ArcadeDrive(() -> m_driveController.getY(true), () -> m_driveController.getZ(true), m_drivetrain));

    // Configure buttons
    m_driveController.getBtn(DSButton.kLT).whenPressed(() -> m_drivetrain.slow(true), m_drivetrain)
        .whenReleased(() -> m_drivetrain.slow(false), m_drivetrain);

    m_driveController.getBtn(DSButton.kRT).whenPressed(() -> m_drivetrain.boost(true), m_drivetrain)
        .whenReleased(() -> m_drivetrain.boost(false), m_drivetrain);

    m_driveController.getBtn(DSButton.kPS).whenPressed(() -> Config.Inverted = !Config.Inverted);

    // Configure Mechanism Controller
    m_mechanismController.setMod(DSButton.kLB);

    // Configure Color Wheel Controls
    // Configure Axis
    m_colorWheelManipulator.setDefaultCommand(new ColorLoop(() -> m_mechanismController.getY(true),
        () -> m_mechanismController.getRawAxis(DSAxis.kRX, true), m_colorWheelManipulator));

    // Configure Buttons
    m_mechanismController.getBtn(DSButton.kTri).whileHeld(m_mechanismController.getDualModeCommand(
        new RotationControl(m_colorWheelManipulator), new PositionControl(m_colorWheelManipulator)));

    // Configure Intake
    // Configure Axis
    m_intake.setDefaultCommand(new IntakeLoop(() -> m_mechanismController.getNetTriggers(true), m_intake));

    // Configure Arm & Lift
    // Configure Axis
    m_lift.setDefaultCommand(new WinchLoop(() -> m_mechanismController.getRawAxis(DSAxis.kRY, true), m_lift));

    // Configure Buttons
    m_mechanismController.getBtn(DSButton.kRB).whenPressed(() -> m_arm.hold(true), m_arm)
        .whenReleased(() -> m_arm.hold(false), m_arm);

    m_mechanismController.getBtn(DSButton.kUp).whenPressed(
        getStateDependantCommand(new InstantCommand(() -> m_arm.setSetpoint(1), m_arm), new InstantCommand(() -> {
          if (m_lift.getStage2State() == SolState.kFwd) {
            m_lift.disengageStage2();
          } else {
            m_lift.disengageStage1();
          }
        }, m_lift)));

    m_mechanismController.getBtn(DSButton.kDown).whenPressed(
        getStateDependantCommand(new InstantCommand(() -> m_arm.setSetpoint(-1), m_arm), new InstantCommand(() -> {
          if (m_lift.getStage1State() == SolState.kFwd) {
            m_lift.engageStage2();
          } else {
            m_lift.engageStage1();
          }
        }, m_lift)));

    m_mechanismController.getBtn(DSButton.kPS)
        .whenPressed(getStateDependantCommand(new InstantCommand(() -> m_lift.initEndgame(m_arm), m_lift, m_arm),
            new InstantCommand(() -> m_lift.undoEndgame(m_arm), m_lift, m_arm)));
  }

  private Command getStateDependantCommand(Command auto, Command telop, Command endgame) {
    return new SelectCommand(
        Map.ofEntries(Map.entry(State.kAuto, auto), Map.entry(State.kTelop, telop), Map.entry(State.kEndgame, endgame)),
        RobotState::getState);
  }

  private Command getStateDependantCommand(Command telop, Command endgame) {
    return getStateDependantCommand(new InstantCommand(), telop, endgame);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    Command autonCommand = new InstantCommand();
    Command wait1 = new WaitCommand(SB.AutoDat.getInstance().getWait1());
    RamseteController controller = new RamseteController(DriveConstants.kRamseteB, DriveConstants.kRamseteZeta);

    switch (SB.AutoDat.getInstance().getRoutine()) {
      case 0:

        break;
      case 1:

        break;
      case 3:

        break;
      case 4:

        break;
      case 5:

        break;
      case 6:

        break;
      case 7:

        break;
      case 9: // Score and Run
        autonCommand = new ScoreAndRun(SB.AutoDat.getInstance().getStartingPosition(), controller, m_drivetrain,
            m_intake, m_arm);
        break;
      case 10: // Score and Run Wide
        autonCommand = new ScoreAndRunWide(SB.AutoDat.getInstance().getStartingPosition(), controller, m_drivetrain,
            m_intake, m_arm);
        break;
      case 11:

        break;
      case 12:

        break;
      case 13:
        autonCommand = new SimpleDriveDist(DriveConstants.kFrameLength, DriveConstants.kAutoSpeed, m_drivetrain);
        break;
      case 14:
        autonCommand = new SimpleScore(SB.AutoDat.getInstance().getStartingPosition(), m_drivetrain, m_intake, m_arm);
        break;
      case 15: // Why
        autonCommand = new Spin(0.5, m_drivetrain);
        break;
    }

    autonCommand = wait1.andThen(autonCommand);

    if (SB.AutoDat.getInstance().getPushing())
      autonCommand = new DriveDistance(-DriveConstants.kFrameLength / 2, m_drivetrain).andThen(autonCommand);

    autonCommand = autonCommand.withInterrupt(
        () -> m_driveController.getRawButton(DSButton.kO) || m_mechanismController.getRawButton(DSButton.kO));

    return autonCommand;
  }

  public void end() {
    m_drivetrain.stop();
  }

    // Oblog
    @Override
    public String configureLogName() {
      return "Diagnostics";
    }
}
