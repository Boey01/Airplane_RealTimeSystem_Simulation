/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
 * @author Boey
 */
public class WingsFlap implements Runnable{
SystemPhase phase;
    int angle = 0;
List<Integer> wingsCommand;

    public WingsFlap(List<Integer> wc, SystemPhase sp) {
        this.wingsCommand = wc;
        this.phase = sp;
    }

    @Override
    public void run() {
         receiveWingsCommand();
         adjustWingsAngle();
    }
    
    public void receiveWingsCommand(){
        try {
            String queueName = "wingsAngle";
            
            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();
            
            chan.queueDeclare(queueName,false,false,false,null);
            
            //use the channel to consume/receive the message
            chan.basicConsume(queueName,(x,msg)->{
                String m = new String(msg.getBody(),"UTF-8");
                angle = Integer.parseInt(m); 
            }, x->{});
           
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(PlaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void adjustWingsAngle(){
        System.out.println("Wings' angle has been adjusted to: " + angle); 
        phase.changeOfAltitudeRules(angle);
    }
}
