package blocks.catalog.translate;

import rx.Observable;
import rx.Observer;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;
import rx.observers.EmptyObserver;
import rx.subjects.PublishSubject;
import blocks.core.Block;
import blocks.util.Language;

public class TranslateBlock extends Block {

	@Override
	protected void initialize() {
		com.memetix.mst.translate.Translate
				.setClientId("ef5e1fb2-a2f9-481c-a930-3edbef48ff62");
		com.memetix.mst.translate.Translate
				.setClientSecret("gYQVLfkZhSTgWT1QNslRKJEVEh/dc66Ly9/piHkAbFs=");

		inSourceLanguage = new EmptyObserver<Language>() {
			public void onNext(Language t) {
				sourceLanguage = t;
			}
		};

		inDestLanguage = new EmptyObserver<Language>() {
			public void onNext(Language t) {
				destLanguage = t;
			}
		};

		in = PublishSubject.create();

		out = in.map(new Func1<String, String>() {
			public String call(String input) {

				com.memetix.mst.language.Language source = com.memetix.mst.language.Language
						.fromString(sourceLanguage.getPrimaryLanguageCode());
				com.memetix.mst.language.Language destination = com.memetix.mst.language.Language
						.fromString(destLanguage.getPrimaryLanguageCode());
				String output;
				try {
					output = com.memetix.mst.translate.Translate.execute(input,
							source, destination);
				} catch (Exception e) {
					throw OnErrorThrowable.from(e);
				}

				return output;
			}
		});
	}

	public Observer<Language> getInDestLanguage() {
		return inDestLanguage;
	}

	public Observer<Language> getInSourceLanguage() {
		return inSourceLanguage;
	}

	public PublishSubject<String> getIn() {
		return in;
	}

	public Observable<String> getOut() {
		return out;
	}

	private Observer<Language> inSourceLanguage;
	private Observer<Language> inDestLanguage;

	private PublishSubject<String> in;
	private Observable<String> out;

	private Language sourceLanguage = Language.FRENCH;
	private Language destLanguage = Language.ENGLISH_US;

}
