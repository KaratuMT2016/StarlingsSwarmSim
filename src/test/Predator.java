/*
 * 
 * 
 * 
 */
package test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import processing.core.PApplet;
import static processing.core.PApplet.abs;
import processing.core.PVector;


public class Predator {

    // main fields
    
    MainMap mainMap;
    PVector location;
    PVector velocity;
    
    float shade = 255;
    float maxSpeed;
    
    
    
    boolean attackFlag = false;
    int thinkTimer = 0;
    
    
    boolean atePrey;
    
    float atePreyCounter = 0.0f;
    float atePreyRecovery = 5.0f;
    
    
    long timer;
    
    int attackCounter = 0;
    int successCounter = 0;
    int failedCounter = 0;
    int eatCounter = 0;
  
   
    public Predator(MainMap mainMap, float xx, float yy, float speed) {
        this.mainMap = mainMap;
        velocity = new PVector(0, 0);
        location = new PVector(xx, yy);
        
            
        atePrey = false;
        Random rand = new Random();
        thinkTimer = rand.nextInt(10);
        
        maxSpeed = (float) speed; //(speed * mainMap.globalScale);
        //System.out.println("predSpeed = " + maxSpeed);
        timer = (long) (System.currentTimeMillis());
        
    }
    
    
    
    void go() {
        
        increment();
        wrap();
        
        long currentTime = (long) (System.currentTimeMillis());
        long delta = currentTime - timer;
        timer = currentTime;
                
        
        flockPredator();
        
        Boid nearestPrey = getNearestPrey();
        if (nearestPrey != null && !atePrey) {
            PVector nearestPreyDir = new PVector(nearestPrey.location.x, nearestPrey.location.y);
            nearestPreyDir.sub(new PVector(this.location.x, this.location.y));
            nearestPreyDir.normalize();
            
            PVector direction = nearestPreyDir;
            direction = direction.normalize();
            direction = direction.mult(maxSpeed);
            velocity = direction;
            
            nearestPrey.underAttack = true;
            
            if(!attackFlag)
                attackCounter++;
                attackFlag = true;
                
            
            if (shouldEatPrey(this.location.dist(nearestPrey.location))) {
                eatPrey(nearestPrey);
                atePrey = true;
                atePreyCounter = 0.0f;
            }
        }
        else
            attackFlag = false;
        
        if (atePrey) {
            atePreyCounter += (float)delta / 1000.f;
            //System.out.println("recovery Time :" + atePreyCounter);

            if (atePreyCounter > atePreyRecovery) {
                atePrey = false;
            }
            
        }
        
            location.add(velocity);
        
            
            failedCounter = attackCounter - eatCounter;
            
            
            //System.out.println("Total number of Attacks = " + attackCounter + " Sucess: " + eatCounter + "Failed: " + failedCounter);
            
    }

    
    private void eatPrey(Boid prey) {
        ArrayList<Boid> boids = getBoidsInCatchRange();
        
        int boidsInCatchRange = boids.size();
        
        float probability = 1.f / (float)boidsInCatchRange;
        Random random = new Random();
        float randomValue = random.nextFloat();
        
        boolean eat = false;
        if (randomValue < probability) {
            eat = true;
        }
        if (eat) {
                      
            //System.out.println("Boid caught by predator ");
            mainMap.boidsList.remove(prey);
            eatCounter++;
            
        } else {
            //System.out.println("Predator failed to catch the boid");
            
        }
 
    }
    
    private ArrayList<Boid> getBoidsInCatchRange() {
        ArrayList<Boid> boids = new ArrayList<Boid>();
        
        for (Boid boid : mainMap.boidsList) {
            if (this.location.dist(boid.location) < mainMap.catchRadius) {
                boids.add(boid);
            }
        }
        return boids;
    }
    
    private boolean shouldEatPrey(float distance) {
        return distance < mainMap.eatRadius;
    }
    
    private Boid getNearestPrey() {
        Boid nearestPrey = null;
        float minDistance = Float.MAX_VALUE;
        
        for (Boid prey : mainMap.boidsList) {
            float dist = abs(PVector.dist(this.location, prey.location));
            if (dist < minDistance && dist < mainMap.predRadius) {
                nearestPrey = prey;
                minDistance = dist;
            }
        }
        
        if (nearestPrey != null) {
            return nearestPrey;
        } else {
            return null;
        }
    }

    void flockPredator() {
        PVector avoidObjects = getAvoidAvoids();
        
        Random rand = new Random();
        PVector noise = new PVector(rand.nextInt(3) - 1, rand.nextInt(3) - 1);

        avoidObjects.mult((float) 2.5);
        if (!mainMap.option_avoid) {
            avoidObjects.mult(0);
        }

        noise.mult((float) 0.1);
        if (!mainMap.option_noise) {
            noise.mult(0);
        }

        mainMap.stroke(0, 255, 160);

        velocity.add(avoidObjects);
        velocity.add(noise);
        velocity.limit(maxSpeed);
    }

    float getAverageColor() {
        return 0;
    }

    PVector getAvoidAvoids() {
        PVector steer = new PVector(0, 0);
        int count = 0;

        for (Avoid other : mainMap.avoidsList) {
            float d = PVector.dist(location, other.pos);
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((d > 0) && (d < mainMap.avoidRadius)) {
                // Calculate vector pointing away from neighbor
                PVector diff = PVector.sub(location, other.pos);
                diff.normalize();
                diff.div(d);        // Weight by distance
                steer.add(diff);
                count++;            // Keep track of how many
            }
        }
        return steer;
    }
    
    

    void draw() {

        mainMap.noStroke();
        //pApplet.fill(shade, 90, 200);
        mainMap.fill(shade);
        mainMap.pushMatrix();
        mainMap.translate(location.x, location.y);
        mainMap.rotate(velocity.heading());
        mainMap.beginShape();
        mainMap.vertex(15 * mainMap.globalScale, 0);
        mainMap.vertex(-7 * mainMap.globalScale, 7 * mainMap.globalScale);
        mainMap.vertex(-7 * mainMap.globalScale, -7 * mainMap.globalScale);
        mainMap.endShape(mainMap.CLOSE);
        mainMap.popMatrix();
    }

    // update all those timers!
    void increment() {
        thinkTimer = (thinkTimer + 1) % 5;
    }

    void wrap() {
        location.x = (location.x + mainMap.width) % mainMap.width;
        location.y = (location.y + mainMap.height) % mainMap.height;
    }
}
