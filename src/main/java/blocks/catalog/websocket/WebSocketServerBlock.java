package blocks.catalog.websocket;

import java.util.Objects;

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

		in.subscribe(new EmptyObserver<Object>() {
			@Override
			public void onNext(Object data) {
				server.sendToAll(Objects.toString(data));
			}
		});

		server.getOut().subscribe(out);
	}

	public PublishSubject<Object> getIn() {
		return in;
	}

	public Observable<String> getOut() {
		return out;
	}

	private PublishSubject<Object> in;

	private PublishSubject<String> out;

	private WSServer server = null;
}
