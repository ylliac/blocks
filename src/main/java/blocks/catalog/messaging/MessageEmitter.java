package blocks.catalog.messaging;

import org.zeromq.ZMQ;

import rx.observers.EmptyObserver;
import rx.subjects.PublishSubject;
import blocks.core.BlockSupport;
import blocks.util.SerializationUtils;

public class MessageEmitter<T> extends BlockSupport {

	@Override
	protected void initialize() {
		in = PublishSubject.create();
	}

	public void setConnectionString(String connectionString) {

		ZMQ.Context context = ZMQ.context(1);

		final ZMQ.Socket publisher = context.socket(ZMQ.PUB);
		publisher.bind(connectionString);

		in.subscribe(new EmptyObserver<T>() {
			@Override
			public void onNext(T value) {
				byte[] data = SerializationUtils.serialize(value);
				publisher.send(data, 0);
			}
		});
	}

	public PublishSubject<T> getIn() {
		return in;
	}

	private PublishSubject<T> in;

}
