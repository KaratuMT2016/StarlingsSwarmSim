package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import processing.core.PApplet;
import processing.core.PVector;

public class MainMap extends PApplet {

    public Boid barry;
    public ArrayList<Predator> predatorList;
    public ArrayList<Boid> boidsList;
    public ArrayList<Avoid> avoidsList;

    int expCount = 0;
    List<Experiment> expList = new ArrayList<>();

    private class Experiment {

        float boidMeanSpeed;
        float predMeanSpeed;
        int numBiod;
        int numPred;
        int expID;

        public Experiment(int expID, int numBoid, int numPred, float boidMeanSpeed, float predMeanSpeed) {
            this.boidMeanSpeed = boidMeanSpeed;
            this.predMeanSpeed = predMeanSpeed;
            this.expID = expID;
            this.numBiod = numBoid;
            this.numPred = numPred;
        }
    }

    public float globalScale = (float) 0.91;
    public float eraseRadius = 20;
    public String tool = "boids";
    public int mapXSize = 2024;
    public int mapYSize = 1024;

    // boid control
    public float friendRadius;
    public float crowdRadius;
    public float avoidRadius;
    public float coheseRadius;
    public float separateRadius;
    public float predRadius;
    public float eatRadius;
    public float catchRadius;

    public boolean option_friend = true;
    public boolean option_crowd = true;
    public boolean option_avoid = true;
    public boolean option_noise = true;
    public boolean option_cohese = true;
    public boolean option_separate = true;
    public boolean option_predator = true;
    public boolean option_human_predator = true;
    public boolean option_wind = true;
    public boolean option_confuse = true;

    // gui crap
    public int messageTimer = 0;
    public String messageText = "";
    long timer = (long) (System.currentTimeMillis());

    public static void main(String... args) {
//        BinomialDistribution bd = new BinomialDistribution(40, 0.178);
//        System.out.println("" + bd.getProbabilityOfSuccess());
//        System.out.println("" + bd.getNumericalMean());
//        System.out.println("" + bd.probability(1));

        //MainMap main = new MainMap();
        PApplet.runSketch(new String[]{"test. Main"}, new MainMap());
    }

    @Override
    public void settings() {

        size(mapXSize, mapYSize);
    }

