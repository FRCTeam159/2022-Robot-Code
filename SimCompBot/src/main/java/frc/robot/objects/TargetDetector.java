// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;

/** Add your docs here. */
public class TargetDetector extends Thread implements DetectorInterface{
    static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
 
    protected final Timer m_timer = new Timer();
    protected NetworkTable m_target_table;
    protected final NetworkTableInstance inst = NetworkTableInstance.getDefault();
    protected final CvSource outputStream;
    protected Mat mat;
   
    boolean m_enabled=false;
    boolean m_annotate=false;
    protected int m_type;
    public int image_width=640;
    public int image_height=480;
    
    TargetDetector(int type){
        super();
        m_target_table = inst.getTable("TargetData");
        m_type=type;
   
        if(type==FRONT)
            outputStream= CameraServer.putVideo("FrontCamera", image_width, image_height);
        else
            outputStream= CameraServer.putVideo("BackCamera", image_width, image_height);
    }
    public void enable(){ m_enabled=true;}
    public void disable(){ m_enabled=false;}
    public boolean isEnabled() { return m_enabled;}
    public void outputTargetInImage(){
        m_annotate=true;
    }
    public void outputRawImage(){
        m_annotate=false;
    }
    public TargetData getTarget(){
        return null;
    }
    public void outputFrame(Mat mat){
        // TODO add target box to image if annotate flag is set
        outputStream.putFrame(mat);
    }
    
}
