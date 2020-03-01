/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.singleton;

import frc.robot.util.Debug;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Log;
import io.github.oblarg.oblog.annotations.Config;

/**
 * Shuffleboard Controller
 */
@Log.Exclude
public class SB {
    private SB() {
    }
    
    public static class AutoDat implements Loggable {
        @Log.Exclude
        private static AutoDat auto = new AutoDat();

        public static AutoDat getInstance() {
            return auto;
        }

        public void setInstance() {
            auto = this;
        }

        @Override
        public String configureLogName() {
            return "Auto";
        }

        private static class AutoChooser implements Loggable {
            @Log.Exclude
            private static AutoChooser m_autoChooser = new AutoChooser();

            public static AutoChooser getInstance() {
                return m_autoChooser;
            }
            
            @Log
            private final SendableChooser<Integer> positionChooser = new SendableChooser<>(), routineChooser = new SendableChooser<>();

            public void setDefaultPosition(String key, int value) {
                positionChooser.setDefaultOption(key, value);
            }

            public void addPosition(String key, int value) {
                positionChooser.addOption(key, value);
            }

            public void setDefaultRoutine(String key, int value) {
                routineChooser.setDefaultOption(key, value);
            }

            public void addRoutine(String key, int value) {
                routineChooser.addOption(key, value);
            }

            public int getPosition() {
                return positionChooser.getSelected();
            }

            public int getRoutine() {
                return routineChooser.getSelected();
            }

            // Oblog Layout Config
            @Override
            public String configureLogName() {
                return "Autonomous Chooser";
            }

            @Override
            public int[] configureLayoutPosition() {
                return new int[] {0, 0};
            }

            @Override
            public int[] configureLayoutSize() {
                return new int[] {2, 2};
            }

            @Override
            public LayoutType configureLayoutType() {
                return BuiltInLayouts.kList;
            }

            // Oblog Config
            @Override
            public boolean skipLayout() {
                return true;
            }
        }

        private static class WaitTimes implements Loggable {
            @Log.Exclude
            private static WaitTimes waitTimes = new WaitTimes();

            private WaitTimes() {
            }

            public static WaitTimes getInstance() {
                return waitTimes;
            }

            @Log(name = "Wait 1")
            private double wait1 = 0;
            @Log(name = "Wait 2")
            private double wait2 = 0;
            @Log(name = "Wait 3")
            private double wait3 = 0;

            public double getWait1() {
                return wait1;
            }

            public double getWait2() {
                return wait2;
            }

            public double getWait3() {
                return wait3;
            }

            // Oblog Config
            
            @Override
            public String configureLogName() {
                return "Wait Time";
            }

            @Override
            public int[] configureLayoutPosition() {
                return new int[] { 2, 0 };
            }

            @Override
            public int[] configureLayoutSize() {
                return new int[] { 2, 2 };
            }

            @Override
            public LayoutType configureLayoutType() {
                return BuiltInLayouts.kList;
            }
        }

        @Log(name = "Chosen Auto", rowIndex = 2, columnIndex = 0, width = 3, height = 1)
        private String chosenAuto = "Do Nothing";

        private boolean pushing = false;

        private final String[] startingPos = { "Power Port Wall", "Power Port", "Center", "Feeder Station",
                "Feeder Station Wall" },
                routines = { "Steal 2 & Score 15", "Steal All & Score 15", "Score Trench & Generator Switch Balls",
                        "Steal All & Score Generator Switch Balls", "Steal All & Score Trench", "10 Ball",
                        "Score Trench", "Score Generator Switch Balls", "Steal All & Score", "Score and Run",
                        "Score Wide and Run", "Run Only", "Everybot", "Move from Initiation Line", "Simple Score",
                        "Spin", "Do Nothing" };
        private AutoChooser autoChooser = AutoChooser.getInstance();
        private WaitTimes waitTimes = WaitTimes.getInstance();

