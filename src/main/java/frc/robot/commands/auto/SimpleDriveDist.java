/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.singleton.Gyro;
import frc.robot.subsystems.Drivetrain;

public class SimpleDriveDist extends CommandBase {
  private Drivetrain m_dt;
  private double m_leftTarget, m_rightTarget;
  private double m_speed;
  private boolean m_reversed;
  private int m_stopCycles = 0;
  /**
   * Creates a new SimpleDriveDist.
   */
  public SimpleDriveDist(double dist, double speed, Drivetrain dt) {
    m_leftTarget = dt.getLeftDist() + dist;
    m_rightTarget = dt.getRightDist() + dist;
    m_speed = speed;

    m_reversed = dist < 0;
    if(m_reversed && m_speed > 0) {
      m_speed = -m_speed;
    }
    // Use addRequirements() here to declare subsystem dependencies.
    m_dt = dt;
    addRequirements(dt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_dt.arcadeDrive(m_speed, 0);
    if(Gyro.getInstance().getAccel() < 0.1)
      m_stopCycles++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_dt.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(m_reversed) {
      return (m_dt.getLeftDist() < m_leftTarget && m_dt.getRightDist() > m_rightTarget) || m_stopCycles >= 25 /* Half a second */;
    }
    return (m_dt.getLeftDist() > m_leftTarget && m_dt.getRightDist() < m_rightTarget) || m_stopCycles >= 25 /* Half a second */;
  }
}
