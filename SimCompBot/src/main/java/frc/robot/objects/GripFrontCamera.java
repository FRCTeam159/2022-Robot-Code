// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.objects;

import org.opencv.core.Mat;
import frc.robot.subsystems.Cameras;

public class GripFrontCamera extends GripDetector{
    GripTapeTarget grip = new GripTapeTarget();
    Camera camera;

    public GripFrontCamera(){
        System.out.println("new GripFrontCamera");
        camera=new Camera(Cameras.FRONT_CAMERA);
        Cameras.addCamera(camera);
        m_connected=camera.isConnected();
        setBestAttribute(BEST_SHAPE);
        setIdealRatio(2.5);
    }
    
    public Mat getFrame(){
        mat=camera.getFrame();
        if(mat !=null)
           process();     
        return mat;
    }
    
    void process() {
        grip.process(mat);
        if (show_hsv_threshold)
            mat = grip.hsvThresholdOutput();
        contours = grip.convexHullsOutput();
        getTargets();
        markTargets();
        setTarget();
    }
}
