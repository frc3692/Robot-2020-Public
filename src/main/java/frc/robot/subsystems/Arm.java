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

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ArmConstants;

public class Arm extends SubsystemBase {
  private CANSparkMax m_motor = new CANSparkMax(ArmConstants.kMotor, MotorType.kBrushless);
  private DutyCycleEncoder m_enc = new DutyCycleEncoder(ArmConstants.kEnc);
  private double m_setpoint = 0;

  private boolean m_forceDown = false, holding = true;

  /**
   * Creates a new Arm.
   */
  public Arm() {
    m_motor.restoreFactoryDefaults();
    m_motor.clearFaults();
    m_motor.setOpenLoopRampRate(0);
    m_motor.setIdleMode(IdleMode.kBrake);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Encoder Pos", m_enc.getDistance());
    SmartDashboard.putNumber("Arm Amps", m_motor.getOutputCurrent());
    SmartDashboard.putNumber("Enc vel", m_motor.getEncoder().getVelocity());
    SmartDashboard.putNumber("Arm Temp", m_motor.getMotorTemperature());

    double setpoint = m_setpoint;
    if (m_forceDown)
      setpoint = -1;

    double speed = 0;
    if (holding) {
      if(m_enc.get() > ArmConstants.kFallPos) {
        speed = ArmConstants.kHelpSpeed;
      } else if(m_enc.get() < ArmConstants.kRestPos) {
        speed = -ArmConstants.kHelpSpeed;
      }
    } else {
      if (setpoint > 0 && m_enc.get() < ArmConstants.kRestPos) {
        // At the bottom and lifting
        speed = ArmConstants.kLiftSpeed;
      } else if (setpoint > 0 && m_enc.get() < ArmConstants.kSpringPos) {
        // In the middle of lifting
        speed = ArmConstants.kHelpSpeed;
      } else if (setpoint > 0 && m_enc.get() > ArmConstants.kSpringPos) {
        // Lifted
        speed = 0;
      } else if (setpoint < 0 && m_enc.get() > ArmConstants.kFallPos) {
        // At the top and going down
        speed = ArmConstants.kDropSpeed;
      }
    }
    m_motor.set(speed);
    SmartDashboard.putNumber("Arm Speed", speed);
  }

  public void forceDown() {
    m_forceDown = true;
  }

  public void setSetpoint(double setpoint) {
    m_setpoint = setpoint;
  }

  public void hold(boolean enabled) {
    holding = enabled;
  }

  public double getPosition() {
    return m_enc.get();
  }
}
