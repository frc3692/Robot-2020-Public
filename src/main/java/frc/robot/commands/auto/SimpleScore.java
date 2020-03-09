/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.commands.arm.ArmScore;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

public class SimpleScore extends CommandBase {
  private Command m_autonCommand;

  public SimpleScore(int startingPos, Drivetrain dt, Intake intake, Arm arm) {
    switch (startingPos) {
      case 1:
        m_autonCommand = new DriveStraight(DriveConstants.kAutoSpeed, dt).withTimeout(10).withInterrupt(() -> arm.getPosition() < ArmConstants.kUpPos)
            .raceWith(new StartEndCommand(() -> arm.hold(true), () -> arm.hold(false), arm)).andThen(new ArmScore(arm))
            .andThen(new RunCommand(() -> intake.out(), intake).withTimeout(4));
        break;
      default:
        m_autonCommand = new DriveStraight(DriveConstants.kAutoSpeed, dt).withTimeout(3);
    }
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
