/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto.ramsete;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants.DriveConstants;
import frc.robot.singleton.Trajectories;
import frc.robot.subsystems.Drivetrain;

public class PowerPortWallScoreWide extends RamseteCommand {
  /**
   * Creates a new PowerPortWallScore.
   */
  public PowerPortWallScoreWide(RamseteController controller, Drivetrain dt) {
    super(Trajectories.getInstance().getPowerPortWallScoreWide(), dt::getPose, controller, DriveConstants.kFeedForward, DriveConstants.kDriveConstants, dt::getWheelSpeeds,
        new PIDController(DriveConstants.kPDriveVel, 0, 0), new PIDController(DriveConstants.kPDriveVel, 0, 0),
        dt::voltageDrive, dt);
  }
}
