/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ColorWheelConstants;
import io.github.oblarg.oblog.annotations.Log;

public class ColorWheelManipulator extends SubsystemBase {
  private CANSparkMax m_liftMotor = new CANSparkMax(ColorWheelConstants.kLiftMotor, MotorType.kBrushless);
  private CANSparkMax m_wheelMotor = new CANSparkMax(ColorWheelConstants.kWheelMotor, MotorType.kBrushless);
  private CANPIDController m_wheelPID = m_wheelMotor.getPIDController();

  private ColorSensorV3 m_colorSensor = new ColorSensorV3(Port.kOnboard);
  private ColorMatch m_matcher = new ColorMatch();

  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

  @Log
  private String colorString = "";

  /**
   * Creates a new ColorWheelActuator.
   */
  public ColorWheelManipulator() {
    m_matcher.addColorMatch(kBlueTarget);
    m_matcher.addColorMatch(kGreenTarget);
    m_matcher.addColorMatch(kRedTarget);
    m_matcher.addColorMatch(kYellowTarget);

    m_wheelMotor.restoreFactoryDefaults();
    m_wheelMotor.setMotorType(MotorType.kBrushless);
    m_wheelMotor.setIdleMode(IdleMode.kBrake);

    m_wheelPID.setP(ColorWheelConstants.kP);
    m_wheelPID.setI(ColorWheelConstants.kI);
    m_wheelPID.setD(0);

    m_wheelPID.setOutputRange(-0.35, 0.35);

    m_wheelPID.setFF(ColorWheelConstants.kFF);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (m_colorSensor.getProximity() < 1500) {
      Color detectedColor = m_colorSensor.getColor();

      ColorMatchResult match = m_matcher.matchClosestColor(detectedColor);

      if (match.color == kBlueTarget) {
        colorString = "Blue";
      } else if (match.color == kRedTarget) {
        colorString = "Red";
      } else if (match.color == kGreenTarget) {
        colorString = "Green";
      } else if (match.color == kYellowTarget) {
        colorString = "Yellow";
      } else {
        colorString = "Unknown";
      }
    } else {
      colorString = "Not in range";
    }
  }

  public void setLift(double speed) {
    m_liftMotor.set(speed * .2);
    SmartDashboard.putNumber("lift speed", speed * .2);
  }

  public void setWheel(double speed) {
    // if(speed == 0) {
    // m_wheelMotor.set(0);
    // } else {
    m_wheelPID.setReference(speed * ColorWheelConstants.kMotorSpeed, ControlType.kVelocity, 0,
        ColorWheelConstants.kFeedforward.calculate(speed));
    // }
    SmartDashboard.putNumber("Setpoint", speed * ColorWheelConstants.kMotorSpeed);
  }

  public void spin(boolean reversed) {
    double setpoint = reversed ? ColorWheelConstants.kMotorSpeed : -ColorWheelConstants.kMotorSpeed;
    m_wheelPID.setReference(setpoint, ControlType.kVelocity, 0, ColorWheelConstants.kFeedforward.calculate(setpoint));
  }

  public double getWheelPos() {
    return m_wheelMotor.getEncoder().getPosition();
  }
}
