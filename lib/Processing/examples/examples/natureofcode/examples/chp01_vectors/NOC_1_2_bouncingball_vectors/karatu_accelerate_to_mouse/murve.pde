class Murve{

  PVector location;
  PVector velocity;
  PVector acceleration;
  float topspeed;
  float mass;
  
  Murve(){
  
    location = new PVector(random(width),random(height));
    velocity = new PVector(0,0);
    acceleration = new PVector(0,0);
    topspeed = 10;
    mass = 10;
  }
  
  void applyForce(PVector force) {
    PVector f = PVector.div(force,mass);
    acceleration.add(f);
  }
  
  void update(){
  
    PVector mouse = new PVector(mouseX, mouseY);
    PVector dir = PVector.sub(mouse, location);
    dir.normalize();
    dir.mult(0.5);
    acceleration = dir;
    
    velocity.add(acceleration);
    velocity.limit(topspeed);
    location.add(velocity);
    acceleration.mult(0);
  }
  
  void display(){
    stroke(0);
    strokeWeight(2);
    fill(145);
    ellipse(location.x,location.y,30,30);   
  }
  
  void checkEdge(){
    
    if(location.x > width){
        location.x = 0;
    }else if(location.x < 0){
        location.x = width;
    }
    if(location.y > height){
        location.y = 0;
    }else if(location.y < 0){
        location.y = height;
    }
  }
}
