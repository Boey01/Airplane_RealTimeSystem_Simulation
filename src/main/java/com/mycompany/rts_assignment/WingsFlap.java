/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rts_assignment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Boey
 */
public class WingsFlap implements Runnable{
SystemPhase phase;
    int angle = 0;
List<Integer> wingsCommand;

    public WingsFlap(List<Integer> wc, SystemPhase sp) {
        this.wingsCommand = wc;
        this.phase = sp;
    }

    @Override
    public void run() {
        angle = wingsCommand.remove(0);

        System.out.println("Wings' angle has been adjusted to: " + angle);

        switch (angle) {
            case 0:
                phase.resetAltitudeRange();
                break;
            case 25:
                phase.min += (int) (phase.idealAltitude * 0.1);
                phase.max += (int) (phase.idealAltitude * 0.1);
                break;
            case -25:
                phase.min -= (int) (phase.idealAltitude * 0.1);
                phase.max -= (int) (phase.idealAltitude * 0.1);
                break;
            case 45:
                phase.min += (int) (phase.idealAltitude * 0.2);
                phase.max += (int) (phase.idealAltitude * 0.2);
                break;
            case -45:
                phase.min -= (int) (phase.idealAltitude * 0.2);
                phase.max -= (int) (phase.idealAltitude * 0.2);
                break;
            default:
                phase.resetAltitudeRange();
                break;
        }

    }
    
}
