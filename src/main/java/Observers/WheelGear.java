/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Observers;

import com.mycompany.rts_assignment.Data;
import com.mycompany.rts_assignment.Data;
import com.mycompany.rts_assignment.GUI;
import com.mycompany.rts_assignment.GUI;

/**
 *
 * @author Boey
 */
public class WheelGear implements Observer {

    boolean open = false;
    GUI gui;

    public WheelGear(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void updateObserver() {
         if (!open) {
            gui.taAlerts.append("Airplane wheels are opened. \n");
            open = true;
        }
    }

    @Override
    public void updateObserver(Data data){       
    }

}
