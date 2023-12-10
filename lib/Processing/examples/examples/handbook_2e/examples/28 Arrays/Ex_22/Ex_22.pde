float[] data = { 19.0, 40.0, 75.0, 76.0, 90.0 };
 
void setup() {
  halvey(data);
  println(data[0]);  // Prints "9.5"
  println(data[1]);  // Prints "20.0"
  println(data[2]);  // Prints "37.5"
  println(data[3]);  // Prints "38.0"
  println(data[4]);  // Prints "45.0"
}
 
void halvey(float[] e) {
  for (int i = 0; i < e.length; i++) {  // For each array element,
    e[i] = e[i] / 2.0;                  // divide the value by 2
  }
}