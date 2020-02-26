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
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;

public class Run extends CommandBase {
  private Command m_autonCommand;
  private RamseteController m_controller;
  private Drivetrain m_dt;
  
  public Run(int startingPos, RamseteController controller, Drivetrain dt) {
    switch (startingPos) {
    case 0:
      
      break;
    case 1:

      break;
    case 2:

      break;
    case 3:

      break;
    case 4:

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
