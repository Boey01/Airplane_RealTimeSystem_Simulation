/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Boey
 */
public class SpeedSensor implements Runnable{
    SimulationAttributes simulation;
    PressureSensor pressureObserver;
    int speed;
    Random rand = new Random();
    
    public SpeedSensor(SimulationAttributes s) {
        this.simulation = s;
    }

    @Override
    public void run() {
        changeInSpeed();
        sendNewSpeed(simulation.planespeed);
        notifyPressureObserver();
        
    }
    
    public void changeInSpeed(){
        speed = rand.nextInt(20);
        if(rand.nextBoolean())speed = -speed;  
        
        simulation.planespeed += speed;
    }
    
    public void sendNewSpeed(int speed){
       String queueName = "speed";
       try {          
            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();
            
            //convert message
            String msg = Integer.toString(speed);
                       
            chan.queueDeclare(queueName,false,false,false,null);
            
            //publish the message to the exchange using the routing key
            chan.basicPublish("", queueName, null, msg.getBytes());
            chan.close();
            con.close();
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(AltitudeSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addPressureObserver(PressureSensor ps) {
        this.pressureObserver = ps;
    }
        
    public void notifyPressureObserver(){
        if(pressureObserver!=null){
            pressureObserver.speedChanged(speed); 
        }
    }
}
