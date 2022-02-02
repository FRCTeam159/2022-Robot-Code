// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

import org.opencv.core.Mat;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;

/** Add your docs here. */
public class TargetDetector implements VideoInterface{
  
 
    protected final Timer m_timer = new Timer();
    protected NetworkTable m_target_table;
    protected final NetworkTableInstance inst = NetworkTableInstance.getDefault();
   
    protected Mat mat;
   
    protected boolean m_annotate=false;
    protected boolean m_connected=false;
    public static boolean show_hsv_threshold=false;
    
    public int image_width=640;
    public int image_height=480;
    
    TargetDetector(){
        m_target_table = inst.getTable("TargetData");
    }
    
    public void outputTargetInImage(){
        m_annotate=true;
    }
    public void outputRawImage(){
        m_annotate=false;
    }
    
    @Override
    public Mat getFrame(){
        return null;
    }
    @Override
    public boolean isConnected() {
        return m_connected;
    }
}
