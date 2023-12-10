class Movers{

  PVector location;
  PVector velocity;
  PVector acceleration;
  float topspeed = 20;
  
  Movers(){
    location = new PVector(width/2,height/2);
    velocity = new PVector(0,0);
    acceleration = new PVector(-0.001,0.01);
  }
  
  void update(){
  
    velocity.add(acceleration);
    velocity.limit(topspeed);
    location.add(velocity);
  }
  
  void display(){
    stroke(0);
    strokeWeight(2);
    fill(175);
    ellipse(location.x,location.y,50,50);
  }
  
  void checkEdges(){
    
  if(location.x > width){
    location.x = 0;
  }
   else if (location.x < 0){
     location.x = width;
    }
  if(location.y > height){
     location.y = 0;
  }
  else if(location.y < 0){
      location.y = height;
    }
  }
}
