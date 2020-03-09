/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LiftConstants;
import frc.robot.util.RobotState;
import frc.robot.util.RobotState.State;
import frc.robot.util.pneumatics.DoubleWrapper;
import frc.robot.util.pneumatics.SolState;
import frc.robot.util.pneumatics.SolWrapper;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

public class Lift extends SubsystemBase implements Loggable {
  private SolWrapper
      stage1 = new DoubleWrapper(LiftConstants.kStage1F, LiftConstants.kStage1R),
      stage2 = new DoubleWrapper(LiftConstants.kStage2F, LiftConstants.kStage2R);
  private CANSparkMax m_winch = new CANSparkMax(LiftConstants.kWinch, MotorType.kBrushless);
  private CANEncoder m_winchEnc = m_winch.getEncoder();

  @Log
  private Double winchPos = m_winchEnc.getPosition();
  /**
   * Creates a new Lift.
   */
  public Lift() {
    m_winch.restoreFactoryDefaults();
    m_winch.clearFaults();
    m_winch.setMotorType(MotorType.kBrushless);
    m_winch.setOpenLoopRampRate(0.25);
    m_winch.setIdleMode(IdleMode.kCoast);

    stage1.rev();
    stage2.rev();

    m_winch.burnFlash();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (RobotState.getState() != State.kEndgame) {
      stage1.rev();
      stage2.rev();
    }

    winchPos = m_winchEnc.getPosition();
  }

  public void initEndgame(Arm arm) {
    RobotState.updateState(State.kEndgame);
    stage1.fwd();
    arm.forceDown(true);
  }

  public void undoEndgame(Arm arm) {
    RobotState.updateState(State.kTelop);
    arm.forceDown(false);
  }

  public void runWinch(double speed) {
    m_winch.set(speed * LiftConstants.kWinchMaxSpeed);
  }

  public void runWinchUntilOut() {
    if(m_winchEnc.getPosition() > 245) {
      runWinch(-.5);
    } else {
      runWinch(0);
    }
  }

  public void rewindWinch() {
      runWinch(.25);
    }

  public boolean runToPosition(double target, double speed) {
    if(speed > 0) {
      if(m_winchEnc.getPosition() < target) {
        runWinch(speed);
        return false;
      } else {
        return true;
      }
    } else {
      if(m_winchEnc.getPosition() > target) {
        m_winch.set(speed);
        return false;
      } else {
        return true;
      }
    }
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