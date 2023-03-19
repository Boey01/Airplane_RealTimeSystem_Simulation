/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Actuators;

import com.mycompany.rts_assignment.Plane;
import com.mycompany.rts_assignment.Plane;
import com.mycompany.rts_assignment.PlaneController;
import com.mycompany.rts_assignment.PlaneController;
import com.mycompany.rts_assignment.SimulationAttributes;
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
public class Engine implements Runnable {
    SimulationAttributes simulation;
    Random rand = new Random();
    int engineInstruction;

    String exchangeName = "CommandExchange";
    String routingKey = "engine";  
    String queueName;
    ConnectionFactory cf;
    Connection con;
    Channel chan;
    
    public Engine(SimulationAttributes s) {
        try {
            this.simulation = s;
            this.cf = new ConnectionFactory();
            this.con = cf.newConnection();
            this.chan = con.createChannel();

            chan.exchangeDeclare(exchangeName, "direct");

            queueName = chan.queueDeclare().getQueue();
            chan.queueBind(queueName, exchangeName, routingKey);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        receiveEngineCommand();
        
        if(Plane.currentMode != Plane.Mode.closeLanding){
        if (engineInstruction ==1){
            simulation.planespeed -= rand.nextInt(20);
        }
        if (engineInstruction ==2){
            simulation.planespeed += rand.nextInt(20);
        }     
        if (engineInstruction ==3){
            simulation.planespeed -= rand.nextInt(50);
        }
        }
        
        if(Plane.currentMode == Plane.Mode.Landed){
            try {
                con.close();
                chan.close();
            } catch (IOException | TimeoutException ex) {
                Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
        public void receiveEngineCommand() {
        try {
            chan.basicConsume(queueName, (x, msg) -> {
                String m = new String(msg.getBody(), "UTF-8");
                engineInstruction = Integer.parseInt(m);
            }, x -> {
            });
            chan.queuePurge(queueName);
        } catch (IOException ex) {
            Logger.getLogger(PlaneController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
