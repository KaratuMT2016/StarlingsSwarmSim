Murve[] murves = new Murve[100];

void setup() {
  size(640,360);
  background(255);
  for(int i = 0; i < murves.length; i++){
    murves[i] = new Murve();
  }
} 
 void draw() {
    stroke(0);
    background(255);
    for(int i = 0; i < murves.length; i++){
    PVector wind = new PVector(0.001,0);
    float m = murves[i].mass;
    PVector gravity = new PVector(0,0.1*m);
    murves[i].applyForce(wind);
    murves[i].applyForce(gravity);
    murves[i].update();
    murves[i].display();
    murves[i].checkEdge();
  }
}
