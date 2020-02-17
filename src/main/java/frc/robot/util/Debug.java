/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Add your docs here.
 */
public class Debug {
    public static void log(String message) {
        if(!DriverStation.getInstance().isFMSAttached()) {
            System.out.println(message);
        }
    }

    public static void dsLogWarning(String message) {
        if(!DriverStation.getInstance().isFMSAttached()) {
            DriverStation.reportWarning(message, true);
        }
    }

    public static void dsLogError(String message) {
        if(!DriverStation.getInstance().isFMSAttached()) {
            DriverStation.reportError(message, true);
        }
    }
}
