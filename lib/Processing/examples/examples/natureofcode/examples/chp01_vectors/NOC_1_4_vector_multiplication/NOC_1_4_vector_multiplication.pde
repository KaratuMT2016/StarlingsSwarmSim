// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com

// Example 1-4: Vector multiplication

void setup() {
  size(640,360);
  smooth();
}

void draw() {
  background(255);
  
  PVector mouse = new PVector(mouseX,mouseY);
  PVector center = new PVector(width/2,height/2);
  mouse.sub(center);
  
  // Multiplying a vector!  The vector is now half its original size (multiplied by 0.5).
  mouse.mult(0.5);  
  
  translate(width/2,height/2);
  strokeWeight(2);
  stroke(2);
  line(0,0,mouse.x,mouse.y);
}


