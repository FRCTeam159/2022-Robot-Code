// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

import org.opencv.core.Mat;

public class SimBackCamera extends TargetDetector{
    MJpegReader video_source;
    GripBallTarget grip = new GripBallTarget();

    public SimBackCamera(){
        System.out.println("new SimBackCamera");
        video_source=new MJpegReader("http://localhost:9001/?action=stream");     
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