        public AutoDat() {
            if(auto != null)
                throw new RuntimeException("Only one instance of Auto can be created");
                
            if(!DriverStation.getInstance().isFMSAttached()) {
                // Put in test autos
            }

            Debug.log("Auto");

            autoChooser.addPosition("Power Port Wall", 0);
            autoChooser.addPosition("Power Port", 1);
            autoChooser.addPosition("Center", 2);
            autoChooser.addPosition("Feeder Station", 3);
            autoChooser.setDefaultPosition("Feeder Station Wall", 4);
            
            
            /*autoChooser.addRoutine("Steal 2, Score Trench, and Generator Switch (15 Balls) (Feeder Station Only)", 0);
            autoChooser.addRoutine("Steal Trench, Score Trench, and Generator Switch (15 Balls) (Feeder Station Only & No Preload)", 1);
            autoChooser.addRoutine("Score Trench and Generator Switch (13 Balls) ", 2);
            autoChooser.addRoutine("Steal Trench and Score Generator Switch (10 Balls) (Feeder Station Only & No Preload)", 3);
            autoChooser.addRoutine("Steal Trench and Score Trench (10 Balls) (Feeder Station Only & No Preload)", 4);
            autoChooser.addRoutine("10 Ball Auto (Steals 2 when starting from Feeder Station, otherwise takes from Switch)", 5);
            autoChooser.addRoutine("Score Trench (8 Balls)", 6);
            autoChooser.addRoutine("Score Generator Switch (8 Balls)", 7);
            autoChooser.addRoutine("Steal Trench (5 Balls) (No Preload & Recommended Start at Feeder Station)", 8);
            autoChooser.addRoutine("Score and Run (3 Balls)", 9);
            autoChooser.addRoutine("Score Wide and Run (3 Balls)", 10);
            autoChooser.addRoutine("Run Only (0 Balls) (No Preload)", 11);
            autoChooser.addRoutine("Everybot (0 Balls)", 12);*/
            autoChooser.addRoutine("Drive from initiation line", 13);
            autoChooser.addRoutine("Simple Score (only from Power Port)", 14);
            autoChooser.addRoutine("Spin (This auto is a joke, do not choose it unless you don't want to score any points)", 15); // I'm leaving this in
            autoChooser.setDefaultRoutine("Do Nothing", 16);
        }

        public void periodic() {
            chosenAuto = (pushing ? "Push then " : "") + startingPos[autoChooser.getPosition()] + " " + routines[autoChooser.getRoutine()];
        }

        public double getWait1() {
            return waitTimes.getWait1();
        }

        public double getWait2() {
            return waitTimes.getWait2();
        }

        public double getWait3() {
            return waitTimes.getWait3();
        }

        public boolean getPushing() {
            return pushing;
        }

        public int getStartingPosition() {
            return autoChooser.getPosition();
        }

        public int getRoutine() {
            return autoChooser.getRoutine();
        }

        @Config.ToggleSwitch(name = "Push another bot?", rowIndex = 3, columnIndex = 1, width = 2, height = 1)
        private void setPush(boolean pushing) {
            this.pushing = pushing;
        }
    }

    public static class LightingDat implements Loggable {
        @Log.Exclude
        private static LightingDat lighting = new LightingDat();
        
        public static LightingDat getInstance() {
            return lighting;
        }

        @Config(name = "Mode")
        private int mode = 1;
        private boolean frozen = false;

        private LightingDat() {
        }

        public int getMode() {
            return mode;
        }

        public boolean isFrozen() {
            return frozen;
        }

        public void update(int mode) {
            this.mode = mode;
        }

        @Config.ToggleSwitch(name = "Freeze Lighting")
        public void freeze(boolean enabled) {
            frozen = enabled;
        }
    }

    public static class Cameras implements Loggable {
        @Log.Exclude
        private static Cameras cameras = new Cameras();

        public static Cameras getInstance() {
            return cameras;
        }

        
        @Log.CameraStream(rowIndex = 0, columnIndex = 0, width = 4, height = 4)
        private VideoSource cam1 = CameraServer.getInstance().startAutomaticCapture(0);
        @Log.CameraStream(rowIndex = 0, columnIndex = 4, width = 4, height = 4)
        private VideoSource cam2 = CameraServer.getInstance().startAutomaticCapture(1);
        


        private Cameras() {
            //Logger.configureLogging(this);

            cam1.setFPS(20);
            cam1.setResolution(320, 240);

            cam2.setFPS(20);
            cam2.setResolution(320, 240);
        }
    }
}
