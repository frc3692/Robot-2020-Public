/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util.pneumatics;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public enum SolState {
	kFwd, kRev;

	public static SolState get(Value v) {
		switch (v) {
		case kForward:
			return SolState.kFwd;
		case kReverse:
			return SolState.kRev;
		default:
			return null;
		}
	}

	public static SolState get(boolean b) {
		return (b) ? SolState.kFwd : SolState.kRev;
	}

	public SolState invert() {
		return ((this == SolState.kRev) ? SolState.kFwd : SolState.kRev);
	}
}