/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sensors;

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
public class GPSSensor implements Runnable{
    SimulationAttributes simulation;
    
    int offAngle=0;

    public GPSSensor(SimulationAttributes sa){
        this.simulation = sa;
    }
    
     public void changeInOA() {
        offAngle += simulation.giveRandomOA();    
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
                        
            chan.basicPublish("", queueName, null, msg.getBytes());
            chan.close();
            con.close();
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(AltitudeSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
}
