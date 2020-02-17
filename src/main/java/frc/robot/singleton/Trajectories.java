/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.singleton;

import java.io.IOException;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import frc.robot.Constants.DriveConstants;

/**
 * Add your docs here.
 */
public class Trajectories {
    private static Trajectories commands = new Trajectories();

    public static Trajectories getInstance() {
        return commands;
    }

    private Trajectory S1, S2, S3, S4, S5, S1W, S3W, S4W, S5W, ST, SR;

    private Trajectories() {
        try {
            S1 = TrajectoryUtil.fromPathweaverJson(
                    Filesystem.getDeployDirectory().toPath().resolve("paths\\S1 PPW.wpilib.json"));
        } catch (IOException e) {
            DriverStation.reportError(e.getMessage(), e.getStackTrace());
        }
        try {
            S2 = TrajectoryUtil
                    .fromPathweaverJson(Filesystem.getDeployDirectory().toPath().resolve("paths\\S2 PP.wpilib.json"));
        } catch (IOException e) {
            DriverStation.reportError(e.getMessage(), e.getStackTrace());
        }
        try {
            S3 = TrajectoryUtil
                    .fromPathweaverJson(Filesystem.getDeployDirectory().toPath().resolve("paths\\S3 C.wpilib.json"));
        } catch (IOException e) {
            DriverStation.reportError(e.getMessage(), e.getStackTrace());
        }
        try {
            S4 = TrajectoryUtil
                    .fromPathweaverJson(Filesystem.getDeployDirectory().toPath().resolve("paths\\S4 FS.wpilib.json"));
        } catch (IOException e) {
            DriverStation.reportError(e.getMessage(), e.getStackTrace());
        }
        try {
            S5 = TrajectoryUtil.fromPathweaverJson(
                    Filesystem.getDeployDirectory().toPath().resolve("paths\\S5 FSW.wpilib.json"));
        } catch (IOException e) {
            DriverStation.reportError(e.getMessage(), e.getStackTrace());
        }
        try {
            S1W = TrajectoryUtil.fromPathweaverJson(
                    Filesystem.getDeployDirectory().toPath().resolve("paths\\SW1 PPW.wpilib.json"));
        } catch (IOException e) {
            DriverStation.reportError(e.getMessage(), e.getStackTrace());
        }
        try {
            ST = reverseTrajectory(TrajectoryUtil
                    .fromPathweaverJson(Filesystem.getDeployDirectory().toPath().resolve("paths\\ST.wpilib.json")));
        } catch (IOException e) {
            DriverStation.reportError(e.getMessage(), e.getStackTrace());
        }
        
        try {
            SR = TrajectoryUtil
                    .fromPathweaverJson(Filesystem.getDeployDirectory().toPath().resolve("paths\\SR.wpilib.json"));
        } catch (IOException e) {
            DriverStation.reportError(e.getMessage(), e.getStackTrace());
        }
    }

    private Trajectory reverseTrajectory(Trajectory trajectory) {
        ArrayList<Pose2d> waypoints = new ArrayList<>();
        TrajectoryConfig config = new TrajectoryConfig(DriveConstants.kMaxVel, DriveConstants.kMaxAccel).setReversed(true);
        for(var state : ST.getStates()) {
            waypoints.add(state.poseMeters);
        }
        return TrajectoryGenerator.generateTrajectory(waypoints, config);
    }

    public Trajectory getPowerPortWallScore() {
        return S1;
    }

    public Trajectory getPowerPortScore() {
        return S2;
    }

    public Trajectory getCenterScore() {
        return S3;
    }

    public Trajectory getFeederStationScore() {
        return S4;
    }

    public Trajectory getFeederStationWallScore() {
        return S5;
    }

    public Trajectory getScoreTurn() {
        return ST;
    }

    public Trajectory getScoreRun() {
        return SR;
    }

    public Trajectory getPowerPortWallScoreWide() {
        return S1W;
    }
}
