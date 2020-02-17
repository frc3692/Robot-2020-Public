/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LiftConstants;
import frc.robot.util.pneumatics.SingleWrapper;
import frc.robot.util.pneumatics.SolWrapper;

public class Lift extends SubsystemBase {
  private SolWrapper release = new SingleWrapper(LiftConstants.kRelease);

  private boolean released = false;
  /**
   * Creates a new Climber.
   */
  public Lift() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(released) {
      release.rev();
    }
  }

  public void release(Arm arm) {
    arm.forceDown();
    released = true;
  }
}
