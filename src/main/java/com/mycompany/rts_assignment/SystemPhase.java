/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import java.util.Random;

/**
 *
 * @author Boey
 */
public class SystemPhase {
    static int speed = 500; //1000 millisec or 1 sec
    static int idealAltitude = 1000; // maintain around 50 ~ 80
    
    Random rand = new Random();
    int max = (int) (idealAltitude*0.2);
    int min = (int) (idealAltitude*-0.2);
    
    static public int getSpeed() {
        return speed;
    }    
    
    public int giveRandom(){
   return rand.nextInt(max - min + 1) + min;
    }
    
    public void resetAltitudeRange(){
        max = (int) (idealAltitude*0.2);
        min = (int) (idealAltitude*-0.2);
    }
    
    public void changeOfAltitudeRules(int angle){
        switch (angle) {
            case 0:
                this.resetAltitudeRange();
                break;
            case 25:
                max = (int) (this.idealAltitude * 0.11);
                break;
            case -25:
                min = (int) (this.idealAltitude * -0.11);
                break;
            case 45:
                max = (int) (this.idealAltitude * 0.15);
                break;
            case -45:
                min = (int) (this.idealAltitude * -0.15);
                break;
            default:
                this.resetAltitudeRange();
                break;
        }
    }
}
