/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.misc;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * Shuffleboard Controller
 */
public class SB {
    private static ShuffleboardTab drive = Shuffleboard.getTab("Drive");
    public static class Drive {
        public static NetworkTableEntry Slow = drive.addPersistent("Slow Speed", 0.5).getEntry();
        public static NetworkTableEntry Normal = drive.addPersistent("Normal Speed", 0.75).getEntry();
        public static NetworkTableEntry Boost = drive.addPersistent("Boost Speed", 1).getEntry();
    }
}
