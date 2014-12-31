package blocks.demo.properties;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import blocks.core.BlockSupport;

public class SearchBlock extends BlockSupport {

	@Override
	protected void initialize() {
		in = PublishSubject.create();

		out = in.map(new Func1<String, List<String>>() {

			@Override
			public List<String> call(String value) {
				List<String> result = new ArrayList<>();

				for (int i = 0; i < 10; i++) {
					result.add(value + i);
				}

				return result;
			}
		});
	}

	public PublishSubject<String> getIn() {
		return in;
	}

	public Observable<List<String>> getOut() {
		return out;
	}

	private PublishSubject<String> in;
	private Observable<List<String>> out;

}
