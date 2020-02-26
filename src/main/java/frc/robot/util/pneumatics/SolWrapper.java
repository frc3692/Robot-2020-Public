/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util.pneumatics;

import edu.wpi.first.wpilibj.SolenoidBase;

public abstract class SolWrapper {
	private SolState state;
	private SolenoidBase base;
	private boolean isInverted;

	public abstract void set(SolState state);

	protected SolWrapper(SolenoidBase base) {
		this.base = base;
	}

	public void fwd() {
		set(SolState.kFwd);
	}

	public void rev() {
		set(SolState.kRev);
	}

	public SolState getState() {
		return state;
	}

	public boolean getInverted() {
		return isInverted;
	}

	public void setInverted(boolean isInverted) {
		this.isInverted = isInverted;
	}

	public SolenoidBase getBase() {
		return base;
	}

	protected void setState(SolState state) {
		this.state = state;
	}

	public void toggle() {
		state = state.invert();
		set(state);
	}
}