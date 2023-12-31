int[] x = new int[400];  // Array to store x-coordinates
int count = 0;  // Positions stored in the array

void setup() {
  size(400, 100); 
}

void draw() {
  x[count] = mouseX;        // Assign new x-coordinate to the array
  count++;                  // Increment the counter 
  if (count == x.length) {  // If the x array is full,
    x = expand(x);          // double the size of x
    println(x.length);      // Write the new size to the console
  }
}