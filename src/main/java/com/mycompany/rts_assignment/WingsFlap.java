/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import com.rabbitmq.client.BuiltinExchangeType;
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
    SystemPhase phase;
    int angle = 0;

    public WingsFlap(SystemPhase sp) {
        this.phase = sp;
    }

    @Override
    public void run() {
        receiveWingsCommand();
        adjustWingsAngle();
    }

    public void receiveWingsCommand() {
        try {
            String exchangeName = "CommandExchange";
            String routingKey = "wings";

            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();

            chan.exchangeDeclare(exchangeName, "direct");

            String queueName = chan.queueDeclare().getQueue();
            chan.queueBind(queueName, exchangeName, routingKey);

            chan.basicConsume(queueName, (x, msg) -> {
                String m = new String(msg.getBody(), "UTF-8");
                angle = Integer.parseInt(m);
            }, x -> {
            });

        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(PlaneController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void adjustWingsAngle() {
        String upDown = "(Upwards)";
        if (angle < 0) {
            upDown = "(Downwards)";
        }
        if(angle ==0) upDown="";

        System.out.println("Wings' angle has been adjusted to: " + angle + upDown);
        phase.changeOfAltitudeRules(angle);
    }
}
