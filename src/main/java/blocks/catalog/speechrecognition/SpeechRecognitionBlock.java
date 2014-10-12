package blocks.catalog.speechrecognition;

import blocks.core.BlockSupport;

import com.getflourish.stt.STT;
import com.getflourish.stt.TranscribeListener;

public class SpeechRecognitionBlock extends BlockSupport {

	// Implemented with :
	// STT: http://stt.getflourish.com/
	// Minim-java: https://github.com/casmi/minim-java
	// (which was inspired by Minim: http://code.compartmental.net/tools/minim/)

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub

		TranscribeListener listener = new TranscribeListener() {
			
			public void transcribe(String utterance, float confidence, int status) {
				// TODO Auto-generated method stub
				System.out.println("STT : " + utterance);
			}
		};
		
		speechToText = new STT(listener);
		speechToText.setLanguage("fr-FR"); // TODO ACY Paramétrer
		speechToText.begin();
	}

	private STT speechToText;

}
