/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.singleton;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * Shuffleboard Controller
 */
public class SB {
    public static class DriveDat {
        private static DriveDat drive = new DriveDat();

        private DriveDat() {
        }

        public static DriveDat getInstance() {
            return drive;
        }

        private static final ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");

        private static final ShuffleboardLayout driveSpeeds = driveTab.getLayout("Drive Speeds", BuiltInLayouts.kList)
                .withPosition(0, 0).withSize(1, 2);
        private static final ShuffleboardLayout driveData = driveTab.getLayout("Misc Data", BuiltInLayouts.kList)
                .withPosition(2, 0).withSize(1, 2);
        private static final ShuffleboardLayout driveAmps = driveTab.getLayout("Amperage", BuiltInLayouts.kGrid)
                .withPosition(0, 2).withSize(3, 4);

        private static final NetworkTableEntry Slow = driveSpeeds.addPersistent("Slow Speed", 0.5).getEntry();
        private static final NetworkTableEntry Normal = driveSpeeds.addPersistent("Normal Speed", 0.75).getEntry();
        private static final NetworkTableEntry Boost = driveSpeeds.addPersistent("Boost Speed", 1).getEntry();

        private static final NetworkTableEntry Mult = driveData.addPersistent("Mult", 0.75)
                .withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("min", 0, "max", 1)).getEntry();
        private static final NetworkTableEntry Boosted = driveData.addPersistent("Boosted", false)
                .withWidget(BuiltInWidgets.kBooleanBox).getEntry();
        private static final NetworkTableEntry Slowed = driveData.addPersistent("Slowed", false)
                .withWidget(BuiltInWidgets.kBooleanBox).getEntry();

        private static final NetworkTableEntry FLAmperage = driveAmps.addPersistent("FL Amp Draw", 0)
                .withWidget(BuiltInWidgets.kGraph).withPosition(0, 0).getEntry();
        private static final NetworkTableEntry FRAmperage = driveAmps.addPersistent("FR Amp Draw", 0)
                .withWidget(BuiltInWidgets.kGraph).withPosition(1, 0).getEntry();
        private static final NetworkTableEntry BLAmperage = driveAmps.addPersistent("BL Amp Draw", 0)
                .withWidget(BuiltInWidgets.kGraph).withPosition(0, 1).getEntry();
        private static final NetworkTableEntry BRAmperage = driveAmps.addPersistent("BR Amp Draw", 0)
                .withWidget(BuiltInWidgets.kGraph).withPosition(1, 1).getEntry();

        public double getSlowSpeed() {
            return Slow.getDouble(0.5);
        }

        public double getNormalSpeed() {
            return Normal.getDouble(0.75);
        }

        public double getBoostSpeed() {
            return Boost.getDouble(1);
        }

        public void update(double mult, boolean boosted, boolean slowed, Number flamp, Number framp, Number blamp,
                Number bramp) {
            Mult.setDouble(mult);
            Boosted.setBoolean(boosted);
            Slowed.setBoolean(slowed);

            FLAmperage.setNumber(flamp);
            FRAmperage.setNumber(framp);
            BLAmperage.setNumber(blamp);
            BRAmperage.setNumber(bramp);
        }
    }

    public static class LightingDat {
        private static LightingDat lighting = new LightingDat();

        private LightingDat() {
        }

        public static LightingDat getInstance() {
            return lighting;
        }

        private static final ShuffleboardTab lightingTab = Shuffleboard.getTab("Lighting");
        private final NetworkTableEntry Mode = lightingTab.addPersistent("Mode", 1).getEntry();
        private final NetworkTableEntry Freeze = lightingTab.addPersistent("Freeze Mode", false).getEntry();

        public Number getMode() {
            return Mode.getNumber(1);
        }

        public boolean getFrozen() {
            return Freeze.getBoolean(false);
        }

        public void update(Number mode) {
            Mode.setNumber(mode);
        }
    }
}
