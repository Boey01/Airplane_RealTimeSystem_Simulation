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
public class PlaneController implements Runnable{  
    int alt;
    int angleAdjust; //for wings
    int checkAlt;
    int offAngle =0;
    

    @Override
    public void run() {
       this.receiveValues();
       adjustAltitude(alt);
       System.out.println("Current altitude: " + alt);
       
       if (offAngle != 0){
           adjustDirection(offAngle);
           System.out.println("Off angle occurs: " + offAngle + " degree away from track.");
       }
    }
    
    public void adjustAltitude(int current_alt){
        checkAlt = current_alt - SystemPhase.idealAltitude;
        int idealGap = (int) (SystemPhase.idealAltitude * 0.1);
        
        if (checkAlt > idealGap || checkAlt < -idealGap) {//if outside of ideal range
            if(Math.abs(checkAlt) > idealGap*2){
                angleAdjust = 45;
            } else angleAdjust= 25; //if it is really out of track
            if(checkAlt > 0) angleAdjust = -angleAdjust; // if below ideal altitude, e.g: 900-1000=-100 lower, adjust wing down to glide downwards

        } else {
            angleAdjust = 0;
        }
   
        sendWingsCommand(angleAdjust);
}
    
    public void adjustDirection(int offAngle){
        if (offAngle<0){
        //assuume negative off angle = left
        angleAdjust = 20; 
    }else angleAdjust =-20;
        
        sendTailCommand(angleAdjust);
    }
        
    public void receiveValues(){
        try {
            String queueName1 = "altitude";
            String queueName2 = "gps";
            
            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();
            
            chan.queueDeclare(queueName1,false,false,false,null);
            chan.queueDeclare(queueName2,false,false,false,null);
            
            //altitude
            chan.basicConsume(queueName1,(x,msg)->{
                String m = new String(msg.getBody(),"UTF-8");
                alt = Integer.parseInt(m);
            }, x->{});
            
            //gps
            chan.basicConsume(queueName2,(x,msg)->{
                String m = new String(msg.getBody(),"UTF-8");
                offAngle = Integer.parseInt(m);
            }, x->{});
           
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(PlaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void sendWingsCommand(int angle){
      String queueName = "wingsAngle";
       try {          
            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();
            
            //convert message
            String msg = Integer.toString(angle);

            chan.queueDeclare(queueName,false,false,false,null);
            
            
            chan.basicPublish("", queueName, null, msg.getBytes());
            chan.close();
            con.close();
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(AltitudeSensor.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void sendTailCommand(int angle){
      String queueName = "tailAngle";
       try {          
            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();
            
            //convert message
            String msg = Integer.toString(angle);
            
            chan.queueDeclare(queueName,false,false,false,null);
            
            
            chan.basicPublish("", queueName, null, msg.getBytes());
            chan.close();
            con.close();
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(AltitudeSensor.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
}
