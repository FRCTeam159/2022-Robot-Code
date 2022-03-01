// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

import org.opencv.core.Mat;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/** Add your docs here. */
public class TargetDetector implements VideoInterface{
    
    protected static final NetworkTableInstance inst = NetworkTableInstance.getDefault();
    protected static final NetworkTable m_target_table=inst.getTable("TargetData");
   
    protected Mat mat;
    protected static TargetData target=new TargetData();
   
    protected boolean m_annotate=false;
    protected boolean m_connected=false;
    public static boolean show_hsv_threshold=false;

    private NetworkTableEntry ta;
    private NetworkTableEntry tx;
    private NetworkTableEntry ty;
    private NetworkTableEntry tv;
    private NetworkTableEntry tr;

    public int image_width=640;
    public int image_height=480;

    TargetDetector(){
        publish();
    }
    
    public void outputTargetInImage(){
        m_annotate=true;
    }
    public void outputRawImage(){
        m_annotate=false;
    }
    
    public void publish(){
        setTargetData();  
    }
    
    protected void setTargetData(){
        ta= m_target_table.getEntry("ta");
        ta.setDouble(target.ta);
        tx= m_target_table.getEntry("tx");
        tx.setDouble(target.tx);
        ty= m_target_table.getEntry("ty");
        ty.setDouble(target.ty);
        tv= m_target_table.getEntry("tv");
        tv.setBoolean(target.tv); 
        tr= m_target_table.getEntry("tr");
        tr.setDouble(target.tr);     
    }
    
    @Override
    public Mat getFrame(){
        return null;
    }
    
    @Override
    public boolean isConnected() {
        return m_connected;
    }
    @Override
    public void process() {
    }
}
