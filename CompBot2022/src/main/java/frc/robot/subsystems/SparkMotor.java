/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;

/**
 * Add your docs here.
 */
class SparkMotor extends CANSparkMax implements MotorInterface {
    private RelativeEncoder encoder;
    private double zeroValue = 0;
    private double distancePerRotation = 1;

    SparkMotor(int id) {
        super(id, CANSparkMaxLowLevel.MotorType.kBrushless);
        encoder = getEncoder();
        zeroValue = encoder.getPosition();
    }

    public double getRotations() {
        double position = encoder.getPosition();
        return (position - zeroValue);
    }

    public void reset() {
        zeroValue = encoder.getPosition();
    }

    @Override
    public double getRate() {

        return getEncoder().getVelocity();
    }

    @Override
    public void setDistancePerRotation(double d) {
        distancePerRotation = d;

    }

    @Override
    public double getDistance() {
        return getRotations() * distancePerRotation;
    }
}
