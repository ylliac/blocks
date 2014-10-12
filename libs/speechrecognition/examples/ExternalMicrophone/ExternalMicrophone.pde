/*
// Using External Microphones with STT
// www.getflourish.com/sst/
//
// Florian Schulz 2013
*/
 
import com.getflourish.stt.*;
import ddf.minim.*;
import javax.sound.sampled.*;
 
STT stt;
String result;
String key = "Your API Key";
 
void setup ()
{
  size(600, 200);
  // Init STT automatically starts listening
  stt = new STT(this, key);
  stt.enableDebug();
  stt.setLanguage("en"); 
  
  // Display available Inputs with id and name
  Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
 
  for(int i = 0; i < mixerInfo.length; i++)
  {
    println("### " + i + ": " + mixerInfo[i].getName());
  }  
  
  // Set input (e.g. 0)  
  Mixer mixer = AudioSystem.getMixer(mixerInfo[0]);
  
  // Update the Minim instance that STT is using
  Minim minim = stt.getMinimInstance();
  minim.setInputMixer(mixer);
  println("### Source set to: " + mixerInfo[0]);
 
}
 
// Method is called if transcription was successfull 
void transcribe (String utterance, float confidence) 
{
  println(utterance);
  result = utterance;
}
public void keyPressed () {
  stt.begin();
}
public void keyReleased () {
  stt.end();
}
 
void draw ()
{
  background(0);
}
