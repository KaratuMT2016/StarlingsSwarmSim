// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com

// Example 1-1: Bouncing Ball, no vectors
float x = 100;
float y = 100;
float xspeed = 1;
float yspeed = 3.3;

void setup() {
  size(800, 200);
  smooth();
}

void draw() {
  background(255);


  // Add the current speed to the position.
  x = x + xspeed;
  y = y + yspeed;

  if ((x > width) || (x < 0)) {
    xspeed = xspeed * -1;
  }
  if ((y > height) || (y < 0)) {
    yspeed = yspeed * -1;
  }


  // Display circle at x position
  stroke(0);
  strokeWeight(2);
  fill(127);
  ellipse(x, y, 16, 16);
}

