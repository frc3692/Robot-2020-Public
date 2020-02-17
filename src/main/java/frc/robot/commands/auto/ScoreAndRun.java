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
import frc.robot.commands.auto.ramsete.FeederStationScore;
import frc.robot.commands.auto.ramsete.FeederStationWallScore;
import frc.robot.commands.auto.ramsete.PowerPortScore;
import frc.robot.singleton.SB;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

public class ScoreAndRun extends CommandBase {
  private Command autonCommand;

  public ScoreAndRun(int startingPos, RamseteController controller, Drivetrain dt, Intake intake, Arm arm) {

    Command angleArmScore = new RunCommand(() -> arm.set(1), arm);
    Command scoreAndRun = new RunCommand(() -> intake.out(), intake).withTimeout(1)
        .andThen(new ScoreTurn(controller, dt))
        .alongWith(
            new WaitCommand(.5).andThen(new RunCommand(() -> arm.set(-1), arm)))
        .andThen(new ScoreRun(controller, dt)).raceWith(new RunCommand(() -> intake.in(), intake));

    Command wait1 = new WaitCommand(SB.AutonDat.getInstance().getWait1());

    switch (startingPos) {
      case 0:
      // Power Port Wall
      autonCommand = wait1.andThen(new PowerPortScore(controller, dt)).alongWith(angleArmScore)
          .andThen(scoreAndRun);
      break;
    case 1:
      // Power Port
      autonCommand = wait1.andThen(new PowerPortScore(controller, dt)).alongWith(angleArmScore)
          .andThen(scoreAndRun);
      break;
    case 2:
      // Center
      autonCommand = wait1.andThen(new ScoreRun(controller, dt)).alongWith(angleArmScore)
          .andThen(scoreAndRun);
      break;
    case 3:
      // Feeder Station
      autonCommand = wait1.andThen(new FeederStationScore(controller, dt)).alongWith(angleArmScore)
          .andThen(scoreAndRun);
      break;
    case 4:
      // Feeder Station Wall
      autonCommand = wait1.andThen(new FeederStationWallScore(controller, dt)).alongWith(angleArmScore)
          .andThen(scoreAndRun);
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
