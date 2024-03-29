/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.lift;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.util.RobotState;
import frc.robot.util.RobotState.State;
import frc.robot.subsystems.Lift;

public class WinchLoop extends CommandBase {
  DoubleSupplier m_speed;
  Lift m_lift;
  /**
   * Creates a new LiftLoop.
   */
  public WinchLoop(DoubleSupplier speed, Lift lift) {
    m_speed =speed;
    // Use addRequirements() here to declare subsystem dependencies.
    m_lift = lift;
    addRequirements(lift);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(RobotState.getState() == State.kEndgame) {
      if(DriverStation.getInstance().isFMSAttached()) {
        m_lift.runWinch(-Math.abs(m_speed.getAsDouble()));
      } else {
        m_lift.runWinch(m_speed.getAsDouble());
      }
    } else {
      m_lift.runWinch(0);
    }
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
