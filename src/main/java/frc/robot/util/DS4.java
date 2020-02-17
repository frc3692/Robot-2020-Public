/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import java.util.EnumMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class DS4 extends Joystick implements AutoCloseable {
	public enum DSButton {
		sq(1), x(2), o(3), tri(4), lb(5), rb(6), lt(7), rt(8), share(9), options(10), psBtn(13), povNone(-1, true),
		povU(0, true), povUR(45, true), povR(90, true), povDR(135, true), povD(180, true), povDL(225, true),
		povL(270, true), povUL(315, true);

		private int id;
		private boolean isPov = false;

		private DSButton(int id) {
			this.id = id;
		}

		private DSButton(int id, boolean isPov) {
			this.id = id;
			this.isPov = isPov;
		}

		public int getId() {
			return id;
		}

		public boolean isPov() {
			return isPov;
		}
	}

	public enum DSAxis {
		lx(0), ly(1), rx(2), rt(3), lt(4), ry(5);

		private int id;

		private DSAxis(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	private double deadband = 0.02;
	private EnumMap<DSButton, Button> buttons;

	public DS4(int port) {
		super(port);
		buttons = new EnumMap<>(DSButton.class);
	}

	public DS4(int port, double deadband) {
		this(port);
		this.deadband = deadband;
	}

	public Button getBtn(DSButton b) {
		if (buttons.containsKey(b))
			return buttons.get(b);
		Button btn = b.isPov() ? new POVButton(this, b.getId()) : new JoystickButton(this, b.getId());
		buttons.put(b, btn);
		return btn;
	}

	public boolean getRawButton(DSButton btn) {
		return getRawButton(btn.getId());
	}

	@Override
	public boolean getRawButton(int id) {
		return super.getRawButton(id);
	}

	public boolean getSqBtn() {
		return getRawButton(1);
	}

	public boolean getXBtn() {
		return getRawButton(2);
	}

	public boolean getCirBtn() {
		return getRawButton(3);
	}

	public boolean getTriBtn() {
		return getRawButton(4);
	}

	public boolean getLb() {
		return getRawButton(5);
	}

	public boolean getRb() {
		return getRawButton(6);
	}

	public boolean getLtBtn() {
		return getRawButton(7);
	}

	public boolean getRbBtn() {
		return getRawButton(8);
	}

	public boolean getShare() {
		return getRawButton(9);
	}

	public boolean getOptions() {
		return getRawButton(10);
	}

	public boolean getPovNone() {
		return getPOV() == -1;
	}

	public boolean getPovU() {
		return getPOV() == 0;
	}

	public boolean getPovR() {
		return getPOV() == 90;
	}

	public boolean getPovD() {
		return getPOV() == 180;
	}

	public boolean getPovL() {
		return getPOV() == 270;
	}

	public double getRawAxis(DSAxis axis) {
		return getRawAxis(axis.getId());
	}

	public double getRawAxis(DSAxis axis, boolean applyDeadband) {
		if (applyDeadband)
			return applyDeadband(getRawAxis(axis.getId()));
		return getRawAxis(axis.getId());
	}

	public double getRawAxis(int id, boolean applyDeadband) {
		if (applyDeadband)
			return applyDeadband(getRawAxis(id));
		return getRawAxis(id);
	}

	public double getX(boolean applyDeadband) {
		if (applyDeadband)
			return applyDeadband(super.getX());
		return getX();
	}

	public double getY(boolean applyDeadband) {
		if (applyDeadband)
			return applyDeadband(-getY());
		return -getY(); // For whatever reason, vertical axis are inverted
	}

	public double getZ(boolean applyDeadband) {
		if (applyDeadband)
			return applyDeadband(getZ());
		return getZ();
	}

	public double getTwist(boolean applyDeadband) {
		if (applyDeadband)
			return applyDeadband(getTwist());
		return getTwist();
	}

	public double getNetTriggers() {
		return getRawAxis(DSAxis.rt) - getRawAxis(DSAxis.lt);
	}

	public double getNetTriggers(boolean applyDeadband) {
		return getRawAxis(DSAxis.rt, applyDeadband) - getRawAxis(DSAxis.lt, applyDeadband);
	}

	private double applyDeadband(double value) {
		// Copied from DifferentialDrive
		if (Math.abs(value) > deadband) {
			if (value > 0.0) {
				return (value - deadband) / (1.0 - deadband);
			} else {
				return (value + deadband) / (1.0 - deadband);
			}
		} else {
			return 0.0;
		}
	}

	public void close() {
		buttons.clear();
	}
}