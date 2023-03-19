/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import com.mycompany.rts_assignment.GUI;
import com.mycompany.rts_assignment.Plane;
import com.mycompany.rts_assignment.PlaneController;
import com.mycompany.rts_assignment.SimulationAttributes;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Boey
 */
public class WingsFlap implements Runnable{
    SimulationAttributes phase;
    int angle = 0;
    GUI gui;
    
    String exchangeName = "CommandExchange";
    String routingKey = "wings";  
    String queueName;
    ConnectionFactory cf;
    Connection con;
    Channel chan;
    
    public WingsFlap(SimulationAttributes sp,GUI gui) {
        try {
            this.phase = sp;
            this.gui = gui;
            this.cf = new ConnectionFactory();
            this.con = cf.newConnection();
            this.chan = con.createChannel();

            chan.exchangeDeclare(exchangeName, "direct");

            queueName = chan.queueDeclare().getQueue();
            chan.queueBind(queueName, exchangeName, routingKey);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        receiveWingsCommand();
        if(Plane.currentMode != Plane.Mode.closeLanding){
        adjustWingsAngle();
        }
        if(Plane.currentMode == Plane.Mode.Landed){
            try {
                con.close();
                chan.close();
            } catch (IOException | TimeoutException ex) {
                Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void receiveWingsCommand() {
        try {
            chan.basicConsume(queueName, (x, msg) -> {
                String m = new String(msg.getBody(), "UTF-8");
                angle = Integer.parseInt(m);
            }, x -> {
            });

            chan.queuePurge(queueName);
        } catch (IOException ex) {
            Logger.getLogger(PlaneController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void adjustWingsAngle() {
        String upDown = "(Upwards)";
        if (angle < 0) {
            upDown = "(Downwards)";
        }
        if(angle ==0) upDown="";

        //System.out.println("Wings' angle has been adjusted to: " + angle + upDown);
        gui.taAltitude.append("Wings' angle adjusted to: " + angle + upDown + "\n");
        gui.txtWA.setText(String.valueOf(angle));
        phase.changeOfAltitudeRules(angle);
    }
}
