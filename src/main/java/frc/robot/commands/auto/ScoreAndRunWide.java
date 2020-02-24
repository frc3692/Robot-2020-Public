/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.DriveConstants;
import frc.robot.singleton.Trajectories;
import frc.robot.singleton.SB;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

public class ScoreAndRunWide extends CommandBase {
  private Command m_autonCommand;
  private RamseteController m_controller;
  private Drivetrain m_dt;

  public ScoreAndRunWide(int startingPos, RamseteController controller, Drivetrain dt, Intake intake, Arm arm) {
    Trajectories trajectories = Trajectories.getInstance();

    Command angleArmDown = new InstantCommand(() -> arm.setTarget(1), arm);
    Command scoreAndRun = new InstantCommand(() -> arm.setTarget(-1), arm).withTimeout(0.5)
        .andThen(new RunCommand(() -> intake.out(), intake).withTimeout(1)).andThen(ramseteFromTrajectory(trajectories.getScoreRun()))
        .andThen(ramseteFromTrajectory(trajectories.getScoreRun()).raceWith(new RunCommand(() -> intake.in(), intake)));

    Command wait1 = new WaitCommand(SB.AutonDat.getInstance().getWait1());
    switch (startingPos) {
      case 0:
        // Power Port Wall
        m_autonCommand = wait1
            .andThen(ramseteFromTrajectory(trajectories.getPowerPortWallScoreWide()).alongWith(angleArmDown))
            .andThen(scoreAndRun);
        break;
      case 1:
        // Power Port
        m_autonCommand = wait1.andThen(ramseteFromTrajectory(trajectories.getPowerPortScore()).alongWith(angleArmDown))
            .andThen(scoreAndRun);
        break;
      case 2:
        // Center
        m_autonCommand = wait1.andThen(ramseteFromTrajectory(trajectories.getCenterScoreWide()).alongWith(angleArmDown))
            .andThen(scoreAndRun);
        break;
      case 3:
        // Feeder Station
        m_autonCommand = wait1
            .andThen(ramseteFromTrajectory(trajectories.getFeederStationScoreWide()).alongWith(angleArmDown))
            .andThen(scoreAndRun);
        break;
      case 4:
        // Feeder Station Wall
        m_autonCommand = wait1
            .andThen(ramseteFromTrajectory(trajectories.getFeederStationWallScoreWide()).alongWith(angleArmDown))
            .andThen(scoreAndRun);
        break;
      default:
        DriverStation.reportError("Error - Invalid Starting Position", true);
      }
  }

  private RamseteCommand ramseteFromTrajectory(Trajectory trajectory) {
    return new RamseteCommand(trajectory, m_dt::getPose, m_controller, DriveConstants.kFeedForward,
        DriveConstants.kDriveConstants, m_dt::getWheelSpeeds, new PIDController(DriveConstants.kPDriveVel, 0, 0),
        new PIDController(DriveConstants.kPDriveVel, 0, 0), m_dt::voltageDrive, m_dt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_autonCommand.schedule();
  }

  @Override
  public boolean isFinished() {
    return m_autonCommand.isFinished();
  }
}
