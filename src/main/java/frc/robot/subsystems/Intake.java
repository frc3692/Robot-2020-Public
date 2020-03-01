/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  private final CANSparkMax m_motor = new CANSparkMax(IntakeConstants.kMotor, MotorType.kBrushless);

  /**
   * Creates a new Intake.
   */
  public Intake() {
    m_motor.restoreFactoryDefaults();
    m_motor.clearFaults();
    m_motor.setMotorType(MotorType.kBrushless);
    m_motor.setSmartCurrentLimit(40);
    m_motor.setIdleMode(IdleMode.kBrake);

    m_motor.burnFlash();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void in() {
    m_motor.set(IntakeConstants.kSpeed);
  }

  public void out() {
    m_motor.set(-IntakeConstants.kSpeed);
  }

  public void set(double speed) {
    m_motor.set(speed * IntakeConstants.kSpeed);
  }
}
