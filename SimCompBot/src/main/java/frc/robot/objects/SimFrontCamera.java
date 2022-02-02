// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

import org.opencv.core.Mat;

public class SimFrontCamera extends TargetDetector{

    MJpegReader video_source;
    GripTapeTarget grip = new GripTapeTarget();

    public SimFrontCamera(){
        System.out.println("new SimFrontCamera");
        video_source=new MJpegReader("http://localhost:9000/?action=stream");
       
        m_connected=video_source.isConnected();
    }
    
    public Mat getFrame(){
        mat=video_source.getFrame();
        if(mat !=null && show_hsv_threshold){
            grip.process(mat);
            mat=grip.hsvThresholdOutput();
        }
        return mat;
    }
}
