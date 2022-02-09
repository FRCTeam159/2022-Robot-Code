/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.objects;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.robot.Robot;
import gazebo.SimEncMotor;

/**
 * Add your docs here.
 */
public class SparkMotor implements MotorInterface {
    // private RelativeEncoder encoder;
    private double zeroValue = 0;
    private double distancePerRotation = 1;
    private int inverted = 1;
    private SimEncMotor sim_motor;
    private CANSparkMax real_motor;

    // tweak to set simulation and real motor input to get similar behaviors
    private double sim_scale = 1; // velocity multiplier for simulation

    public SparkMotor(int id) {
        if (Robot.isReal()) {
            real_motor = new CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless);
            zeroValue = real_motor.getEncoder().getPosition();
        } else {
            sim_motor = new SimEncMotor(id);
            sim_motor.setScale(sim_scale);
        }
        // System.out.println("IsReal "+Robot.isReal());
    }

    public void setScale(double f){
        if(!Robot.isReal())
            sim_motor.setScale(f);
    }
    private double getRotations() {
        return real_motor.getEncoder().getPosition();
    }
    @Override
    public void reset() {
        if (Robot.isReal())
            zeroValue = real_motor.getEncoder().getPosition();
        else
            sim_motor.reset();
    }

    @Override
    public double getRate() {
        if (Robot.isReal())
            return distancePerRotation * inverted * real_motor.getEncoder().getVelocity() / 60;
        else
            return distancePerRotation * sim_motor.getRate();
    }

    @Override
    public void setDistancePerRotation(double d) {
        distancePerRotation = d;
        sim_motor.setDistancePerRotation(d);
    }

    @Override
    public double getDistance() {
        if (Robot.isReal())
            return (getRotations()-zeroValue) * distancePerRotation * inverted;
        else
            return sim_motor.getDistance();
    }
    
    @Override
    public void setInverted() {
        inverted = -1;
        sim_motor.setInverted();
    }
    @Override
    public void enable() {
        if (!Robot.isReal())
            sim_motor.enable();
    }

    @Override
    public void set(double v) {
        double r=v/distancePerRotation;
        if (Robot.isReal())
            real_motor.set(r);
        else
            sim_motor.set(r);
    }

    @Override
    public void disable() {
        if (Robot.isReal())
            real_motor.disable();
        else
            sim_motor.disable();
    }
}
