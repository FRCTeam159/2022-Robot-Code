// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

import org.opencv.core.Mat;
import frc.robot.subsystems.Cameras;

public class GripBallDetector extends GripDetector {
    GripBallPipeline grip = new GripBallPipeline();
    Camera camera;

    public GripBallDetector(){
        System.out.println("new GripBackCamera");
        camera=new Camera(Cameras.BACK_CAMERA);
        Cameras.addCamera(camera);
        m_connected=camera.isConnected();
        setBestAttribute(LARGEST);
        setIdealRatio(1.0);
        target_info.idealX=0;
        target_info.xScale=0.25;

        target_info.idealY=0;
        target_info.yScale=0.5;

        target_info.useArea=true;
        target_info.idealA=105;
        target_info.aTol=5;
        target_info.aScale=0.7;
    }
   
    public Mat getFrame(){
        mat=camera.getFrame();
        return mat;
    }
    public void process() {
        if(mat == null)
            return;
        grip.process(mat);
        if (show_hsv_threshold)
            mat = grip.hsvThresholdOutput();
        contours = grip.convexHullsOutput();
        getTargets();
        markTargets();
        setTarget(); 
    }  
}
