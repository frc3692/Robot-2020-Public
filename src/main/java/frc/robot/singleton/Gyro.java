/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.singleton;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

/**
 * Add your docs here.
 */
public class Gyro {
    private final static Gyro gyro = new Gyro();
    private final AHRS m_ahrs = new AHRS(SPI.Port.kMXP);

    public static Gyro getInstance() {
        return gyro;
    }

    private Gyro() {
    }

    public double getHeading() {
        return m_ahrs.getYaw();
    }

    public void reset() {
        m_ahrs.zeroYaw();
    }

    public double getTurnRate() {
        return m_ahrs.getRate();
    }

    public float getAccel() {
        return m_ahrs.getWorldLinearAccelX();
    }
}