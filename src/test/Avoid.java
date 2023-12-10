package test;

import processing.core.PVector;

public class Avoid {
   PVector pos;
   MainMap pApplet;
   
   public Avoid (MainMap pApplet, float xx, float yy) {
        this.pApplet = pApplet;
        pos = new PVector(xx,yy);
   }
   
   //TODO: get back
   void draw () {
     pApplet.fill(0, 255, 200);
     pApplet.ellipse(pos.x, pos.y, 15, 15);
   }

}

