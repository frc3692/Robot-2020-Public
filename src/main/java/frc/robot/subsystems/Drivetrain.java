/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.misc.SB;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;

@SuppressWarnings("unused")
public class Drivetrain extends SubsystemBase {
  /**
   * Creates a new Drivetrain.
   */
  private SpeedController lf = new WPI_TalonSRX(DriveConstants.m_LeftFront); // new CANSparkMax(DriveConstants.m_LeftFront, MotorType.kBrushless);
  private SpeedController lb = new WPI_TalonSRX(DriveConstants.m_LeftBack); //new CANSparkMax(DriveConstants.m_LeftBack, MotorType.kBrushless);
  private SpeedController rf = new WPI_TalonSRX(DriveConstants.m_RightFront); //new CANSparkMax(DriveConstants.m_RightFront, MotorType.kBrushless);
  private SpeedController rb = new WPI_TalonSRX(DriveConstants.m_RightBack); //new CANSparkMax(DriveConstants.m_RightBack, MotorType.kBrushless);
  private SpeedControllerGroup left = new SpeedControllerGroup(lf, lb);
  private SpeedControllerGroup right = new SpeedControllerGroup(rf, rb);
  private DifferentialDrive drive = new DifferentialDrive(left, right);

  private boolean slow = false, boost = false;
  private double mult = RobotContainer.Config.NormalSpd;

  public Drivetrain() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    RobotContainer.Config.SlowSpd = SB.Drive.Slow.getDouble(0.5);
    RobotContainer.Config.NormalSpd = SB.Drive.Normal.getDouble(0.75);
    RobotContainer.Config.BoostSpd = SB.Drive.Boost.getDouble(1);
  }

  public void drive(double xSpeed, double zRotation) {
    left.setInverted(RobotContainer.Config.Inverted);
    right.setInverted(RobotContainer.Config.Inverted);
    if(!slow && !boost)
      mult = RobotContainer.Config.NormalSpd;

    drive.arcadeDrive(xSpeed * mult, zRotation * mult);
  }

  public void slow(boolean enabled) {
    slow = enabled;
    if(slow)
      mult = RobotContainer.Config.SlowSpd;
    else if(boost)
      mult = RobotContainer.Config.BoostSpd;
  }

  public void boost(boolean enabled) {
    boost = enabled;
    if(boost && !slow)
      mult = RobotContainer.Config.BoostSpd;
  }

}
