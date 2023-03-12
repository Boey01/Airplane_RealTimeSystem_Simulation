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
public class Plane {

   static SimulationAttributes simulation = new SimulationAttributes(); 
   static AltitudeSensor as = new AltitudeSensor(simulation);
   static PlaneController pc = new PlaneController(); 
   static WingsFlap wf = new WingsFlap(simulation);
   static GPSSensor gs = new GPSSensor(simulation);
   static Tail tail = new Tail(simulation);
   static PressureSensor ps = new PressureSensor();
   static SpeedSensor ss = new SpeedSensor(simulation);
   static Engine engine = new Engine(simulation);
   
    public static void main(String[] args) {
        as.addPressureObserver(ps);
        ss.addPressureObserver(ps);
     
        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
        
        es.scheduleAtFixedRate(as, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(pc, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(wf, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
         es.scheduleAtFixedRate(gs, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
         es.scheduleAtFixedRate(tail, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
         es.scheduleAtFixedRate(ss, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
         es.scheduleAtFixedRate(engine, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
        
    }
}
