package blocks.catalog.websocket;

import rx.Observable;
import rx.observers.EmptyObserver;
import rx.subjects.PublishSubject;
import blocks.core.BlockSupport;

public class WebSocketServerBlock extends BlockSupport {

	@Override
	protected void initialize() {
		in = PublishSubject.create();
		out = PublishSubject.create();
	}

	public void setAdress(int port) {
		server = new WSServer(port);
		server.start();

		in.subscribe(new EmptyObserver<String>() {
			@Override
			public void onNext(String data) {
				server.sendToAll(data);
			}
		});

		server.getOut().subscribe(out);
	}

	public PublishSubject<String> getIn() {
		return in;
	}

	public Observable<String> getOut() {
		return out;
	}

	private PublishSubject<String> in;

	private PublishSubject<String> out;

	private WSServer server = null;
}
