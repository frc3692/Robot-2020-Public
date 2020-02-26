/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;

public class DriveDistance extends CommandBase {
  private double dist;
  private Drivetrain dt;
  /**
   * Creates a new DriveDistance.
   */
  public DriveDistance(double dist, Drivetrain dt) {
    this.dist = dist;
    // Use addRequirements() here to declare subsystem dependencies.
    this.dt = dt;
    addRequirements(dt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    dt.updatePosSetpoint(dist);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    dt.distDrive(dist);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    dt.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //                            Prevent this from running until the robot is characterized
    return dt.atPosSetpoint() || DriveConstants.kPDriveVel == 0;
  }
}
