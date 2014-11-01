package blocks.catalog.messaging;

import org.zeromq.ZMQ;

import rx.observers.EmptyObserver;
import rx.subjects.PublishSubject;
import blocks.core.BlockSupport;
import blocks.util.SerializationUtils;

public class MessageEmitter<T> extends BlockSupport {

	@Override
	protected void initialize() {
		ZMQ.Context context = ZMQ.context(1);

		final ZMQ.Socket publisher = context.socket(ZMQ.PUB);
		publisher.bind("tcp://*:5556");

		in = PublishSubject.create();
		in.subscribe(new EmptyObserver<T>() {
			@Override
			public void onNext(T value) {
				byte[] data = SerializationUtils.serialize(value);
				System.out.println("SEND " + value);
				publisher.send(data, 0);
			}
		});
	}

	public PublishSubject<T> getIn() {
		return in;
	}

	private PublishSubject<T> in;

}
