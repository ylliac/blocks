package blocks.demo.presentation;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import blocks.core.BlockSupport;

public class RandomSource extends BlockSupport {

	@Override
	protected void initialize() {
		out = Observable.interval(50, TimeUnit.MILLISECONDS).map(
				new Func1<Long, Float>() {

					@Override
					public Float call(Long t1) {
						return (float) (random.nextGaussian() * 100 / 3);
					}

				});
	}

	public Observable<Float> getBufferedOut() {
		return out.buffer(1, TimeUnit.SECONDS).map(
				new Func1<List<Float>, Float>() {

					@Override
					public Float call(List<Float> values) {
						float result = 0;
						if (!values.isEmpty()) {
							for (float value : values) {
								result += value;
							}
							result /= values.size();
						}
						return result;
					}
				});
	}

	public Observable<Float> getOut() {
		return out;
	}

	private Observable<Float> out;

	private final Random random = new Random();

}