    @Override
    public void setup() {
        textSize(16);
        recalculateConstants();

        predatorList = new ArrayList<Predator>();
        //predatorList.add(new Predator(this, 150, 150));
        //predatorList.add(new Predator(this, 200, 200));
        boidsList = new ArrayList<Boid>();
        avoidsList = new ArrayList<Avoid>();
        int i = 0;
        int k = 5;
        for (int j = 0; j < k; j++) {
            expList.add(new Experiment(i++, 100, 1, 3.731f, 4.29f));
        }
        for (int j = 0; j < k; j++) {
            expList.add(new Experiment(i++, 100, 2, 3.731f, 4.29f));
        }
        for (int j = 0; j < k; j++) {
            expList.add(new Experiment(i++, 100, 3, 3.731f, 4.29f));
        }
        for (int j = 0; j < k; j++) {
            expList.add(new Experiment(i++, 100, 4, 3.731f, 4.29f));
        }
        for (int j = 0; j < k; j++) {
            expList.add(new Experiment(i++, 100, 5, 3.731f, 4.29f));
        }
        for (int j = 0; j < k; j++) {
            expList.add(new Experiment(i++, 100, 6, 3.731f, 4.29f));
        }
        for (int j = 0; j < k; j++) {
            expList.add(new Experiment(i++, 100, 7, 3.731f, 4.29f));
        }
        for (int j = 0; j < k; j++) {
            expList.add(new Experiment(i++, 100, 8, 3.731f, 4.29f));
        }
        for (int j = 0; j < k; j++) {
            expList.add(new Experiment(i++, 100, 9, 3.731f, 4.29f));
        }
        for (int j = 0; j < k; j++) {
            expList.add(new Experiment(i++, 100, 10, 3.731f, 4.29f));
        }

//                  expList.add(new Experiment(1, 3.731f, 4.29f));
//                  expList.add(new Experiment(2, 3.731f, 4.29f));
//                  expList.add(new Experiment(3, 3.731f, 4.29f));
//                  expList.add(new Experiment(4, 3.731f, 4.29f));
//                  expList.add(new Experiment(5, 3.731f, 4.29f));
//                  expList.add(new Experiment(6, 3.731f, 4.29f));
//                  expList.add(new Experiment(7, 3.731f, 4.29f));
//                  expList.add(new Experiment(8, 3.731f, 4.29f));
//                  expList.add(new Experiment(9, 3.731f, 4.29f));
//                  expList.add(new Experiment(10, 3.731f, 4.29f));
//                expList.add(new Experiment(1, 1.001f, 1.81f));
//                expList.add(new Experiment(2, 1.911f, 1.81f));
//                expList.add(new Experiment(3, 2.821f, 1.81f));
//                expList.add(new Experiment(4, 3.731f, 1.81f));
//                expList.add(new Experiment(5, 4.641f, 1.81f));
//                expList.add(new Experiment(6, 5.551f, 1.81f));
//                expList.add(new Experiment(7, 6.461f, 1.81f));
//                expList.add(new Experiment(8, 7.371f, 1.81f));
//                expList.add(new Experiment(9, 8.281f, 1.81f));
//                expList.add(new Experiment(10, 9.191f, 1.81f));
//                expList.add(new Experiment(11, 1.001f, 2.69f));
//                expList.add(new Experiment(12, 1.911f, 2.69f));
//                expList.add(new Experiment(13, 2.821f, 2.69f));
//                expList.add(new Experiment(14, 3.731f, 2.69f));
//                expList.add(new Experiment(15, 4.641f, 2.69f));
//                expList.add(new Experiment(16, 5.551f, 2.69f));
//                expList.add(new Experiment(17, 6.461f, 2.69f));
//                expList.add(new Experiment(18, 7.371f, 2.69f));
//                expList.add(new Experiment(19, 8.281f, 2.69f));
//                expList.add(new Experiment(20, 9.191f, 2.69f));
//                expList.add(new Experiment(21, 1.001f, 3.48f));
//                expList.add(new Experiment(22, 1.911f, 3.48f));
//                expList.add(new Experiment(23, 2.821f, 3.48f));
//                expList.add(new Experiment(24, 3.731f, 3.48f));
//                expList.add(new Experiment(25, 4.641f, 3.48f));
//                expList.add(new Experiment(26, 5.551f, 3.48f));
//                expList.add(new Experiment(27, 6.461f, 3.48f));
//                expList.add(new Experiment(28, 7.371f, 3.48f));
//                expList.add(new Experiment(29, 8.281f, 3.48f));
//                expList.add(new Experiment(30, 9.191f, 3.48f));
//                expList.add(new Experiment(31, 1.001f, 4.29f));
//                expList.add(new Experiment(32, 1.911f, 4.29f));
//                expList.add(new Experiment(33, 2.821f, 4.29f));
//                expList.add(new Experiment(1, 3.731f, 4.29f));
//                expList.add(new Experiment(35, 4.641f, 4.29f));
//                expList.add(new Experiment(36, 5.551f, 4.29f));
//                expList.add(new Experiment(37, 6.461f, 4.29f));
//                expList.add(new Experiment(38, 7.371f, 4.29f));
//                expList.add(new Experiment(39, 8.281f, 4.29f));
//                expList.add(new Experiment(40, 9.191f, 4.29f));
//                expList.add(new Experiment(41, 1.001f, 5.08f));
//                expList.add(new Experiment(42, 1.911f, 5.08f));
//                expList.add(new Experiment(43, 2.821f, 5.08f));
//                expList.add(new Experiment(44, 3.731f, 5.08f));
//                expList.add(new Experiment(45, 4.641f, 5.08f));
//                expList.add(new Experiment(46, 5.551f, 5.08f));
//                expList.add(new Experiment(47, 6.461f, 5.08f));
//                expList.add(new Experiment(48, 7.371f, 5.08f));
//                expList.add(new Experiment(49, 8.281f, 5.08f));
//                expList.add(new Experiment(50, 9.191f, 5.08f));
//                expList.add(new Experiment(51, 1.001f, 5.94f));
//                expList.add(new Experiment(52, 1.911f, 5.94f));
//                expList.add(new Experiment(53, 2.821f, 5.94f));
//                expList.add(new Experiment(54, 3.731f, 5.94f));
//                expList.add(new Experiment(55, 4.641f, 5.94f));
//                expList.add(new Experiment(56, 5.551f, 5.94f));
//                expList.add(new Experiment(57, 6.461f, 5.94f));
//                expList.add(new Experiment(58, 7.371f, 5.94f));
//                expList.add(new Experiment(59, 8.281f, 5.94f));
//                expList.add(new Experiment(60, 9.191f, 5.94f));
//                expList.add(new Experiment(61, 1.001f, 6.80f));
//                expList.add(new Experiment(62, 1.911f, 6.80f));
//                expList.add(new Experiment(63, 2.821f, 6.80f));
//                expList.add(new Experiment(64, 3.731f, 6.80f));
//                expList.add(new Experiment(65, 4.641f, 6.80f));
//                expList.add(new Experiment(66, 5.551f, 6.80f));
//                expList.add(new Experiment(67, 6.461f, 6.80f));
//                expList.add(new Experiment(68, 7.371f, 6.80f));
//                expList.add(new Experiment(69, 8.281f, 6.80f));
//                expList.add(new Experiment(70, 9.191f, 6.80f));
//                expList.add(new Experiment(71, 1.001f, 7.60f));
//                expList.add(new Experiment(72, 1.911f, 7.60f));
//                expList.add(new Experiment(73, 2.821f, 7.60f));
//                expList.add(new Experiment(74, 3.731f, 7.60f));
//                expList.add(new Experiment(75, 4.641f, 7.60f));
//                expList.add(new Experiment(76, 5.551f, 7.60f));
//                expList.add(new Experiment(77, 6.461f, 7.60f));
//                expList.add(new Experiment(78, 7.371f, 7.60f));
//                expList.add(new Experiment(79, 8.281f, 7.60f));
//                expList.add(new Experiment(80, 9.191f, 7.60f));
//                expList.add(new Experiment(81, 1.001f, 8.40f));
//                expList.add(new Experiment(82, 1.911f, 8.40f));
//                expList.add(new Experiment(83, 2.821f, 8.40f));
//                expList.add(new Experiment(84, 3.731f, 8.40f));
//                expList.add(new Experiment(85, 4.641f, 8.40f));
//                expList.add(new Experiment(86, 5.551f, 8.40f));
//                expList.add(new Experiment(87, 6.461f, 8.40f));
//                expList.add(new Experiment(88, 7.371f, 8.40f));
//                expList.add(new Experiment(89, 8.281f, 8.40f));
//                expList.add(new Experiment(90, 9.191f, 8.40f));
//                expList.add(new Experiment(91, 1.001f, 9.25f));       
//                expList.add(new Experiment(92, 1.911f, 9.25f));
//                expList.add(new Experiment(93, 2.821f, 9.25f));
//                expList.add(new Experiment(94, 3.731f, 9.25f));
//                expList.add(new Experiment(95, 4.641f, 9.25f));
//                expList.add(new Experiment(96, 5.551f, 9.25f));
//                expList.add(new Experiment(97, 6.461f, 9.25f));
//                expList.add(new Experiment(98, 7.371f, 9.25f));
//                expList.add(new Experiment(99, 8.281f, 9.25f));
//                expList.add(new Experiment(100, 9.191f, 9.25f));
        initWorld(expList.get(0));
        setupWalls();
        frameRate(40);
    }

