/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import com.mycompany.rts_assignment.Data;
import com.mycompany.rts_assignment.*;
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
public class PressureSensor implements Observer{
    int pressure,altitude,speed;
    int temperature = 25;
    GUI gui;
    Data sd;

    public PressureSensor(GUI gui) {
        this.gui = gui;
    }
    
    @Override
    public void updateObserver() {

    }

     @Override
    public void updateObserver(Data data) {
        sd = data;
        if(sd.routingKey == "altitude"){
            altitudeChanged(sd.data);
        }
        
        if(sd.routingKey == "speed"){
            speedChanged(sd.data);
        }       
        pressure = (altitude+speed)/temperature;
        //System.out.println("Cabin Pressure: "+pressure);
        
        sendPressureValue(pressure);
    }
    
    public void altitudeChanged(int alt) {
      this.altitude = alt;
    }

    public void speedChanged(int speed) {
        this.speed = speed;
    }
    
    public void sendPressureValue(int pressure){
        try {
            String queueName = "pressure";
            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();
            
            //convert message
            String msg = Integer.toString(pressure);
                       
            chan.queueDeclare(queueName,false,false,false,null);
                        
            chan.basicPublish("", queueName, null, msg.getBytes());
            chan.close();
            con.close();

        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(PressureSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
