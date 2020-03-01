/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util.pneumatics;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class DoubleWrapper extends SolWrapper {
	DoubleSolenoid base;

	public DoubleWrapper(int fwd, int rev) {
		super(new DoubleSolenoid(fwd, rev));
		this.base = (DoubleSolenoid)getBase();
		base.set(Value.kReverse);

		set(SolState.kRev);
	}

	public void set(SolState state) {
		setState(state);
		base.set(state.getAsValue());
	}
}