/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class VelocityDrive extends CommandBase {
  private final DoubleSupplier spd, rot;
  private final Drivetrain dt;

  /**
   * Creates a new ArcadeDrive.
   */
  public VelocityDrive(DoubleSupplier spd, DoubleSupplier rot, Drivetrain dt) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.spd = spd;
    this.rot = rot;

    this.dt = dt;
    addRequirements(dt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    dt.velocityDrive(spd.getAsDouble(), rot.getAsDouble());
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
