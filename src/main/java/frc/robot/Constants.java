/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
    // Drivetrain constants
    public static final class DriveConstants {
        public static final int kFrontLeft = 1;
        public static final int kBackLeft = 2;
        public static final int kFrontRight = 3;
        public static final int kBackRight = 4;

        // Assuming it's the wheel Circumference
        public static final double kWheelCirc = 0.1524 * Math.PI;

        // Auto
        // TODO: Get constants from frc robot characterization tool
        public static final double ksVolts = 0;
        public static final double ksVoltSecondsPerMeter = 0;
        public static final double kaVoltSecondsSquaredPerMeter = 0;

        public static final double KPDriveVel = 0;

        public static final double kTrackWidthMeters = 0;
        public static final DifferentialDriveKinematics kDriveConstants = new DifferentialDriveKinematics(
                kTrackWidthMeters);

        // Reasonable baseline values for a RAMSETE follower in units of meters and
        // seconds
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;
    }

    public static final class Misc {
        public static final int kBlinkinPort = 0;
    }
}
