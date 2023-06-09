/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Observers.WheelGear;
import Observers.CabinMask;
import Actuators.Tail;
import Actuators.WingsFlap;
import Actuators.Engine;
import Sensors.AltitudeSensor;
import Sensors.SpeedSensor;
import Observers.PressureSensor;
import Sensors.GPSSensor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author devil
 */
public class Plane {
   static GUI gui = new GUI();
   static public enum Mode {Cruising, Landing, closeLanding,Landed};
   static public Mode currentMode = Mode.Cruising;
   static ExecutorService ex = Executors.newCachedThreadPool();
   static Future<Object>cruisingMode = ex.submit(new cruisingLogic(gui));
   static Phaser p = new Phaser();
   
   static SimulationAttributes simulation = new SimulationAttributes(); 
   static AltitudeSensor as = new AltitudeSensor(simulation);
   static PlaneController pc = new PlaneController(gui,p); 
   static WingsFlap wf = new WingsFlap(simulation,gui);
   static GPSSensor gs = new GPSSensor(simulation);
   static Tail tail = new Tail(simulation,gui);
   static PressureSensor ps = new PressureSensor(gui);
   static SpeedSensor ss = new SpeedSensor(simulation);
   static Engine engine = new Engine(simulation);
   static CabinMask cm = new CabinMask(gui);
   static WheelGear wg = new WheelGear(gui);
        
    public static void main(String[] args) throws InterruptedException {
        as.addPressureObserver(ps);
        ss.addPressureObserver(ps);
        pc.addObserver(cm, 1);
        pc.addObserver(wg, 2);
        
        gui.setVisible(true);
        gui.importAltitudeSensor(as); 
        
        ScheduledExecutorService es = Executors.newScheduledThreadPool(7);
        p.register();
              
        es.scheduleAtFixedRate(as, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(gs, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(ss, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);        
        es.scheduleAtFixedRate(pc, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(wf, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(tail, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
        es.scheduleAtFixedRate(engine, 0, SimulationAttributes.getSpeed(), TimeUnit.MILLISECONDS);
        
             
         while(!cruisingMode.isDone()){          
        } 
        gui.taAlerts.append("Airplane is gliding down for landing.\n");
        p.arriveAndAwaitAdvance(); 
        currentMode = Mode.closeLanding; 
        gui.taAlerts.append("Airplane is proceeding to final landing procedures.\n");
        
        p.arriveAndAwaitAdvance(); 
        currentMode = Mode.Landed;
         
        gui.taAlerts.append("Airplane is now landed.\n");
        
        Thread.sleep(1000);
        es.shutdown();
    }
    
    
}

   class cruisingLogic implements Callable<Object>{
    private GUI gui; 
    
    public cruisingLogic(GUI gui) {
        this.gui = gui;
    }
    
    public Object call() throws Exception {
    while (Plane.currentMode == Plane.Mode.Cruising) {
        Thread.sleep(100);
        gui.btnLanding.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Plane.currentMode = Plane.Mode.Landing;            
            }
        });
    }
    return null;
}
}
