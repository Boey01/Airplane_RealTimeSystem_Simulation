/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import com.mycompany.rts_assignment.GUI;
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
public class Tail implements Runnable{
    SimulationAttributes phase;
    int angle = 0;
    GUI gui;

    String exchangeName = "CommandExchange";
    String routingKey = "tail";  
    String queueName;
    ConnectionFactory cf;
    Connection con;
    Channel chan;
    
    public Tail(SimulationAttributes sp, GUI gui) {
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
        receiveTailCommand();
        adjustTailAngle();
        if(Plane.currentMode == Plane.Mode.Landed){
            try {
                con.close();
                chan.close();
            } catch (IOException | TimeoutException ex) {
                Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void receiveTailCommand() {
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

    public void adjustTailAngle(){ 
        String direction = "(Turn Right)";
        if (angle < 0) {
            direction = "(Turn Left)";
        }
        if(angle ==0) direction ="";

       // System.out.println("Tail's angle has been adjusted to: " + angle + direction);
        gui.taGPS.append("Tail's angle adjusted to: " + angle + direction+"\n");
        gui.txtTA.setText(String.valueOf(angle));
        phase.changeOfDirection(angle);
    
    }
}
