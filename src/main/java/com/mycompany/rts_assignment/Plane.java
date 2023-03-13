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
   static GUI gui = new GUI();
   static SimulationAttributes simulation = new SimulationAttributes(); 
   static AltitudeSensor as = new AltitudeSensor(simulation);
   static PlaneController pc = new PlaneController(gui); 
   static WingsFlap wf = new WingsFlap(simulation,gui);
   static GPSSensor gs = new GPSSensor(simulation);
   static Tail tail = new Tail(simulation,gui);
   static PressureSensor ps = new PressureSensor(gui);
   static SpeedSensor ss = new SpeedSensor(simulation);
   static Engine engine = new Engine(simulation);
   
    public static void main(String[] args) {
        as.addPressureObserver(ps);
        ss.addPressureObserver(ps);
        gui.setVisible(true);
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
