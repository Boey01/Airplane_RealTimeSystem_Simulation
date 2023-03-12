/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

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
    SystemPhase phase;
    int angle = 0;

    public Tail(SystemPhase sp) {
        this.phase = sp;
    }

    @Override
    public void run() {
        receiveTailCommand();
        adjustTailAngle();
    }

    public void receiveTailCommand() {
        try {
            String queueName = "tailAngle";

            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();

            chan.queueDeclare(queueName, false, false, false, null);

            //use the channel to consume/receive the message
            chan.basicConsume(queueName, (x, msg) -> {
                String m = new String(msg.getBody(), "UTF-8");
                angle = Integer.parseInt(m);
            }, x -> {
            });

        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(PlaneController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void adjustTailAngle(){ 

        String direction = "(Turn Right)";
        if (angle < 0) {
            direction = "(Turn Left)";
        }
        if(angle ==0) direction ="";

        System.out.println("Tail's angle has been adjusted to: " + angle + direction);
        phase.changeOfDirection(angle);
    
    }
}