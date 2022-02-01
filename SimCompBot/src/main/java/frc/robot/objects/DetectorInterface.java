// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

/** Add your docs here. */
public interface DetectorInterface {
    public static final int FRONT=0;
    public static final int BACK=1;
    public static final int TARGET=2;
    void enable();
    void disable();
    boolean isEnabled();

}
