class Mover{
  PVector location;
  PVector velocity;
  
   //Mover m = new Mover();
  //constructor for the mover class
  Mover(){
    location = new PVector(random(width), random(height));
    velocity = new PVector(random(-2,2), random(-2,2));  
  }

  void update(){
  location.add(velocity);
  }
  
  void display(){
    noStroke();
    fill(175);
    ellipse(location.x,location.y,16,16);
  }
  
void checkEdges(){
  if((location.x > width) || (location.x < 0)){
  velocity.x = velocity.x*-1;
}
if((location.y>height) || (location.y<0)){
velocity.y = velocity.y*-1;
    }
  }
}
