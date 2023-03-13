/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Actuators;

import com.mycompany.rts_assignment.PlaneController;
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

    public Engine(SimulationAttributes s) {
        this.simulation = s;
    }

    @Override
    public void run() {
        receiveEngineCommand();
        if (engineInstruction ==1){
            simulation.planespeed -= rand.nextInt(20);
        }
        if (engineInstruction ==2){
            simulation.planespeed += rand.nextInt(20);
        }
    
    }
        public void receiveEngineCommand() {
        try {
            String exchangeName = "CommandExchange";
            String routingKey = "engine";

            ConnectionFactory cf = new ConnectionFactory();
            Connection con = cf.newConnection();
            Channel chan = con.createChannel();

            chan.exchangeDeclare(exchangeName, "direct");

            String queueName = chan.queueDeclare().getQueue();
            chan.queueBind(queueName, exchangeName, routingKey);

            chan.basicConsume(queueName, (x, msg) -> {
                String m = new String(msg.getBody(), "UTF-8");
                engineInstruction = Integer.parseInt(m);
            }, x -> {
            });

        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(PlaneController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
