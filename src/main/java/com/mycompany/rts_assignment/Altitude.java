/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.rts_assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author devil
 */
public class Altitude implements Runnable{
    SystemPhase phase;
    List<Integer> altitudeList =new ArrayList<Integer>();
    
    int altitude = SystemPhase.idealAltitude;

    public Altitude(List<Integer> list, SystemPhase sp){
        this.altitudeList = list;
        this.phase = sp;
    }
    
    public void changeInAltitude() {
        altitude += phase.giveRandom();

        altitudeList.add(altitude);
    }

    @Override
    public void run() {
        changeInAltitude();
    }

}
