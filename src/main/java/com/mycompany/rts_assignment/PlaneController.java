/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import Observers.Observer;
import Sensories.SensoryData;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Boey
 */
public class PlaneController implements Runnable {

    int alt;
    int angleAdjust; //for wings
    int checkAlt;
    int idealAltitude = SimulationAttributes.initialAltitude;
    int wheelsTrigger = (int)(idealAltitude*0.2);
    int offAngle = 0;
    int newSpeed = 0;
    int idealSpeed = 850; //km/h
    int cabinPressure = 0;
    GUI gui;
    ArrayList<SensoryData> commandList = new ArrayList<>();
    Observer cabinMask, wheelGear;
    Phaser ph;

    public PlaneController(GUI gui, Phaser p) {
        this.gui = gui;
        this.ph = p;
        ph.register();
    }

    @Override
    public void run() {
        this.receiveValues();

        //Cruusing/close ground Landing mode ~~~~~~~~~~~~~~~~~~~~~~~
        if (Plane.currentMode != Plane.Mode.Landing) {
            if(alt < 0){alt =0;}
            if(newSpeed < 0){newSpeed =0;}
            processSensorData();            
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //start glidiing down for landing -------------------
        if (Plane.currentMode == Plane.Mode.Landing) {
            if (alt > (idealAltitude * 0.2) || newSpeed > (idealSpeed * 0.1)) {
                glidingDown();
                adjustDirection(offAngle);
            } else {
                ph.arrive();
                
                idealAltitude =(int)(idealAltitude * 0.2);
                idealSpeed =(int)(idealSpeed * 0.15);
            }
        }
        //----------------------------------------------
        
        if (alt == 0 && newSpeed == 0){
            ph.arriveAndDeregister();
        }
        if (alt < wheelsTrigger) {
            wheelGear.updateObserver();
        }
        displayInformation();
        sendCommand();
    }

    public void processSensorData() {
        //altitude -------------------------
        adjustAltitude(alt);

        //directions/GPS -----------------------------
        adjustDirection(offAngle);

        //speed -----------------------------------      
        adjustSpeed();

        // Pressure----------------------------------------
        checkPressure();
    }

    public void adjustAltitude(int current_alt) {
        checkAlt = current_alt - idealAltitude;
        int idealGap = (int) (idealAltitude * 0.1);

        if (checkAlt > idealGap || checkAlt < -idealGap) {//if outside of ideal range
            if (Math.abs(checkAlt) > idealGap * 2) {
                angleAdjust = 45;
            } else {
                angleAdjust = 25; //if it is really out of track
            }
            if (checkAlt > 0) {
                angleAdjust = -angleAdjust; // if below ideal altitude, e.g: 900-1000=-100 lower, adjust wing down to glide downwards
            }
        } else {
            angleAdjust = 0;
        }

        commandList.add(new SensoryData(angleAdjust, "wings"));
    }

    public void adjustDirection(int offAngle) {
        if (offAngle != 0) {
            if (offAngle < 0) {
                //assuume negative off angle = left
                angleAdjust = 20;
            } else {
                angleAdjust = -20;
            }

        } else {
            angleAdjust = 0;
        }
        commandList.add(new SensoryData(angleAdjust, "tail"));
    }

    public void adjustSpeed() {
        int speedInstruc = 0; // 0 do nothing, 1 slow down, 2 = speed up
        if (newSpeed > (idealSpeed * 1.1)) {
            speedInstruc = 1;//above ideal speed
        }
        if (newSpeed < (idealSpeed * 1.1)) {
            speedInstruc = 2;
        }

        commandList.add(new SensoryData(speedInstruc, "engine"));
    }

    public void checkPressure() {
        if (cabinPressure >= 100) {
            gui.taAlerts.append("!!!Cabin pressure is at abnormal level: " + cabinPressure + "!!!\n");
            cabinMask.updateObserver();
        }
    }

    public void displayInformation() { //to panel
        //altitude 
        //System.out.println("Current altitude: " + alt);
        gui.taAltitude.append("Current altitude: " + alt + "\n");
        gui.txtAlt.setText(String.valueOf(alt));

        //GPS
        // System.out.println("Off angle occurs: " + offAngle + " degree away from track.");
        gui.taGPS.append("Off angle occurs: " + offAngle + " degree away from track." + "\n");
        gui.txtOA.setText(String.valueOf(offAngle));

        //speed
        //System.out.println("Current plane speed:" + newSpeed);
        gui.txtSpeed.setText(String.valueOf(newSpeed));

        //pressure
        gui.txtPressure.setText(String.valueOf(cabinPressure));
    }

    public void receiveValues() {
        try {
            String queueName1 = "altitude";
            String queueName2 = "gps";
            String queueName3 = "speed";
            String queueName4 = "pressure";

            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();

            chan.queueDeclare(queueName1, false, false, false, null);
            chan.queueDeclare(queueName2, false, false, false, null);
            chan.queueDeclare(queueName3, false, false, false, null);
            chan.queueDeclare(queueName4, false, false, false, null);

            //altitude
            chan.basicConsume(queueName1, (x, msg) -> {
                String m = new String(msg.getBody(), "UTF-8");
                alt = Integer.parseInt(m);
            }, x -> {
            });

            //gps
            chan.basicConsume(queueName2, (x, msg) -> {
                String m = new String(msg.getBody(), "UTF-8");
                offAngle = Integer.parseInt(m);
            }, x -> {
            });

            //speed
            chan.basicConsume(queueName3, (x, msg) -> {
                String m = new String(msg.getBody(), "UTF-8");
                newSpeed = Integer.parseInt(m);
            }, x -> {
            });

            //pressure
            chan.basicConsume(queueName4, (x, msg) -> {
                String m = new String(msg.getBody(), "UTF-8");
                cabinPressure = Integer.parseInt(m);
            }, x -> {
            });

        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(PlaneController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sendCommand() {
        commandList.parallelStream().forEach(element -> {
            processCommand(element);
        });
        commandList.clear();
    }

    public static void processCommand(SensoryData i) {
        String ex = "CommandExchange";
        ConnectionFactory f = new ConnectionFactory();

        try (Connection con = f.newConnection()) {
            Channel ch = con.createChannel();
            ch.exchangeDeclare(ex, "direct");

            ch.basicPublish(ex, i.routingKey, null, String.valueOf(i.data).getBytes());
        } catch (IOException | TimeoutException ex1) {
            Logger.getLogger(PlaneController.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    public void addObserver(Observer o, int type) {
        switch (type) {
            case 1:
                this.cabinMask = o;
                break;
            case 2:
                this.wheelGear = o;
                break;
        }
    }

    public void glidingDown() {
        if (alt > 200) {
            angleAdjust = -25;
        } else {
            angleAdjust = 0;
        }

        int engineInstr = 2;
        if (newSpeed > 80) {
            engineInstr = 3;
        }
        commandList.add(new SensoryData(angleAdjust, "wings"));
        commandList.add(new SensoryData(engineInstr, "engine"));
    }
}
