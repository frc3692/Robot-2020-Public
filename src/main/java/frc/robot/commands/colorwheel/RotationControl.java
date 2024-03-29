/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.colorwheel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ColorWheelManipulator;
import frc.robot.util.Debug;

public class RotationControl extends CommandBase {
  private ColorWheelManipulator m_colorwheel;
  private double target;
  private boolean rev = false;
  /**
   * Creates a new PositionControl.
   */
  public RotationControl(ColorWheelManipulator colorwheel) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(colorwheel);
    m_colorwheel = colorwheel;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  if(rev)
    target = -189 + m_colorwheel.getWheelPos(); // 3.5 rotations, wheels seem to slip
  else
    target = 189 + m_colorwheel.getWheelPos();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_colorwheel.spin(rev);
    Debug.log("POS - " + m_colorwheel.getWheelPos() + "| Target - " + target);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return rev ? m_colorwheel.getWheelPos() <= target : m_colorwheel.getWheelPos() >= target;
  }
}
