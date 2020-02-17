/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
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
        // Electrical
        public static final int kFrontLeft = 1;
        public static final int kBackLeft = 2;
        public static final int kFrontRight = 3;
        public static final int kBackRight = 4;

        // Mechanical
        /** Wheel circumference in meters */
        public static final double kWheelCirc = 0.1524 /* 6 in */ * Math.PI;
        /** Gear ratio of the toughbox mini */
        public static final double kGearRatio = 12.75;
        /** Frame width in meters (includes bumpers) */
        public static final double kFrameWidth = 0.9144; // 36 in
        /** Frame length in meters (includes bumpers) */
        public static final double kFrameLength = 0.94615; // 37 1/4 in
        /** Wheelbase in meters */
        public static final double kTrackWidth = 0.6096; // 24 in

        // PID
        /** Encoder distance per revolution in m */
        public static final double kEncDPR = kWheelCirc / kGearRatio;

        // Auto
        // TODO: Find max accel and vel
        public static final double kMaxAccel = 6;
        public static final double kMaxVel = 3;

        // TODO: Get constants from frc robot characterization tool
        public static final double ksVolts = 0;
        public static final double ksVoltSecondsPerMeter = 0;
        public static final double kaVoltSecondsSquaredPerMeter = 0;

        public static final double kPDriveVel = 0;

        public static final DifferentialDriveKinematics kDriveConstants = new DifferentialDriveKinematics(kTrackWidth);
        public static final SimpleMotorFeedforward kFeedForward = new SimpleMotorFeedforward(ksVolts,
                ksVoltSecondsPerMeter, kaVoltSecondsSquaredPerMeter);

        // Reasonable baseline values for a RAMSETE follower in units of meters and
        // seconds
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;
    }

    public static final class ArmConstants {
        public static final int kMotor = 5;

        public static final int kEnc = 0;
        /** Encoder counts per revolution */
        public static final int kEncCPR = 8192;

        public static final double kRestPos = 0.9;
        public static final double kSpringPos = 0.18;
        public static final double kFallPos = 0.2;

        public static final double kInitialSpeed = 0.25;
        public static final double kHelpSpeed = 0.15;
    }

    public static final class IntakeConstants {
        public static final int kMotor = 9;
        public static final double kSpeed = .6;
    }

    public static final class ColorWheelConstants {
        public static final int kLiftMotor = 6;
        public static final int kWheelMotor = 7;

        /** Max motor speed in RPM */
        public static final int kWheelSpeed = 3200;
    }

    public static final class LiftConstants {
        public static final int kRelease = 0;
    }

    public static final class MiscConstants {
        public static final int kBlinkinPort = 0;
    }
}
