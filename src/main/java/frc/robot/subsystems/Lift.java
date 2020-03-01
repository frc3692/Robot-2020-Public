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
import frc.robot.Constants.LiftConstants;
import frc.robot.util.RobotState;
import frc.robot.util.RobotState.State;
import frc.robot.util.pneumatics.DoubleWrapper;
import frc.robot.util.pneumatics.SolState;
import frc.robot.util.pneumatics.SolWrapper;

public class Lift extends SubsystemBase {
  private SolWrapper
      stage1 = new DoubleWrapper(LiftConstants.kStage1F, LiftConstants.kStage1R),
      stage2 = new DoubleWrapper(LiftConstants.kStage2F, LiftConstants.kStage2R);
  private CANSparkMax m_winch = new CANSparkMax(LiftConstants.kWinch, MotorType.kBrushless);

  /**
   * Creates a new Lift.
   */
  public Lift() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (RobotState.getState() == State.kEndgame) {
      stage1.fwd();
      stage2.set(stage2.getState());
    } else {
      stage1.rev();
      stage2.rev();
    }
  }

  public void initEndgame(Arm arm) {
    RobotState.updateState(State.kEndgame);
    arm.forceDown(true);
    stage1.fwd();
  }

  public void undoEndgame(Arm arm) {
    RobotState.updateState(State.kTelop);
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

  public SolState getStage1State() {
    return stage1.getState();
  }

  public SolState getStage2State() {
    return stage2.getState();
  }
}
