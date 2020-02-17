/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.colorwheel;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ColorWheelActuator;

public class ColorLoop extends CommandBase {
  private ColorWheelActuator colorWheelActuator;
  private DoubleSupplier liftSpeed, wheelSpeed;

  /**
   * Creates a new RunLift.
   */
  public ColorLoop(DoubleSupplier liftSpeed, DoubleSupplier wheelSpeed, ColorWheelActuator colorWheelActuator) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.liftSpeed = liftSpeed;

    this.colorWheelActuator = colorWheelActuator;
    addRequirements(colorWheelActuator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    colorWheelActuator.setLift(liftSpeed.getAsDouble());
    colorWheelActuator.setWheel(wheelSpeed.getAsDouble());
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
