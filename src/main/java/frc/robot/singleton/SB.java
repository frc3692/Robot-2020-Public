/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.singleton;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.RobotContainer;
import frc.robot.util.Debug;

/**
 * Shuffleboard Controller
 */
public class SB {
    public static class AutonDat {
        private final static AutonDat auto = new AutonDat();

        public static AutonDat getInstance() {
            return auto;
        }

        private final ShuffleboardTab autoTab = Shuffleboard.getTab("Auto");
        private final ShuffleboardLayout autoChooserList = autoTab.getLayout("Autonomous Chooser", BuiltInLayouts.kList).withPosition(0, 0).withSize(2, 2);
        private final ShuffleboardLayout waitList = autoTab.getLayout("Wait times", BuiltInLayouts.kList).withPosition(0, 2).withSize(2, 2);

        private final NetworkTableEntry chosenAuto = autoTab.add("Chosen Auto", "Power Port Score and Run").withPosition(2, 0).withSize(3, 1).getEntry();
        private final NetworkTableEntry push = autoTab.add("Push another bot?", false).withWidget(BuiltInWidgets.kToggleButton).withPosition(3, 1).withSize(2, 1).getEntry();

        private final NetworkTableEntry wait1 = waitList.add("Wait 1", 0).getEntry();
        private final NetworkTableEntry wait2 = waitList.add("Wait 2", 0).getEntry();
        private final NetworkTableEntry wait3 = waitList.add("Wait 3", 0).getEntry();
        private final SendableChooser<Integer> positionChooser = new SendableChooser<>(), routineChooser = new SendableChooser<>();

        private final String[] startingPos = {
            "Power Port Wall",
            "Power Port",
            "Center",
            "Feeder Station",
            "Feeder Station Wall"
        },
        routines = {
            "Steal 2 & Score 15",
            "Steal All & Score 15",
            "Score Trench & Generator Switch Balls",
            "Steal All & Score Generator Switch Balls",
            "Steal All & Score Trench",
            "10 Ball",
            "Score Trench",
            "Score Generator Switch Balls",
            "Steal All & Score",
            "Score and Run",
            "Score Wide and Run",
            "Run Only",
            "Everybot",
            "Spin"
        };
        private AutonDat() {
            if(!DriverStation.getInstance().isFMSAttached()) {
                // Put in test autos
            }
            Debug.log("Auto");
            positionChooser.addOption("Power Port Wall", 0);
            positionChooser.addOption("Power Port", 1);
            positionChooser.addOption("Center", 2);
            positionChooser.addOption("Feeder Station", 3);
            positionChooser.setDefaultOption("Feeder Station Wall", 4);
            
            
            routineChooser.setDefaultOption("Steal 2, Score Trench, and Generator Switch (15 Balls) (Feeder Station Only)", 0);
            routineChooser.addOption("Steal Trench, Score Trench, and Generator Switch (15 Balls) (Feeder Station Only & No Preload)", 1);
            routineChooser.addOption("Score Trench and Generator Switch (13 Balls) ", 2);
            routineChooser.addOption("Steal Trench and Score Generator Switch (10 Balls) (Feeder Station Only & No Preload)", 3);
            routineChooser.addOption("Steal Trench and Score Trench (10 Balls) (Feeder Station Only & No Preload)", 4);
            routineChooser.addOption("10 Ball Auto (Steals 2 when starting from Feeder Station, otherwise takes from Switch)", 5);
            routineChooser.addOption("Score Trench (8 Balls)", 6);
            routineChooser.addOption("Score Generator Switch (8 Balls)", 7);
            routineChooser.addOption("Steal Trench (5 Balls) (No Preload & Recommended Start at Feeder Station)", 8);
            routineChooser.addOption("Score and Run (3 Balls)", 9);
            routineChooser.addOption("Score Wide and Run (3 Balls)", 10);
            routineChooser.addOption("Run Only (0 Balls) (No Preload)", 11);
            routineChooser.addOption("Everybot (0 Balls)", 12);
            routineChooser.addOption("Spin (This auto is a joke, do not choose it unless you don't want to score any points)", 13); // I'm leaving this in

            autoChooserList.add(positionChooser);
        }

