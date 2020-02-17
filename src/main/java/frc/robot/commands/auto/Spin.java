/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
/** Why would you use this? */
public class Spin extends CommandBase {
  double speed;
  Drivetrain dt;
  /**
   * Creates a new Spin.
   */
  public Spin(double speed, Drivetrain dt) {
    this.speed = speed;

    // Use addRequirements() here to declare subsystem dependencies.
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
    dt.arcadeDrive(0, speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    dt.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return DriverStation.getInstance().isOperatorControl();
  }
}
