  /*
 * 
 * 
 * 
 */
package test;

import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import processing.core.PApplet;
import static processing.core.PApplet.abs;
import static processing.core.PApplet.sin;
import processing.core.PVector;

/**
 *
 * @author r02aha16
 */
public class Boid {

    // main fields
    MainMap mainMap;
    PVector location;
    PVector velocity;
    
    ArrayList<Boid> friends;
    public float maxSpeed;
    public float confuseSpeed;
    
    boolean seenFlag;
    boolean targetFlag;
    
    float standardDeviation;
    float mean;
    float shade;
    int boidID;
    
    boolean visited = false;
    
    int thinkTimer = 0;
    
    Predator nearestPred;
    
    boolean underAttack = false;
    boolean confuse = false;
    Boid confuseTarget = null;

    public Boid(MainMap pApplet, float xx, float yy, float meanSpeed, int boidID) {
        
        this.mainMap = pApplet;
        velocity = new PVector(0, 0);
        location = new PVector(xx, yy);
        
        this.boidID = boidID;

        seenFlag = false;
        
        //standardDeviation = (float) 0.05;
        mean = (float) (meanSpeed * mainMap.globalScale);
       // mean = (float) (mean * 1.5);
        Random rand = new Random();
        thinkTimer = rand.nextInt(10);
        shade = rand.nextInt(255);
        friends = new ArrayList<Boid>();
                
//        Random r = new Random();
//        maxSpeed = (float) ((r.nextGaussian()* standardDeviation) + mean);
        //maxSpeed = (float) (maxSpeed + 0.1);(float) (r.nextGaussian()*Math.pow(standardDeviation,2)+mean);
//        if(maxSpeed < mean)
//            maxSpeed = mean;
//        else if(maxSpeed > 5)
//            maxSpeed = 5;
        Random r = new Random();

        LogNormalDistribution lnd = new LogNormalDistribution(2f, 0.6f);
        
        
        while(true)
        {
            maxSpeed = (float) (lnd.sample() + mean);
            //maxSpeed = (float) ((r.nextGaussian()* standardDeviation) + mean);
            if(maxSpeed >= 0.5 && maxSpeed <= mean +1)
                break;
        }
            
        
            
        //System.out.println("" + maxSpeed);
        //maxSpeed = (float) ((r.nextGaussian()* standardDeviation) + mean);
//        if(maxSpeed < 0.5)
//            maxSpeed = (float) 0.5;
//        else if(maxSpeed > 15.5)
//            maxSpeed = 56.9f;
        
        confuseSpeed = (float) ((maxSpeed)*(0.5+1));
            
    }

    void go() {
        increment();
        wrap();

        if (thinkTimer == 0) {
            // update our friend array (lots of square roots)
            getFriends();
        }
        flock();
        
        if (underAttack) {
            for (Boid friend : getFriends()) {
                if (friend != this) {
                    friend.confuse(this);
                }
            }
        }
        
        if (confuse && confuseTarget != null && confuseTarget != this) {
            PVector dir = new PVector(confuseTarget.location.x, confuseTarget.location.y);
            dir.sub(this.location);
            dir.normalize();
                       
            velocity = dir.mult(confuseSpeed);
        }
        
        location.add(velocity);
        
        underAttack = false;
        confuse = false;
    }
    
    public void confuse(Boid target) {
        confuse = true;
        confuseTarget = target;
    }

    void flock() {
        
        PVector allign = getAverageDir();
        PVector separation = getSeparation();
        PVector avoidObjects = getAvoidAvoids();
        PVector avoidPred = getAvoidPred();
        Random rand = new Random();
        PVector noise = PVector.random2D();
        noise.mult(2);
        PVector cohese = getCohesion();
        
        PVector predDir = getNearestPredDir();
        
        predDir.mult(1);
        if (!mainMap.option_confuse) {
            predDir.mult(0);
        }
        
        avoidPred.mult(3);
        if (!mainMap.option_predator) {
            avoidPred.mult(0);
        }

        allign.mult((float)2.5);
        if (!mainMap.option_friend) {
            allign.mult(0);
        }

        separation.mult(1);
        if (!mainMap.option_crowd) {
            separation.mult(0);
        }

        avoidObjects.mult((float)2.5);
        if (!mainMap.option_avoid) {
            avoidObjects.mult(0);
        }

        noise.mult((float) 0.1);
        if (!mainMap.option_noise) {
            noise.mult(0);
        }

        cohese.mult((float)1.5);
        if (!mainMap.option_cohese) {
            cohese.mult(0);
        }
      
        mainMap.stroke(0, 55, 160);
        velocity.add(cohese);
        velocity.add(allign);
        velocity.add(separation);
        velocity.add(avoidObjects);
        velocity.add(noise);
        velocity.add(avoidPred);
        velocity.add(predDir);
        velocity.limit(maxSpeed);

        shade += getAverageColor() * 0.03;
        shade += (rand.nextInt(2) - 1);
        shade = (shade + 255) % 255; //max(0, min(255, shade));

    }
    
    

