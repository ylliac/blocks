package blocks.catalog.speechrecognition;

import java.io.IOException;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import blocks.core.BlockSupport;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class SpeechRecognitionOfflineBlock extends BlockSupport {

	@Override
	protected void initialize() {

		out = Observable.create(new OnSubscribe<String>() {
			public void call(Subscriber<? super String> subscriber) {
				Configuration configuration = new Configuration();

				// Set path to acoustic model.
				configuration
						.setAcousticModelPath("file:conf/speech/acoustic/lium_french_f0");
				// Set path to dictionary.
				configuration
						.setDictionaryPath("file:conf/speech/dic/frenchWords62K.dic");
				// Set Grammar
				configuration.setGrammarPath("file:conf/speech/grammar");
				configuration.setGrammarName("language");
				configuration.setUseGrammar(true);

				// ACY : Si on veut utiliser un modèle de language au lieu d'une
				// grammaire, utiliser ce code (avertissement : ca marche pas
				// très bien)
				// configuration.setLanguageModelPath("file:conf/speech/model/french3g62K.lm.dmp");
				// configuration.setUseGrammar(false);

				try {
					LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(
							configuration);

					// Start recognition process pruning previously cached data.
					recognizer.startRecognition(true);

					SpeechResult result;
					while ((result = recognizer.getResult()) != null) {
						// Send utterance string without filler words.
						String hypothesis = result.getHypothesis();
						subscriber.onNext(hypothesis);
					}

					// Pause recognition process. It can be resumed then with
					// startRecognition(false).
					recognizer.stopRecognition();

					subscriber.onCompleted();
				} catch (IOException e) {
					subscriber.onError(e);
				}
			}
		}).observeOn(Schedulers.newThread());
	}

	public Observable<String> getOut() {
		return out;
	}

	private Observable<String> out;

}
