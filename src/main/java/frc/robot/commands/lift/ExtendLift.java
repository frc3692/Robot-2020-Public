/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.lift;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Lift;
import frc.robot.util.pneumatics.SolState;

public class ExtendLift extends CommandBase {
  private boolean m_finished = false;
  private Lift m_lift;
  private double m_target;
  /**
   * Creates a new ExtendLift.
   */
  public ExtendLift(Lift lift) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_lift = lift;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_lift.getStage1State() == SolState.kFwd) {
      m_lift.engageStage2();
      m_target = -55;
    } else {
      m_lift.engageStage1();
      m_target = -55
      ;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_finished = m_lift.runToPosition(m_target, -0.5);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_lift.runWinch(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_finished;
  }
}
