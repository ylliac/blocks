package blocks.catalog.messaging;

import org.zeromq.ZMQ;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;
import blocks.util.SerializationUtils;

public class MessageObservables {

	public static <T> Observable<T> from(final ZMQ.Socket socket) {
		return Observable.create(new OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> o) {

				for (;;) {
					try {
						T value = (T) SerializationUtils.deserialize(socket
								.recv());
						o.onNext(value);
					} catch (ClassCastException e) {
						o.onError(OnErrorThrowable.from(e));
					} catch (ClassNotFoundException e) {
						o.onError(OnErrorThrowable.from(e));
					}

					if (o.isUnsubscribed()) {
						return;
					}
				}
			}
		});
	}

}