    ArrayList<Boid> getFriends() {
        ArrayList<Boid> nearby = new ArrayList<Boid>();
        for (int i = 0; i < mainMap.boidsList.size(); i++) {
            Boid test = mainMap.boidsList.get(i);
            if (test == this) {
                continue;
            }
            if (Math.abs(test.location.x - this.location.x) < mainMap.friendRadius
                    && Math.abs(test.location.y - this.location.y) < mainMap.friendRadius) {
                nearby.add(test);
            }
        }
        friends = nearby;
        
        return friends;
    }

    float getAverageColor() {
        float total = 0;
        float count = 0;
        for (Boid other : friends) {
            if (other.shade - shade < -128) {
                total += other.shade + 255 - shade;
            } else if (other.shade - shade > 128) {
                total += other.shade - 255 - shade;
            } else {
                total += other.shade - shade;
            }
            count++;
        }
        if (count == 0) {
            return 0;
        }
        return total / (float) count;
    }

    PVector getAverageDir() {
        PVector sum = new PVector(0, 0);
        
        for (Boid other : friends) {
            float d = PVector.dist(location, other.location);
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((d > 0) && (d < mainMap.friendRadius)) {
                PVector copy = other.velocity.get();
                copy.normalize();
                copy.div(d);
                sum.add(copy);
                
            }
            
        }
        return sum;
    }

    PVector getSeparation() {
        PVector steer = new PVector(0, 0);
        
        for (Boid other : friends) {
            float d = PVector.dist(location, other.location);
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((d > 0) && (d < mainMap.crowdRadius)) {
                // Calculate vector pointing away from neighbor
                PVector diff = PVector.sub(location, other.location);
                diff.normalize();
                diff.div(d);        // Weight by distance
                steer.add(diff);
            }
        }
        
        return steer;
    }
    
    
    
    PVector getNearestPredDir() {
        PVector steer = new PVector(0, 0);
        
        ArrayList<Predator> predRangeList = new ArrayList<>();
        //The following code find the list of boids in the range of the eagle
        for (Predator pred : mainMap.predatorList) {
            float dist = abs(PVector.dist(this.location, pred.location));
            if ((dist > 0) && (dist < mainMap.predRadius)) {
                predRangeList.add(pred);
            }
        }
        //endcode

        //The following code find the nearest boid in the range created above
        if (!predRangeList.isEmpty()) {
            float currentDist = 0;
            float leastDist = 0;
            nearestPred = predRangeList.get(0);
            for (Predator pred : predRangeList) {
                currentDist = abs(PVector.dist(this.location, pred.location));
                leastDist = abs(PVector.dist(this.location, nearestPred.location));
                if (currentDist < leastDist) {
                    nearestPred = pred;
                }

            }
            
            steer = PVector.sub(nearestPred.location, this.location);
            steer.setMag((float) 0.05);
        }

        return steer;
    }

    PVector getAvoidAvoids() {
        PVector steer = new PVector(0, 0);
        int count = 0;

        for (Avoid avoid : mainMap.avoidsList) {
            float d = PVector.dist(location, avoid.pos);
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((d > 0) && (d < mainMap.avoidRadius)) {
                // Calculate vector pointing away from neighbor
                PVector diff = PVector.sub(location, avoid.pos);
                diff.normalize();
                //diff.div(d);        // Weight by distance
                diff.mult((float) 0.05);
                steer.add(diff);
                count++;            // Keep track of how many
            }
        }

        return steer;
    }
    
    public PVector getAvoidPred() {
        
        PVector steer = new PVector(0, 0);
                
        for (Predator predator : mainMap.predatorList) {
            float d = abs(PVector.dist(location, predator.location));
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((d > 0) && (d < mainMap.predRadius)) {
                
                PVector dir = PVector.sub(this.location, predator.location);
                dir.normalize();
                dir.mult((float) 0.05);

                steer.add(dir);
                
            }
        }

        if (mainMap.option_human_predator) {
            PVector humanPredator = new PVector(mainMap.mouseX, mainMap.mouseY);
            float dist = abs(PVector.dist(location, humanPredator));
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((dist > 0) && (dist < mainMap.predRadius)) {
                
                PVector dir = PVector.sub(this.location, humanPredator);
                dir.normalize();
                dir.mult((float) 0.05);
                steer.add(dir);
              
            }
        }

        return steer;
        
    }

    PVector getCohesion() {
        
        PVector sum = new PVector(0, 0);   // Start with empty vector to accumulate all locations
        int count = 0;
        for (Boid other : friends) {
            float d = PVector.dist(location, other.location);
            if ((d > 0) && (d < mainMap.coheseRadius)) {
                sum.add(other.location); // Add location
                count++;
            }
        }
        if (count > 0) {
            sum.div(count);

            PVector desired = PVector.sub(sum, location);
            desired.setMag((float) 0.05);
            return desired;
        } else {
            return new PVector(0, 0);
        }
    }

    void draw() {
        
        if(nearestPred != null)
        {
            mainMap.line(this.location.x, this.location.y, nearestPred.location.x, nearestPred.location.y);
            nearestPred = null;
        }
        mainMap.noStroke();

        if (seenFlag) {
            if (targetFlag) {
                mainMap.fill(Integer.parseInt("100"));
            } else {
                mainMap.fill(Integer.parseInt("200"));
            }
        } else {
            mainMap.fill(shade, 90, 200);
        }

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

    @Override
    public boolean equals(Object o) {
        if(this.boidID == ((Boid)o).boidID)
            return true;
        else
            return false;
    }
    
    
} 
