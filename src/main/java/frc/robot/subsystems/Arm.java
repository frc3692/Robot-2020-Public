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
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ArmConstants;

public class Arm extends SubsystemBase {
  private CANSparkMax m_motor = new CANSparkMax(ArmConstants.kMotor, MotorType.kBrushless);
  private CANPIDController m_PIDController = m_motor.getPIDController();
  private DutyCycleEncoder m_enc = new DutyCycleEncoder(ArmConstants.kEnc);
  private double m_setpoint = ArmConstants.retractedAngle;

  /**
   * Creates a new ReservoirArm.
   */
  public Arm() {
    m_motor.restoreFactoryDefaults();
    m_motor.clearFaults();
    m_motor.setOpenLoopRampRate(0);
    m_motor.setIdleMode(IdleMode.kBrake);
    
    m_PIDController.setP(ArmConstants.kPVel);
    m_PIDController.setSmartMotionMaxAccel(ArmConstants.kMaxAccel, 0);
    m_PIDController.setSmartMotionMaxVelocity(ArmConstants.kMaxVel, 0);
  }

  public void setSetPoint(double setpoint) {
    m_setpoint = setpoint;
  }

  @Override
  public void periodic() {
    //double pos = ((m_enc.get() - m_setpoint) * ArmConstants.kGearRatio) + m_motor.getEncoder().getPosition();
    //m_PIDController.setReference(pos, ControlType.kSmartMotion);
    SmartDashboard.putNumber("Encoder Pos", m_enc.getDistance());
    SmartDashboard.putNumber("Arm Amps", m_motor.getOutputCurrent());
    SmartDashboard.putNumber("Enc vel", m_motor.getEncoder().getVelocity());
    SmartDashboard.putNumber("Arm Temp", m_motor.getMotorTemperature());
  }

  public void stop() {
    m_setpoint = m_enc.get();
  }

  public void set(double speed) {
    speed = -speed;
    if(speed < 0 && m_enc.get() < 0.09) {
      m_motor.set(0.25 * speed);
      SmartDashboard.putNumber("Speed", 0.25 * speed);
    } else if(speed < 0 && m_enc.get() < 0.18) {
      m_motor.set(0.15 * speed);
      SmartDashboard.putNumber("Speed", 0.15 * speed);
    } else if(speed < 0 && m_enc.get() > .18) {
      m_motor.set(0);
      SmartDashboard.putNumber("Speed", 0);
    } else if(speed > 0 && m_enc.get() > .2){
      m_motor.set(speed * 0.15);
      SmartDashboard.putNumber("Speed", 0.15 * speed);
    } else {
      m_motor.set(0);
      SmartDashboard.putNumber("Speed", 0);

    }

  }

  private boolean inTol(double val, double set, double tol) {
    return (val > (set - tol) && val < (set + tol));
  }
}
