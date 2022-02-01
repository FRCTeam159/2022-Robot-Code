// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

import org.opencv.core.Mat;

import edu.wpi.first.networktables.NetworkTable;
import utils.GripPipeline;

public class SimBackCamera extends TargetDetector{
    boolean connected=false;
    MJpegReader video_source;
    GripBallTarget grip = new GripBallTarget();
    public boolean show_hsv_threshold=true;

    public SimBackCamera(int type){
        super(type);
        System.out.println("new SimBackCamera "+type);
    }
    public void run(){
        m_timer.start();
        
        video_source=new MJpegReader("http://localhost:9001/?action=stream");
        
        connected=video_source.isConnected();
        
        if (!connected)
            disable();     
        else
            enable();
        
        Mat mat=new Mat();
        while (true && isEnabled()) {
            try {
              Thread.sleep(20);
            } catch (InterruptedException ex) {
              System.out.println("exception)");
            }
            mat=video_source.getMat();
            if(mat==null)
                continue;
            if(show_hsv_threshold){
                grip.process(mat);
                outputFrame(grip.hsvThresholdOutput);
            }
            else
                outputFrame(mat);
        }
    }
}