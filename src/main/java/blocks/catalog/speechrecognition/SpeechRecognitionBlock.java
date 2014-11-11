package blocks.catalog.speechrecognition;

import java.io.IOException;

import blocks.core.BlockSupport;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class SpeechRecognitionBlock extends BlockSupport {

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub

		Configuration configuration = new Configuration();

		// Set path to acoustic model.
		configuration
				.setAcousticModelPath("file:conf/speech/acoustic/lium_french_f0");
		// Set path to dictionary.
		configuration
				.setDictionaryPath("file:conf/speech/dic/frenchWords62K.dic");
		// Set language model
		// TODO ACY Laisser en commentaire qq part
		// configuration
		// .setLanguageModelPath("file:conf/speech/model/french3g62K.lm.dmp");
		configuration.setGrammarPath("file:conf/speech/grammar");
		configuration.setGrammarName("language");
		configuration.setUseGrammar(true);

		try {
			LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(
					configuration);

			// Start recognition process pruning previously cached data.
			recognizer.startRecognition(true);

			System.out.println("START");

			SpeechResult result;
			while ((result = recognizer.getResult()) != null) {
				// Print utterance string without filler words.
				System.out.println(result.getHypothesis());
			}

			System.out.println("STOP");

			// Pause recognition process. It can be resumed then with
			// startRecognition(false).
			recognizer.stopRecognition();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
