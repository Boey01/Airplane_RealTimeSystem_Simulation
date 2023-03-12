/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author devil
 */
public class Main {
   static SystemPhase phase = new SystemPhase();   
   
    public static void main(String[] args) {
        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
        
        es.scheduleAtFixedRate(new AltitudeSensor(phase), 0, SystemPhase.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(new PlaneController(), 0, SystemPhase.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(new WingsFlap(phase), 0, SystemPhase.getSpeed(), TimeUnit.MILLISECONDS);
         es.scheduleAtFixedRate(new GPSSensor(phase), 0, SystemPhase.getSpeed(), TimeUnit.MILLISECONDS);
         es.scheduleAtFixedRate(new Tail(phase), 0, SystemPhase.getSpeed(), TimeUnit.MILLISECONDS);
        
    }
}
