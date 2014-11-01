package blocks.catalog.queue;

import java.util.concurrent.BlockingQueue;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

public class QueueObservables {

	public static <T> Observable<T> from(final BlockingQueue<T> queue) {
		return Observable.create(new OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> o) {

				for (;;) {
					try {
						T value = queue.take();
						o.onNext(value);
					} catch (InterruptedException e) {
						// DO NOTHING
					}

					if (o.isUnsubscribed()) {
						return;
					}
				}
			}
		});
	}

}
