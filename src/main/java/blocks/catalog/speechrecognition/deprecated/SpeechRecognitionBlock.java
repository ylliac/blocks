package blocks.catalog.speechrecognition.deprecated;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.observers.EmptyObserver;
import rx.subjects.PublishSubject;
import blocks.core.BlockSupport;
import blocks.util.Language;

public class SpeechRecognitionBlock extends BlockSupport {

	@Override
	protected void initialize() {
		recognizer = new GoogleSpeechRecognizer();
		recognizer.setProfanityFilter(false);
		recognizer.setLanguage(Language.ENGLISH_US);

		inLanguage = new EmptyObserver<Language>() {
			@Override
			public void onNext(Language lang) {
				recognizer.setLanguage(lang);
			}
		};

		in = PublishSubject.create();

		out = in.map(new Func1<byte[], String>() {
			public String call(byte[] input) {

				//TODO ACY
				System.out.println("speech");
				
				
				SpeechToTextResult s2tResult = recognizer.speechToText(input);
				
				//TODO ACY
				System.out.println("s2t : " + s2tResult.getResult());
				
				return s2tResult.getResult();
			}
		});
	}

	public Observer<Language> getInLanguage() {
		return inLanguage;
	}

	public PublishSubject<byte[]> getIn() {
		return in;
	}

	public Observable<String> getOut() {
		return out;
	}

	private Observer<Language> inLanguage;
	private PublishSubject<byte[]> in;

	private Observable<String> out;

	private GoogleSpeechRecognizer recognizer;

}
