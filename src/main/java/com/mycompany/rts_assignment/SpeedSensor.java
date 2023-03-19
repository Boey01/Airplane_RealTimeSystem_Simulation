/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import com.mycompany.rts_assignment.Data;
import com.mycompany.rts_assignment.Plane;
import com.mycompany.rts_assignment.SimulationAttributes;
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
    Observer pressureObserver;
    int speed;
    Random rand = new Random();
    
    public SpeedSensor(SimulationAttributes s) {
        this.simulation = s;
    }

    @Override
    public void run() {
        if(Plane.currentMode != Plane.Mode.closeLanding) {
        changeInSpeed();
        }
        else{
            simulation.planespeed -= 10;
        }
        
        sendNewSpeed(simulation.planespeed);
        notifyPressureObserver();
        
    }
    
    public void changeInSpeed(){
        speed = rand.nextInt(10);
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
            
            
            chan.basicPublish("", queueName, null, msg.getBytes());
            chan.close();
            con.close();
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(AltitudeSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addPressureObserver(Observer ps) {
        this.pressureObserver = ps;
    }
        
    public void notifyPressureObserver(){
        if(pressureObserver!=null){
            pressureObserver.updateObserver(new Data(simulation.planespeed,"speed"));
        }
    }
}
