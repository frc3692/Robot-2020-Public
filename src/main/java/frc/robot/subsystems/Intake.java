/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  private final TalonSRX m_wheels = new TalonSRX(IntakeConstants.kMotor);

  /**
   * Creates a new BallSucker.
   */
  public Intake() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void in() {
    m_wheels.set(ControlMode.PercentOutput, IntakeConstants.kSpeed);
  }

  public void out() {
    m_wheels.set(ControlMode.PercentOutput, -IntakeConstants.kSpeed);
  }

  public void set(double speed) {
    m_wheels.set(ControlMode.PercentOutput, speed);
  }
}
