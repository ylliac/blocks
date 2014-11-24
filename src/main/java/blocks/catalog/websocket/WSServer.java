package blocks.catalog.websocket;

import java.net.InetSocketAddress;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import rx.Observable;
import rx.subjects.PublishSubject;

public class WSServer extends WebSocketServer {

	public WSServer(int port) {
		this(new InetSocketAddress(port));
	}

	public WSServer(InetSocketAddress address) {
		super(address);
		out = PublishSubject.create();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		out.onNext(message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		out.onError(ex);
	}

	public void sendToAll(String text) {
		Collection<WebSocket> con = connections();
		synchronized (con) {
			for (WebSocket c : con) {
				c.send(text);
			}
		}
	}

	public Observable<String> getOut() {
		return out;
	}

	private PublishSubject<String> out;

}
