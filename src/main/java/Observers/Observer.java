/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Observers;

import Sensories.SensoryData;

/**
 *
 * @author Boey
 */
public interface Observer {
    public void updateObserver();
    public void updateObserver(SensoryData data);
}
