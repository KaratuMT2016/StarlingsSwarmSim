Movers mover;

void setup(){
size(640,360);
background(255);
mover = new Movers();
}

void draw(){
background(255);
mover.update();
mover.checkEdges();
mover.display();
}
