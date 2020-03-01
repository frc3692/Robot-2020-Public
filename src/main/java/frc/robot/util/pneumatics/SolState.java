/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util.pneumatics;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public enum SolState {
	kFwd, kRev, kOff;

	public static SolState get(Value v) {
		switch (v) {
			case kForward:
				return SolState.kFwd;
			case kReverse:
				return SolState.kRev;
			case kOff:
				return SolState.kOff;
			default:
				return null;
		}
	}

	public static SolState get(boolean b) {
		return (b) ? SolState.kFwd : SolState.kRev;
	}

	public Value getAsValue() {
		switch (this) {
			case kFwd:
				return Value.kForward;
			case kRev:
				return Value.kReverse;
			case kOff:
				return Value.kOff;
			default:
				return null;
		}
	}

	public boolean getAsBoolean() {
		return this == kFwd;
	}

	public SolState invert() {
		return ((this == SolState.kRev) ? SolState.kFwd : SolState.kRev);
	}
}