    public void initWorld(Experiment exp) {
        boidsList.clear();
        predatorList.clear();

        FileWriter fw;
        try {
            fw = new FileWriter("boids.csv", true);
            fw.write("\n");
            for (int i = 0; i < exp.numBiod; i++) {
                Boid b = new Boid(this, random(mapXSize), random(mapYSize), exp.boidMeanSpeed, i);
                boidsList.add(b);
                fw.write("" + b.maxSpeed + ",");
            }

            //fw.write("\n");
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(MainMap.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < exp.numPred; i++) {
            predatorList.add(new Predator(this, random(mapXSize), random(mapYSize), exp.predMeanSpeed));
        }

    }

// This will recalculate constants
    void recalculateConstants() {
        friendRadius = 150 * globalScale;
        crowdRadius = (float) (friendRadius / 1.3);
        avoidRadius = 90 * globalScale;
        coheseRadius = friendRadius;
        separateRadius = friendRadius;
        predRadius = 300 * globalScale;
        eatRadius = 10 * globalScale;

        catchRadius = 20 * globalScale;
    }
// This set the wall that birds and predator can not exceed 

    void setupWalls() {
        avoidsList = new ArrayList<Avoid>();
        for (int x = 0; x < width; x += 10) {
            avoidsList.add(new Avoid(this, x, 10));
            avoidsList.add(new Avoid(this, x, height - 10));
        }
        for (int y = 0; y < height; y += 10) {
            avoidsList.add(new Avoid(this, 10, y));
            avoidsList.add(new Avoid(this, width - 10, y));
        }
    }

    void setupCircle() {
        avoidsList = new ArrayList<Avoid>();
        for (int x = 0; x < 100; x += 1) {
            float dir = (float) ((x / 100.0) * TWO_PI);
            //TODO: get back
            float xx = (int) (float) (width * 0.5 + Math.cos(dir) * height * 0.4);
            float yy = (float) (height * 0.5 + Math.sin(dir) * height * 0.4);
            avoidsList.add(new Avoid(this, xx, yy));
        }
    }

    int totalAvgNeighbourCount = 0;
    float totalAvgNeighbour = 0;

    @Override
    public void draw() {

        noStroke();
        colorMode(HSB);
        fill(0, 100);
        rect(0, 0, width, height);

        if (tool == "erase") {
            noFill();
            stroke(0, 100, 260);
            rect(mouseX - eraseRadius, mouseY - eraseRadius, eraseRadius * 2, eraseRadius * 2);
            if (mousePressed) {
                erase();
            }
        } else if (tool == "avoids") {
            noStroke();
            fill(0, 200, 200);
            ellipse(mouseX, mouseY, 15, 15);
        }
        for (int i = 0; i < boidsList.size(); i++) {

            Boid current = boidsList.get(i);

            current.go();
            current.draw();
        }
//        calculateAvgNeighbour();
        //System.out.println("calculateAvgNeighbour(): " + calculateAvgNeighbour());
        totalAvgNeighbour += calculateAvgNeighbour();
        totalAvgNeighbourCount++;

        for (int i = 0; i < avoidsList.size(); i++) {
            Avoid current = avoidsList.get(i);
            //current.go();
            current.draw();
        }

        for (int i = 0; i < predatorList.size(); i++) {
            Predator predator = predatorList.get(i);

            predator.go();
            predator.draw();
        }

        long currentTime = (long) (System.currentTimeMillis());
        long delta = currentTime - timer;
        float myTimer = delta / 1000.0f;
        
            
        if (myTimer > 300) {//TIME PASSES
            try {
                File f = new File("data.csv");
                FileWriter fw = new FileWriter(f, true);
                String result;
//                result = "Exp ID " + expList.get(expCount).expID
//                        + " Boid Mean Speed " + expList.get(expCount).boidMeanSpeed
//                        + " Pred Mean Speed " + expList.get(expCount).predMeanSpeed
//                        + " AVG Neighbour " + totalAvgNeighbour / totalAvgNeighbourCount;
                result = expList.get(expCount).expID + ",";
                result += expList.get(expCount).boidMeanSpeed + ",";
                result += expList.get(expCount).predMeanSpeed + ",";
                result += totalAvgNeighbour / totalAvgNeighbourCount + ",";
                for (int i = 0; i < predatorList.size(); i++) {
                    Predator predator = predatorList.get(i);
                    result+= predator.attackCounter + "," + predator.eatCounter + "," + predator.failedCounter + ",";
                }
                        
                fw.write(result);
                fw.write(System.getProperty("line.separator"));
                fw.close();
                //predator.attackCounter = predator.eatCounter = predator.failedCounter = 0;
                totalAvgNeighbour = 0;
                totalAvgNeighbourCount = 0;

            } catch (Exception e) {

            }
            
            //reset the population
            timer = (long) (System.currentTimeMillis());
            expCount++;
            initWorld(expList.get(expCount));
            System.out.println("-------------------------------------------");
        }    

        if (messageTimer > 0) {
            messageTimer -= 1;
        }
        drawGUI();
    }

    private float calculateAvgNeighbour() {
        //NEW CODE
        List<List<Boid>> totalConnectedBoidsList = new ArrayList<>();

        List<Boid> visitedBoids = new ArrayList<>();
        visitedBoids.addAll(boidsList);

        //Graph connectedGraph = new Graph(boidsList.size());
        Graph connectedGraph = new Graph(100);
        for (Boid boid : boidsList) {
            for (Boid friendBoid : boid.getFriends()) {
                connectedGraph.addEdge(boid, friendBoid);
            }
        }
        while (!visitedBoids.isEmpty()) {
            List<Boid> connectedBoids = connectedGraph.BFS(visitedBoids.get(0));
            totalConnectedBoidsList.add(connectedBoids);
            visitedBoids.removeAll(connectedBoids);
        }

        int size = 0;
        int total = totalConnectedBoidsList.size();
        for (int i = 0; i < totalConnectedBoidsList.size(); i++) {

            List<Boid> connectedBoids = totalConnectedBoidsList.get(i);
            size += connectedBoids.size();
            System.out.print(connectedBoids.size() + ",");
        }

        System.out.println("");

        for (Boid boid : boidsList) {
            boid.visited = false;
        }

        return size * 1f / total;
    }

    @Override
    public void keyPressed() {
        if (key == 'q') {
            tool = "boids";
            message("Add boids");
        } else if (key == 'w') {
            tool = "avoids";
            message("Place obstacles");
        } else if (key == 'o') {
            predatorList.add(new Predator(this, random(mapXSize), random(mapYSize), expList.get(expCount).predMeanSpeed));
            message("Placed Predator & Size = " + predatorList.size());
        } else if (key == 'p') {
            predatorList.remove(predatorList.size() - 1);
            message("Removed Predator & Size = " + predatorList.size());
        } else if (key == 'k') {
            option_human_predator = option_human_predator ? false : true;
            message("Turned option_human_predator " + on(option_human_predator));
        } else if (key == 'p') {
            tool = "pred";
            message("Place predator");
        } else if (key == 'e') {
            tool = "erase";
            message("Eraser");
        } else if (key == '-') {
            message("Decreased scale");
            globalScale *= 0.8;
        } else if (key == '=') {
            message("Increased Scale");
            globalScale /= 0.8;
        } else if (key == '1') {
            option_friend = option_friend ? false : true;
            message("Turned friend allignment " + on(option_friend));
        } else if (key == '2') {
            option_crowd = option_crowd ? false : true;
            message("Turned crowding avoidance " + on(option_crowd));
        } else if (key == '3') {
            option_avoid = option_avoid ? false : true;
            message("Turned obstacle avoidance " + on(option_avoid));
        } else if (key == '4') {
            option_cohese = option_cohese ? false : true;
            message("Turned cohesion " + on(option_cohese));
        } else if (key == '5') {
            option_noise = option_noise ? false : true;
            message("Turned noise " + on(option_noise));
        } else if (key == ',') {
            setupWalls();
        } else if (key == '.') {
            setupCircle();
        }
        recalculateConstants();

    }

    void drawGUI() {
        if (messageTimer > 0) {
            fill((float) ((min(30, messageTimer) / 30.0) * 255.0));

            text(messageText, 10, height - 20);
        }
    }

    String s(int count) {
        return (count != 1) ? "s" : "";
    }

    String on(boolean in) {
        return in ? "on" : "off";
    }

    @Override
    public void mousePressed() {

        if (tool == "boids") {
            //boidsList.add(new Boid(this, mouseX, mouseY, 2.1f));
            message(boidsList.size() + " Total Boid" + s(boidsList.size()));
        } else if (tool == "avoids") {
            avoidsList.add(new Avoid(this, mouseX, mouseY));
        } else if (tool == "pred") {

        }
    }

    void erase() {
        for (int i = boidsList.size() - 1; i > -1; i--) {
            Boid b = boidsList.get(i);
            if (abs(b.location.x - mouseX) < eraseRadius && abs(b.location.y - mouseY) < eraseRadius) {
                boidsList.remove(i);
            }
        }

        for (int i = avoidsList.size() - 1; i > -1; i--) {
            Avoid b = avoidsList.get(i);
            if (abs(b.pos.x - mouseX) < eraseRadius && abs(b.pos.y - mouseY) < eraseRadius) {
                avoidsList.remove(i);
            }
        }
    }

    void drawText(String s, float x, float y) {
        fill(0);
        text(s, x, y);
        fill(200);
        text(s, x - 1, y - 1);
    }

    void message(String in) {
        messageText = in;
        messageTimer = (int) frameRate * 3;
    }

}
