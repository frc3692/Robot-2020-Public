/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.LiftConstants;
import frc.robot.RobotContainer.RobotState;
import frc.robot.util.pneumatics.DoubleWrapper;
import frc.robot.util.pneumatics.SingleWrapper;
import frc.robot.util.pneumatics.SolState;
import frc.robot.util.pneumatics.SolWrapper;

public class Lift extends SubsystemBase {
  private SolWrapper m_liftEngage = new SingleWrapper(LiftConstants.kEngageF),
      stage1 = new DoubleWrapper(LiftConstants.kStage1F, LiftConstants.kStage1R),
      stage2 = new DoubleWrapper(LiftConstants.kStage2F, LiftConstants.kStage2R),
      stage3 = new DoubleWrapper(LiftConstants.kStage3F, LiftConstants.kStage3R);
  private CANSparkMax m_winch = new CANSparkMax(LiftConstants.kWinch, MotorType.kBrushless);

  /**
   * Creates a new Climber.
   */
  public Lift() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (RobotContainer.State == RobotState.kEndgame) {
      m_liftEngage.fwd();
      stage1.set(stage1.getState());
      stage2.set(stage2.getState());
      stage3.set(stage3.getState());
    } else {
      m_liftEngage.rev();
      stage1.rev();
      stage2.rev();
      stage3.rev();
    }
  }

  public void initEndgame(Arm arm) {
    RobotContainer.State = RobotState.kEndgame;
    arm.forceDown(true);
    stage1.fwd();
  }

  public void undoEndgame(Arm arm) {
    RobotContainer.State = RobotState.kNormal;
    arm.forceDown(false);
  }

  public void runWinch(double speed) {
    m_winch.set(speed * LiftConstants.kWinchMaxSpeed);
  }

  public void engageStage1() {
    stage1.fwd();
  }

  public void disengageStage1() {
    stage1.rev();
  }

  public void engageStage2() {
    stage2.fwd();
  }

  public void disengageStage2() {
    stage2.rev();
  }

  public void engageStage3() {
    stage3.fwd();
  }

  public void disengageStage3() {
    stage3.rev();
  }

  public SolState getStage1State() {
    return stage1.getState();
  }

  public SolState getStage2State() {
    return stage2.getState();
  }

  public SolState getStage3State() {
    return stage3.getState();
  }

  public void engageAll() {
    stage1.fwd();
    stage2.fwd();
    stage3.fwd();
  }
}
