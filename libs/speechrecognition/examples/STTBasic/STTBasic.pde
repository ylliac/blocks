/*
// This is a basic example to demonstrate how the Speech-To-Text Library 
// can be used. See http://stt.getflourish.com for more information on
// available settings.
//
// Florian Schulz 2011, www.getflourish.com
*/

import com.getflourish.stt.*;

STT stt;
String result;
String key = "Your API Key";

void setup ()
{
  size(600, 200);
  // Init STT with default manual record mode

  stt = new STT(this, key);
  stt.enableDebug();
  stt.setLanguage("en"); 
  
  // Some text to display the result
  textFont(createFont("Arial", 24));
  result = "Say something!";
}

void draw ()
{
  background(0);
  text(result, mouseX, mouseY);
}

// Method is called if transcription was successfull 
void transcribe (String utterance, float confidence) 
{
  println(utterance);
  result = utterance;
}

// Use any key to begin and end a record
public void keyPressed () {
  stt.begin();
}
public void keyReleased () {
  stt.end();
}
