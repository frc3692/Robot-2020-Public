/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.ArmConstants;
import frc.robot.commands.auto.ramsete.ScoreRun;
import frc.robot.commands.auto.ramsete.ScoreTurn;
import frc.robot.singleton.Trajectories;
import frc.robot.singleton.SB;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

public class ScoreAndRunWide extends CommandBase {
  private Command autonCommand;
  public ScoreAndRunWide(int startingPos, RamseteController controller, Drivetrain dt, Intake intake, Arm arm) {
    Trajectories auto = Trajectories.getInstance();

    Command angleArmScore = new InstantCommand(() -> arm.setSetPoint(ArmConstants.kScoreAngle), arm);
    Command scoreAndRun = new RunCommand(() -> intake.out(), intake).withTimeout(1)
        .andThen(new ScoreTurn(controller, dt))
        .alongWith(
            new WaitCommand(.5).andThen(new InstantCommand(() -> arm.setSetPoint(ArmConstants.kForwardAngle), arm)))
        .andThen(new ScoreRun(controller, dt)).raceWith(new RunCommand(() -> intake.in(), intake));

    Command wait1 = new WaitCommand(SB.AutonDat.getInstance().getWait1());
    switch (startingPos) {
    case 0:
      autonCommand = wait1;
      break;
    case 1:

      break;
    case 2:

      break;
    case 3:

      break;
    case 4:

      break;
    default:
      DriverStation.reportError("Error - Invalid Starting Position", true);
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    autonCommand.schedule();
  }

  @Override
  public boolean isFinished() {
    return autonCommand.isFinished();
  }
}
