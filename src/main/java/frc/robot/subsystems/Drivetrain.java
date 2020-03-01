/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.singleton.Gyro;
import io.github.oblarg.oblog.annotations.Log;
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

  private final CANPIDController m_leftPID = m_frontLeft.getPIDController();
  private final CANPIDController m_rightPID = m_frontRight.getPIDController();

  private final DifferentialDrive m_drive = new DifferentialDrive(m_frontLeft, m_frontRight);

  private final CANEncoder m_leftEncoder = m_frontLeft.getEncoder();
  private final CANEncoder m_rightEncoder = m_frontRight.getEncoder();

  private final DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(
      Rotation2d.fromDegrees(Gyro.getInstance().getHeading()));

  private boolean m_slow = false, m_boost = false;

  private double m_leftSetpoint = 0, m_rightSetpoint = 0;

  public Drivetrain() {

    m_drive.setDeadband(0.1);
    // Set up encoders for wheels
    m_leftEncoder.setPositionConversionFactor(DriveConstants.kEncDPR); // m
    m_leftEncoder.setVelocityConversionFactor(DriveConstants.kEncDPR / 60); // m/s

    m_rightEncoder.setPositionConversionFactor(DriveConstants.kEncDPR); // m
    m_rightEncoder.setVelocityConversionFactor(DriveConstants.kEncDPR / 60); // m/s

    m_frontLeft.restoreFactoryDefaults();
    m_frontRight.restoreFactoryDefaults();
    m_backLeft.restoreFactoryDefaults();
    m_backRight.restoreFactoryDefaults();

    m_frontLeft.clearFaults();
    m_frontRight.clearFaults();
    m_backLeft.clearFaults();
    m_backRight.clearFaults();

    m_frontLeft.setMotorType(MotorType.kBrushless);
    m_frontRight.setMotorType(MotorType.kBrushless);
    m_backLeft.setMotorType(MotorType.kBrushless);
    m_backRight.setMotorType(MotorType.kBrushless);

    m_frontLeft.setOpenLoopRampRate(0.5);
    m_frontRight.setOpenLoopRampRate(0.5);
    m_backLeft.setOpenLoopRampRate(0.5);
    m_backRight.setOpenLoopRampRate(0.5);

    m_frontLeft.setIdleMode(IdleMode.kBrake);
    m_frontRight.setIdleMode(IdleMode.kBrake);
    m_backLeft.setIdleMode(IdleMode.kBrake);
    m_backRight.setIdleMode(IdleMode.kBrake);

    m_backLeft.follow(m_frontLeft);
    m_backRight.follow(m_frontRight);

    m_leftPID.setP(DriveConstants.kPDriveVel, 0);
    m_leftPID.setSmartMotionMaxAccel(DriveConstants.kMaxAccel, 0);
    m_leftPID.setSmartMotionMaxVelocity(DriveConstants.kMaxVel, 0);

    m_rightPID.setP(DriveConstants.kPDriveVel);
    m_rightPID.setSmartMotionMaxAccel(DriveConstants.kMaxAccel, 0);
    m_rightPID.setSmartMotionMaxVelocity(DriveConstants.kMaxVel, 0);

    m_frontLeft.burnFlash();
    m_frontRight.burnFlash();
    m_backLeft.burnFlash();
    m_backRight.burnFlash();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double mult = RobotContainer.Config.NormalSpd;

    if (m_slow) {
      mult = RobotContainer.Config.SlowSpd;
    } else if (m_boost) {
      mult = RobotContainer.Config.BoostSpd;
    }

    m_drive.setMaxOutput(mult);

    //m_odometry.update(Rotation2d.fromDegrees(Gyro.getInstance().getHeading()), m_leftEncoder.getPosition(),
    //    m_rightEncoder.getPosition());
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
    m_frontRight.setVoltage(-rVolts);
    m_drive.feed();
  }

  public void velocityDrive(double xSpeed, double zRotation) {
    velocityDrive(xSpeed, zRotation, true);
  }

  public void velocityDrive(double xSpeed, double zRotation, boolean squareInputs) {
    if (RobotContainer.Config.Inverted)
      xSpeed = -xSpeed;

    xSpeed = MathUtil.clamp(xSpeed, -1.0, 1.0);

    zRotation = MathUtil.clamp(zRotation, -1.0, 1.0);

    // Square the inputs (while preserving the sign) to increase fine control
    // while permitting full power.
    if (squareInputs) {
      xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
      zRotation = Math.copySign(zRotation * zRotation, zRotation);
    }

    double leftMotorOutput;
    double rightMotorOutput;

    double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

    if (xSpeed >= 0.0) {
      // First quadrant, else second quadrant
      if (zRotation >= 0.0) {
        leftMotorOutput = maxInput;
        rightMotorOutput = xSpeed - zRotation;
      } else {
        leftMotorOutput = xSpeed + zRotation;
        rightMotorOutput = maxInput;
      }
    } else {
      // Third quadrant, else fourth quadrant
      if (zRotation >= 0.0) {
        leftMotorOutput = xSpeed + zRotation;
        rightMotorOutput = maxInput;
      } else {
        leftMotorOutput = maxInput;
        rightMotorOutput = xSpeed - zRotation;
      }
    }

    double leftVel = leftMotorOutput * DriveConstants.kMaxVel;
    double rightVel = rightMotorOutput * DriveConstants.kMaxVel;

    m_leftPID.setReference(leftVel, ControlType.kSmartVelocity, 0, DriveConstants.kFeedForward.calculate(leftVel));
    m_rightPID.setReference(rightVel, ControlType.kSmartVelocity, 0, DriveConstants.kFeedForward.calculate(rightVel));

    m_drive.feed();
  }

  public void updatePosSetpoint(double dist) {
    m_leftSetpoint = dist + m_leftEncoder.getPosition();
    m_rightSetpoint = dist + m_rightEncoder.getPosition();
  }

  public void distDrive(double dist) {
    updatePosSetpoint(dist);
    m_leftPID.setReference(m_leftSetpoint, ControlType.kSmartMotion, 1,
        DriveConstants.kFeedForward.calculate(m_leftEncoder.getVelocity()));
    m_rightPID.setReference(m_rightSetpoint, ControlType.kSmartMotion, 1,
        DriveConstants.kFeedForward.calculate(m_rightEncoder.getVelocity()));
  }

  public boolean atPosSetpoint() {
    return (m_leftEncoder.getPosition() >= (m_leftSetpoint - m_leftPID.getSmartMotionAllowedClosedLoopError(1))
        && m_leftEncoder.getPosition() <= (m_leftSetpoint + m_leftPID.getSmartMotionAllowedClosedLoopError(1)))
        && (m_rightEncoder.getPosition() >= (m_rightSetpoint - m_rightPID.getSmartMotionAllowedClosedLoopError(1))
            && m_rightEncoder.getPosition() <= (m_rightSetpoint + m_rightPID.getSmartMotionAllowedClosedLoopError(1)));
  }

  public double getLeftDist() {
    return m_leftEncoder.getPosition();
  }

  public double getRightDist() {
    return m_rightEncoder.getPosition();
  }

  public double getLeftVel() {
    return m_leftEncoder.getVelocity();
  }

  public double getRightVel() {
    return m_rightEncoder.getVelocity();
  }

  public void slow(boolean enabled) {
    m_slow = enabled;
  }

  public void boost(boolean enabled) {
    m_boost = enabled;
  }

  public void stop() {
    m_drive.stopMotor();
  }

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(m_leftEncoder.getVelocity(), m_rightEncoder.getVelocity());
  }
}