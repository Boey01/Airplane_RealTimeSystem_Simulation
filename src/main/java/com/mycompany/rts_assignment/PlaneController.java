/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import java.util.List;

/**
 *
 * @author Boey
 */
public class PlaneController implements Runnable{
    List<Integer> altitudeList;
    List<Integer> wingsCommand;
    
    int alt;
    int angleAdjust; //for wings
    int checkAlt;
    
    public PlaneController(List<Integer> list, List<Integer> wc){
        this.altitudeList = list;
        this.wingsCommand = wc;
    }   

    @Override
    public void run() {
       alt = altitudeList.remove(0);
       adjustAltitude(alt);
       System.out.println("Current altitude: " + alt);
       
    }
    
    public void adjustAltitude(int current_alt){
    checkAlt = current_alt - SystemPhase.idealAltitude;
    
    
    if (checkAlt > SystemPhase.idealAltitude * 0.1) { // higher than ideal altitude range
        angleAdjust = -25;
    } else if (checkAlt < -SystemPhase.idealAltitude * 0.1) { //lower than ideal altitude range
        angleAdjust = 25;
    } else { // still close enough to ideal altitude
        angleAdjust = 0;
    }

    
    wingsCommand.add(angleAdjust);
}
}
