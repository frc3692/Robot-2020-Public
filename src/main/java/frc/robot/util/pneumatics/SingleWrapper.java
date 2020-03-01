/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util.pneumatics;

import edu.wpi.first.wpilibj.Solenoid;

public class SingleWrapper extends SolWrapper {
	Solenoid base;

	public SingleWrapper(int id) {
		super(new Solenoid(id));
		base = (Solenoid)getBase();

		base.set(false);
		set(SolState.kRev);
	}

	public void set(SolState state) {
		setState(state);
		base.set(state.getAsBoolean());
	}
}