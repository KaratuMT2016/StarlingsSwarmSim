void draw(){
   size(240, 240);
float[] sineWave = new float[width];
    
for (int i = 0; i < sineWave.length; i++) { 
  // Fill the array with values from sin() 
  float r = map(i, 0, width, 0, TWO_PI);
  sineWave[i] = abs(sin(r)); 
  println(sineWave[i]);
}

 
for (int i = 0; i < sineWave.length; i++) { 
  // Set the stroke values to numbers read from the array 
  stroke(sineWave[i] * 255); 
  line(i, 0, i, height); 
}

}