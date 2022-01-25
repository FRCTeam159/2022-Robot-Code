// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

import edu.wpi.first.wpilibj.AnalogGyro;
import frc.robot.Robot;
import gazebo.SimGyro;

/** Add your docs here. */
public class Gyroscope implements GyroInterface{
    SimGyro sim_gyro;
    AnalogGyro real_gyro;
    boolean enabled=true;

    public Gyroscope(){
        if(Robot.isReal())
            real_gyro = new AnalogGyro(0);
        else
            sim_gyro=new SimGyro();
    }

    @Override
    public void enable() {
        enabled=true;
        if(!Robot.isReal())
            sim_gyro.enable();   
    }

    @Override
    public void disable() {
        enabled=false; 
        if(!Robot.isReal())
            sim_gyro.disable();
    }
    @Override
    public void reset() {
        if(Robot.isReal())
            real_gyro.reset();
        else
            sim_gyro.reset();
    }

    @Override
    public double getRate() {
        if(!enabled)
            return 0;
        else if(Robot.isReal())
            return real_gyro.getRate();
        else
            return sim_gyro.getRate();
    }

    @Override
    public double getHeading() {
        if(!enabled)
            return 0;
        else if(Robot.isReal())
            return real_gyro.getAngle();
        else
            return sim_gyro.getHeading();
    }
    public double getAbsHeading() {
        if(Robot.isReal())
            return real_gyro.getAngle();
        else
            return sim_gyro.getAbsHeading();
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    
}
