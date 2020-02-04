/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.singleton.SB.DriveDat;
import frc.robot.singleton.Gyro;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {
  /**
   * Creates a new Drivetrain.
   */
  private final CANSparkMax m_frontLeft = new CANSparkMax(DriveConstants.kFrontLeft, MotorType.kBrushless);
  private final CANSparkMax m_backLeft = new CANSparkMax(DriveConstants.kBackLeft, MotorType.kBrushless);
  private final CANSparkMax m_frontRight = new CANSparkMax(DriveConstants.kFrontRight, MotorType.kBrushless);
  private final CANSparkMax m_backRight = new CANSparkMax(DriveConstants.kBackRight, MotorType.kBrushless);

  private final DifferentialDrive m_drive = new DifferentialDrive(m_frontLeft, m_frontRight);

  private final CANEncoder m_leftEncoder = m_frontLeft.getAlternateEncoder(AlternateEncoderType.kQuadrature, 8192);
  private final CANEncoder m_rightEncoder = m_frontRight.getAlternateEncoder(AlternateEncoderType.kQuadrature, 8192);

  private final DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(
      Rotation2d.fromDegrees(Gyro.getInstance().getHeading()));

  private boolean slow = false, boost = false;

  public Drivetrain() {
    m_backLeft.follow(m_frontLeft);
    m_backRight.follow(m_frontRight);

    // Set up encoders for wheels
    m_leftEncoder.setPositionConversionFactor(DriveConstants.kWheelCirc);
    m_leftEncoder.setVelocityConversionFactor(DriveConstants.kWheelCirc);

    m_rightEncoder.setPositionConversionFactor(DriveConstants.kWheelCirc);
    m_rightEncoder.setVelocityConversionFactor(DriveConstants.kWheelCirc);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    RobotContainer.Config.SlowSpd = DriveDat.getInstance().getSlowSpeed();
    RobotContainer.Config.NormalSpd = DriveDat.getInstance().getNormalSpeed();
    RobotContainer.Config.BoostSpd = DriveDat.getInstance().getBoostSpeed();

    double mult = RobotContainer.Config.NormalSpd;

    if (slow) {
      mult = RobotContainer.Config.SlowSpd;
    } else if (boost) {
      mult = RobotContainer.Config.BoostSpd;
    }

    m_drive.setMaxOutput(mult);

    DriveDat.getInstance().update(mult, boost, slow, m_frontLeft.getOutputCurrent(), m_frontRight.getOutputCurrent(),
        m_backLeft.getOutputCurrent(), m_backRight.getOutputCurrent());

    m_odometry.update(Rotation2d.fromDegrees(Gyro.getInstance().getHeading()), m_leftEncoder.getPosition(),
        m_rightEncoder.getPosition());
  }

  public void arcadeDrive(double xSpeed, double zRotation) {
    if (RobotContainer.Config.Inverted)
      xSpeed = -xSpeed;
    m_drive.arcadeDrive(xSpeed, zRotation);
  }

  public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn) {
    if (RobotContainer.Config.Inverted)
      xSpeed = -xSpeed;
    m_drive.curvatureDrive(xSpeed, zRotation, isQuickTurn);
  }

  public void voltageDrive(double lVolts, double rVolts) {
    m_frontLeft.setVoltage(lVolts);
    m_frontRight.setVoltage(rVolts);
    m_drive.feed();
  }

  public void slow(boolean enabled) {
    slow = enabled;
  }

  public void boost(boolean enabled) {
    boost = enabled;
  }

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(m_leftEncoder.getVelocity(), m_rightEncoder.getVelocity());
  }
}