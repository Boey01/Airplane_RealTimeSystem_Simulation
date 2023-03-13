/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

/**
 *
 * @author Boey
 */
public class PressureSensor {
    int pressure,altitude,speed;
    int temperature = 25;
    GUI gui;

    public PressureSensor(GUI gui) {
        this.gui = gui;
    }
    
    
    public void altitudeChanged(int alt) {
      this.altitude = alt;
      checkPressure();
    }

    public void speedChanged(int speed) {
        this.speed = speed;
        checkPressure();
    }
    
    public void checkPressure(){
        pressure = (altitude+speed)/temperature;
        //System.out.println("Cabin Pressure: "+pressure);
        gui.txtPressure.setText(String.valueOf(pressure));
    }
}
