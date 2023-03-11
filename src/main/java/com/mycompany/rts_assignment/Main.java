/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author devil
 */
public class Main {
   static List<Integer> altList =new ArrayList<Integer>();
   static List<Integer> wingsCommand =new ArrayList<Integer>();
   static SystemPhase phase = new SystemPhase();   
   
    public static void main(String[] args) {
        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
        
        es.scheduleAtFixedRate(new Altitude(altList,phase), 0, SystemPhase.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(new PlaneController(altList,wingsCommand), 0, SystemPhase.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(new WingsFlap(wingsCommand,phase), 0, SystemPhase.getSpeed(), TimeUnit.MILLISECONDS);
        //test
    }
}
