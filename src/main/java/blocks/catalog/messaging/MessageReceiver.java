package blocks.catalog.messaging;

import org.zeromq.ZMQ;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import blocks.core.BlockSupport;

public class MessageReceiver<T> extends BlockSupport {

	@Override
	protected void initialize() {
		out = PublishSubject.create();
	}

	public void setConnectionString(String connectionString) {
		ZMQ.Context context = ZMQ.context(1);

		ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
		subscriber.subscribe(new byte[0]);
		subscriber.connect(connectionString);

		Observable<T> observable = (Observable<T>) MessageObservables.from(
				subscriber).subscribeOn(Schedulers.newThread());
		observable.subscribe(out);
	}

	public Observable<T> getOut() {
		return out;
	}

	private PublishSubject<T> out;

}
