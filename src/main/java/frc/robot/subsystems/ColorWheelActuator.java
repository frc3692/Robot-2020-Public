/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ColorWheelConstants;

public class ColorWheelActuator extends SubsystemBase {
  private CANSparkMax m_liftMotor = new CANSparkMax(ColorWheelConstants.kLiftMotor, MotorType.kBrushless);
  private CANSparkMax m_wheelMotor = new CANSparkMax(ColorWheelConstants.kWheelMotor, MotorType.kBrushless);
  private CANPIDController m_wheelPID = m_wheelMotor.getPIDController();

  /**
   * Creates a new ColorWheelActuator.
   */
  public ColorWheelActuator() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setLift(double speed) {
    m_liftMotor.set(speed);
  }

  public void setWheel(double speed) {
    m_wheelPID.setReference(speed * ColorWheelConstants.kWheelSpeed, ControlType.kVelocity);
  }
}
