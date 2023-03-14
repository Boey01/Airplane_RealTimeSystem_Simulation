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
public class SimulationAttributes {
    static public int simulationSpeed = 500; //1000 millisec == 1 sec
    static public int initialAltitude = 1000; 
    public int planespeed = 850;
    
    Random rand = new Random();
    public int altMax = (int) (initialAltitude*0.1);
    public int altMin = (int) (initialAltitude*-0.1);
    
    //oa means Off Angle
    public int oaMax = 15;
    public int oaMin = -15;
    
    static public int getSpeed() {
        return simulationSpeed;
    }    
    
    public int giveRandomAlt(){
   return rand.nextInt(altMax - altMin + 1) + altMin;
    }
    
    public int giveRandomOA(){
   return rand.nextInt(oaMax - oaMin + 1) + oaMin;
    }
    
    
    public void resetAltitudeRange(){
        altMax = (int) (initialAltitude*0.1);
        altMin = (int) (initialAltitude*-0.1);
    }
    
    public void resetOffAngleRange(){
        oaMax = 10;
        oaMin = -10;
    }
    
    public void changeOfAltitudeRules(int angle){
        switch (angle) {
            case 0:
                this.resetAltitudeRange();
                break;
            case 25:
                altMax = (int) (this.initialAltitude * 0.15);
                break;
            case -25:
                altMin = (int) (this.initialAltitude * -0.15);
                break;
            case 45:
                altMax = (int) (this.initialAltitude * 0.2);
                break;
            case -45:
                altMin = (int) (this.initialAltitude * -0.2);
                break;
            default:
                this.resetAltitudeRange();
                break;
        }
    }
    
    public void changeOfDirection(int angle){
        switch (angle) {
            case 0:
                this.resetOffAngleRange();
                break;
            case 20:
                oaMax = 3;
                oaMin = 0;
                break;
            case -20:
                oaMin = -3;
                oaMax = 0;
                break;
            default:
                this.resetOffAngleRange();
                break;
        }
    }
}
