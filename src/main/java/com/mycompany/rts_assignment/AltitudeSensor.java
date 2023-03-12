/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.rts_assignment;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devil
 */
public class AltitudeSensor implements Runnable{
    SystemPhase phase;
    
    int altitude = SystemPhase.idealAltitude;

    public AltitudeSensor(SystemPhase sp){
        this.phase = sp;
    }
    
    public void changeInAltitude() {
        altitude += phase.giveRandom();    
    }

    @Override
    public void run() {
        changeInAltitude();
        sendAltitudeValue(altitude);
    }

    public void sendAltitudeValue(int alt){
       String queueName = "altitude";
       try {          
            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();
            
            //convert message
            String msg = Integer.toString(alt);
                       
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
