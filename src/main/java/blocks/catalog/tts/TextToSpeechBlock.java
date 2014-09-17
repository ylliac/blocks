package blocks.catalog.tts;

import javazoom.jl.player.Player;
import rx.Observer;
import rx.exceptions.OnErrorThrowable;
import rx.observers.EmptyObserver;
import blocks.core.Block;
import blocks.util.Language;

public class TextToSpeechBlock extends Block {

	@Override
	protected void initialize() {
		synthesizer = new GoogleTextToSpeech();
		synthesizer.setLanguageCode(Language.ENGLISH_US.getLanguageCode());
		
		inLanguage = new EmptyObserver<Language>() {
			@Override
			public void onNext(Language lang) {
				synthesizer.setLanguageCode(lang.getLanguageCode());
			}
		};

		in = new EmptyObserver<String>() {
			@Override
			public void onNext(String message) {
				try {
					Player player = new Player(synthesizer.getMP3Data(message));
					player.play();
				} catch (Exception e) {
					OnErrorThrowable.from(e);
				}
			}
		};

	}

	public Observer<Language> getInLanguage() {
		return inLanguage;
	}

	public Observer<String> getIn() {
		return in;
	}

	private Observer<Language> inLanguage;
	private Observer<String> in;

	private GoogleTextToSpeech synthesizer;
}
