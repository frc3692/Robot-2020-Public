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
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Drivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Run extends InstantCommand {
  private Command command;
  public Run(int startingPos, RamseteController controller, Drivetrain dt) {
    switch (startingPos) {
    case 0:
      
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
    command.schedule();
  }
}
