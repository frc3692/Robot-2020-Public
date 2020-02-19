/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util.pneumatics;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public enum SolState {
	fwd, rev;

	public static SolState get(Value v) {
		switch (v) {
		case kForward:
			return SolState.fwd;
		case kReverse:
			return SolState.rev;
		default:
			return null;
		}
	}

	public static SolState get(boolean b) {
		return (b) ? SolState.fwd : SolState.rev;
	}

	public SolState invert() {
		return ((this == SolState.rev) ? SolState.fwd : SolState.rev);
	}
}