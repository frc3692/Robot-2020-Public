/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.singleton;

import edu.wpi.first.wpilibj.Spark;
import frc.robot.Constants.MiscConstants;
import frc.robot.singleton.SB.LightingDat;

public class Lighting {
  private final static Lighting lighting = new Lighting();

  private Lighting() {
  }

  public static Lighting getInstance() {
    return lighting;
  }


  private final Spark Blinkin = new Spark(MiscConstants.kBlinkinPort);
  private int mode = 1;
  private double val = -0.99;

  public void update() {
    // This method will be called once per scheduler run
    int sbMode = (int) LightingDat.getInstance().getMode();
    if (mode != sbMode) {
      if (sbMode > 100 || sbMode < 1) {
        LightingDat.getInstance().update(mode);
      } else {
        mode = sbMode;
        updateVal(mode);
      }
    }

    Blinkin.set(val);
  }

  public void setMode(int mode) {
    this.mode = mode;
    LightingDat.getInstance().update(mode);
    updateVal(mode);
  }

  private void updateVal(int mode) {
    val = -1.01 + (.02 * mode);
  }
}
