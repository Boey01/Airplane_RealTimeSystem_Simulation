/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import Actuators.Tail;
import Actuators.WingsFlap;
import Actuators.Engine;
import Observers.CabinMask;
import Sensories.GPSSensor;
import Sensories.AltitudeSensor;
import Sensories.SpeedSensor;
import Observers.PressureSensor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author devil
 */
public class Plane {
   static GUI gui = new GUI();
   static ExecutorService ex = Executors.newCachedThreadPool();
   static Future<Object>cruisingMode = ex.submit(new logic(gui));
   
   static SimulationAttributes simulation = new SimulationAttributes(); 
   static AltitudeSensor as = new AltitudeSensor(simulation);
   static PlaneController pc = new PlaneController(gui,cruisingMode); 
   static WingsFlap wf = new WingsFlap(simulation,gui);
   static GPSSensor gs = new GPSSensor(simulation);
   static Tail tail = new Tail(simulation,gui);
   static PressureSensor ps = new PressureSensor(gui);
   static SpeedSensor ss = new SpeedSensor(simulation);
   static Engine engine = new Engine(simulation);
   static CabinMask cm = new CabinMask(gui);
        
    public static void main(String[] args) {
        as.addPressureObserver(ps);
        ss.addPressureObserver(ps);
        pc.addObserver(cm, true);
        gui.setVisible(true);
        gui.importAltitudeSensor(as); 
        
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

   class logic implements Callable<Object>{
    private GUI gui; 
    enum Mode { Cruising, Landing };
    Mode currentMode = Mode.Cruising;
    
    public logic(GUI gui) {
        this.gui = gui;
    }
    
    public Object call() throws Exception {
    while (currentMode == Mode.Cruising) {
        gui.btnLanding.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentMode = Mode.Landing;
            }
        });
    }
    return null;
}
}
