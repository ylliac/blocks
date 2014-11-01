package blocks.catalog.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Subscription;
import rx.exceptions.OnErrorThrowable;
import rx.observers.EmptyObserver;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import blocks.core.BlockSupport;

public class GateBlock<T> extends BlockSupport {

	@Override
	protected void initialize() {

		queue = new LinkedBlockingQueue<>();

		out = PublishSubject.create();

		in = PublishSubject.create();
		// Always add values in a queue
		in.subscribe(new EmptyObserver<T>() {
			@Override
			public void onNext(T value) {
				try {
					queue.put(value);
				} catch (InterruptedException e) {
					throw OnErrorThrowable.from(e);
				}
			}
		});

		final Observable<T> queueObservable = QueueObservables.from(queue);

		// Connect or not
		inSwitch = PublishSubject.create();
		inSwitch.subscribe(new EmptyObserver<Boolean>() {
			@Override
			public void onNext(Boolean value) {
				if (value && subscription == null) {
					subscription = queueObservable.subscribeOn(
							Schedulers.newThread()).subscribe(out);
				} else if (!value && subscription != null) {
					subscription.unsubscribe();
					subscription = null;
				}
			}
		});
	}

	public PublishSubject<T> getIn() {
		return in;
	}

	public PublishSubject<T> getOut() {
		return out;
	}

	public PublishSubject<Boolean> getInSwitch() {
		return inSwitch;
	}

	private PublishSubject<Boolean> inSwitch;

	private PublishSubject<T> in;
	private PublishSubject<T> out;

	private Subscription subscription;

	private BlockingQueue<T> queue;

}
