/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Observers;

import Main.Data;
import Main.Data;
import Main.GUI;
import Main.GUI;

/**
 *
 * @author Boey
 */
public class CabinMask implements Observer{
    int cabinMaskCount = 1;
    GUI gui;
    public CabinMask(GUI gui) {
        this.gui = gui;
    }
   
    @Override
    public void updateObserver() {
      if (cabinMaskCount > 0){
          gui.taAlerts.append("!!!Realeasing Cabin Mask!!!\n");
          cabinMaskCount --;
      }
    }
    
    @Override
    public void updateObserver(Data data) {  
    }
    
}
