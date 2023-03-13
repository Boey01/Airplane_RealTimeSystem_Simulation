/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Observers;

import com.mycompany.rts_assignment.*;

/**
 *
 * @author Boey
 */
public class PressureSensor implements Observer{
    int pressure,altitude,speed;
    int temperature = 25;
    GUI gui;
    SensoryData sd;

    public PressureSensor(GUI gui) {
        this.gui = gui;
    }
    
     @Override
    public void updateObserver(SensoryData data) {
        sd = data;
        if(sd.routingKey == "altitude"){
            altitudeChanged(sd.data);
        }
        
        if(sd.routingKey == "speed"){
            speedChanged(sd.data);
        }       
        pressure = (altitude+speed)/temperature;
        //System.out.println("Cabin Pressure: "+pressure);
        gui.txtPressure.setText(String.valueOf(pressure));
    }
    
    public void altitudeChanged(int alt) {
      this.altitude = alt;
    }

    public void speedChanged(int speed) {
        this.speed = speed;
    }
    
}
