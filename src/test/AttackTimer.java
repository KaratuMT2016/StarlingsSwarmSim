/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author musak
 */

public class AttackTimer {
   
    int secondspassed = 0;
        
    Timer myTimer = new Timer();
    
    TimerTask task = new TimerTask(){
        //Its the heart of my timer task
        public void run(){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(AttackTimer.class.getName()).log(Level.SEVERE, null, ex);
//            }
            secondspassed++;
            //System.out.println("seconds passed " + secondspassed);
        }
    };
    
    public void start(){
       
        myTimer.scheduleAtFixedRate(task, 1000, 1000);
    }
    
    public void resetTime()
    {
      secondspassed = 0;
          
    }
    
}
