/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

/**
 * Add your docs here.
 */
public class RobotState {
    public static enum State {
        kAuto, kTelop, kEndgame
    }

    private static State m_state = State.kAuto;
    
    public static State getState() {
        return m_state;
    }

    public static void updateState(State state) {
        m_state = state;
    }
}
