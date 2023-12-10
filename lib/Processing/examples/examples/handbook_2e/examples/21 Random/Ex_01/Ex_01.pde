float f = random(32, 128);     // Assign f a float value from 0 to 5.2
float i = random(5.2);       // ERROR! Can't assign a float to an int 
int j = int(random(5.2));  // Assign j an int value from 0 to 5

println(f, i, j);