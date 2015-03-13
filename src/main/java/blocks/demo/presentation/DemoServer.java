package blocks.demo.presentation;

import java.util.concurrent.TimeUnit;

import blocks.catalog.messaging.MessageEmitter;
import blocks.catalog.websocket.WebSocketServerBlock;

public class DemoServer {

	public static void main(String[] args) {
		DemoServer demo = new DemoServer();
		demo.demo();
	}

	// Démo réelle
	public void demo() {
		RandomSource source = new RandomSource();

		MessageEmitter<Float> emitterQuick = new MessageEmitter<>();
		emitterQuick.setConnectionString("tcp://*:5556");
		source.getOut().subscribe(emitterQuick.getIn());

		MessageEmitter<Float> emitterSlow = new MessageEmitter<>();
		emitterSlow.setConnectionString("tcp://*:5557");
		source.getOut().throttleFirst(150, TimeUnit.MILLISECONDS)
				.subscribe(emitterSlow.getIn());

		WebSocketServerBlock webSocket = new WebSocketServerBlock();
		webSocket.setAdress(8887);
		source.getOut().subscribe(webSocket.getIn());
	}

}
