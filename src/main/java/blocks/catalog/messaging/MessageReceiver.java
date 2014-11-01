package blocks.catalog.messaging;

import org.zeromq.ZMQ;

import rx.Observable;
import blocks.core.BlockSupport;

public class MessageReceiver<T> extends BlockSupport {

	@Override
	protected void initialize() {
		ZMQ.Context context = ZMQ.context(1);

		ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
		subscriber.connect("tcp://localhost:5556");

		out = MessageObservables.from(subscriber);
	}

	public Observable<T> getOut() {
		return out;
	}

	private Observable<T> out;

}
