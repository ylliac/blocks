package blocks.catalog.tts;

import rx.Observer;
import rx.observers.EmptyObserver;
import t2s.son.LecteurTexte;
import blocks.core.BlockSupport;

public class TextToSpeechOfflineBlock extends BlockSupport {

	@Override
	protected void initialize() {

		synthesizer = new LecteurTexte();

		in = new EmptyObserver<String>() {
			@Override
			public void onNext(String message) {
				synthesizer.setTexte(message);
				synthesizer.playAll();
			}
		};

	}

	public Observer<String> getIn() {
		return in;
	}

	private Observer<String> in;

	private LecteurTexte synthesizer;
}
