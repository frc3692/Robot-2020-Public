/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import java.util.EnumMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class DS4 extends Joystick implements AutoCloseable {
	public static enum DSButton {
		kSq(1), kX(2), kO(3), kTri(4), kLB(5), kRB(6), kLT(7), kRT(8), kShare(9), kOptions(10), kPS(13), kPOVNone(-1, true),
		kUp(0, true), kUpRight(45, true), kRight(90, true), kDownRight(135, true), kDown(180, true), kDownLeft(225, true),
		kLeft(270, true), kUpLeft(315, true);

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

	public static enum DSAxis {
		kLX(0), kLY(1), kRX(2), kLT(3), kRT(4), kRY(5);

		private int id;

		private DSAxis(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	public static enum CommandSet {
		kPrimary, kSecondary
	}

	private double deadband = 0.02;
	private EnumMap<DSButton, Button> buttons;

	private CommandSet m_commandSet = CommandSet.kPrimary;

	public DS4(int port) {
		super(port);
		buttons = new EnumMap<>(DSButton.class);
	}

	public DS4(int port, double deadband) {
		this(port);
		this.deadband = deadband;
	}


	public void setMod(DSButton btn) {
		getBtn(btn).whileHeld(
				new StartEndCommand(() -> m_commandSet = CommandSet.kSecondary, () -> m_commandSet = CommandSet.kPrimary));
	}

	public Command getDualModeCommand(Command primary, Command secondary) {
		return new ConditionalCommand(primary, secondary, () -> m_commandSet == CommandSet.kPrimary);
	}

	public Command getDualModeCommand(Runnable primary, Runnable secondary) {
		return getDualModeCommand(new InstantCommand(primary), new InstantCommand(secondary));
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
		return getRawAxis(DSAxis.kRT) - getRawAxis(DSAxis.kLT);
	}

	public double getNetTriggers(boolean applyDeadband) {
		return getRawAxis(DSAxis.kRT, applyDeadband) - getRawAxis(DSAxis.kLT, applyDeadband);
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