        public int getStartingPosition() {
            return positionChooser.getSelected();
        }

        public int getRoutine() {
            return routineChooser.getSelected();
        }

        public void periodic() {
            chosenAuto.forceSetString((getPushing() ? "Push then " : "") + startingPos[positionChooser.getSelected()] + " " + routines[routineChooser.getSelected()]);
        }

        public double getWait1() {
            return wait1.getDouble(0);
        }

        public double getWait2() {
            return wait2.getDouble(0);
        }

        public double getWait3() {
            return wait3.getDouble(0);
        }

        public boolean getPushing() {
            return push.getBoolean(false);
        }
    }

    public static class DriveDat {
        private final static DriveDat drive = new DriveDat();

        public static DriveDat getInstance() {
            return drive;
        }

        private final ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");

        private final ShuffleboardLayout driveSpeeds = driveTab.getLayout("Drive Speeds", BuiltInLayouts.kList)
                .withPosition(0, 0).withSize(1, 2);
        private final ShuffleboardLayout driveData = driveTab.getLayout("Misc Data", BuiltInLayouts.kList)
                .withPosition(2, 0).withSize(1, 2);
        private final ShuffleboardLayout driveAmps = driveTab.getLayout("Amperage", BuiltInLayouts.kGrid)
                .withProperties(Map.of("rows", 2, "columns", 2))
                .withPosition(0, 2).withSize(3, 4);

        private final NetworkTableEntry Slow = driveSpeeds.addPersistent("Slow Speed", 0.5).getEntry();
        private final NetworkTableEntry Normal = driveSpeeds.addPersistent("Normal Speed", 0.75).getEntry();
        private final NetworkTableEntry Boost = driveSpeeds.addPersistent("Boost Speed", 1).getEntry();

        private final NetworkTableEntry Mult = driveData.addPersistent("Mult", 0.75)
                .withProperties(Map.of("min", 0, "max", 1)).getEntry();
        private final NetworkTableEntry Boosted = driveData.addPersistent("Boosted", false)
                .withWidget(BuiltInWidgets.kBooleanBox).getEntry();
        private final NetworkTableEntry Slowed = driveData.addPersistent("Slowed", false)
                .withWidget(BuiltInWidgets.kBooleanBox).getEntry();

        private final NetworkTableEntry FLAmperage = driveAmps.addPersistent("FL Amp Draw", 0)
                .withWidget(BuiltInWidgets.kGraph).withPosition(0, 0).getEntry();
        private final NetworkTableEntry FRAmperage = driveAmps.addPersistent("FR Amp Draw", 0)
                .withWidget(BuiltInWidgets.kGraph).withPosition(1, 0).getEntry();
        private final NetworkTableEntry BLAmperage = driveAmps.addPersistent("BL Amp Draw", 0)
                .withWidget(BuiltInWidgets.kGraph).withPosition(0, 1).getEntry();
        private final NetworkTableEntry BRAmperage = driveAmps.addPersistent("BR Amp Draw", 0)
                .withWidget(BuiltInWidgets.kGraph).withPosition(1, 1).getEntry();

        private DriveDat() {
        }

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

        public void periodic() {
            RobotContainer.Config.BoostSpd = getBoostSpeed();
            RobotContainer.Config.NormalSpd = getNormalSpeed();
            RobotContainer.Config.SlowSpd = getSlowSpeed();
        }
    }

    public static class LightingDat {
        private static LightingDat lighting = new LightingDat();

        public static LightingDat getInstance() {
            return lighting;
        }

        private final ShuffleboardTab lightingTab = Shuffleboard.getTab("Lighting");
        private final NetworkTableEntry Mode = lightingTab.addPersistent("Mode", 1).getEntry();
        private final NetworkTableEntry Freeze = lightingTab.addPersistent("Freeze Mode", false).getEntry();

        private LightingDat() {
        }

        public int getMode() {
            return Mode.getNumber(1).intValue();
        }

        public boolean getFrozen() {
            return Freeze.getBoolean(false);
        }

        public void update(Number mode) {
            Mode.setNumber(mode);
        }
    }
}