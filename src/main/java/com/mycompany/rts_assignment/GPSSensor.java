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
public class GPSSensor implements Runnable{
    SystemPhase phase;
    
    int offAngle=0;

    public GPSSensor(SystemPhase sp){
        this.phase = sp;
    }
    
     public void changeInOA() {
        offAngle += phase.giveRandomOA();    
    }

    @Override
    public void run() {
        changeInOA();
        sendGPSValue(offAngle);
    }

    public void sendGPSValue(int offAngle){
       String queueName = "gps";
       try {          
            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();
            
            //convert message
            String msg = Integer.toString(offAngle);
                       
            chan.queueDeclare(queueName,false,false,false,null);
            
            //publish the message to the exchange using the routing key
            chan.basicPublish("", queueName, null, msg.getBytes());
            chan.close();
            con.close();
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(AltitudeSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
}
