package blocks.demo.presentation;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.SwingScheduler;
import blocks.catalog.console.ConsoleBlock;
import blocks.catalog.messaging.MessageEmitter;
import blocks.catalog.messaging.MessageReceiver;
import blocks.catalog.queue.GateBlock;
import blocks.catalog.websocket.WebSocketServerBlock;

public class DemoPresentation {

	public static void main(String[] args) {
		DemoPresentation demo = new DemoPresentation();
		// demo.sourceToGraph();
		// demo.sourceToThrottleToGraph();
		// demo.sourceToBufferToGraph();
		// demo.sourceToThrottleToQueueToGraph();
		// demo.sourceToThrottleTo0MQToGraph();
		// demo.sourceToWebSocketToHTML();
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
		source.getOut()// .throttleFirst(50, TimeUnit.MILLISECONDS)
				.subscribe(emitterSlow.getIn());

		WebSocketServerBlock webSocket = new WebSocketServerBlock();
		webSocket.setAdress(8887);
		source.getOut()// .throttleFirst(50, TimeUnit.MILLISECONDS)
				.subscribe(webSocket.getIn());

		DemoFrame frameQuick = new DemoFrame("Test");
		MessageReceiver<Float> receiver = new MessageReceiver<>();
		receiver.setConnectionString("tcp://localhost:5557");
		receiver.getOut().throttleFirst(150, TimeUnit.MILLISECONDS)
				.subscribe(frameQuick.getIn());
		frameQuick.setVisible(true);
	}

	// Source simple affichage simple
	// SOURCE --> GRAPH
	public void sourceToGraph() {
		RandomSource source = new RandomSource();
		ConsoleBlock console = new ConsoleBlock();
		DemoFrame frame = new DemoFrame("Test");

		// source.getOut().subscribeOn(Schedulers.computation())
		// .subscribe(console.getInMessage());
		source.getOut().observeOn(SwingScheduler.getInstance())
				.subscribe(frame.getIn());

		frame.setVisible(true);
	}

	// Un peu rapide ? on sous echantillonne en ignorant
	// SOURCE --> THROTTLE --> GRAPH
	public void sourceToThrottleToGraph() {
		RandomSource source = new RandomSource();
		DemoFrame frame = new DemoFrame("Test");

		source.getOut().throttleFirst(50, TimeUnit.MILLISECONDS)
				.observeOn(SwingScheduler.getInstance())
				.subscribe(frame.getIn());

		frame.setVisible(true);
	}

	// Un peu simpliste ? on sous echantillonne en moyennant
	// SOURCE --> BUFFER --> GRAPH
	public void sourceToBufferToGraph() {
		RandomSource source = new RandomSource();
		ConsoleBlock console = new ConsoleBlock();
		DemoFrame frame = new DemoFrame("Test");

		// Buffer every second
		Observable<Float> bufferedSource = source.getOut()
				.buffer(50, TimeUnit.MILLISECONDS)
				.map(new Func1<List<Float>, Float>() {

					@Override
					public Float call(List<Float> values) {
						float result = 0;
						if (!values.isEmpty()) {
							for (float value : values) {
								result += value;
							}
							result /= values.size();
						}
						return result;
					}
				});

		bufferedSource.subscribeOn(Schedulers.computation()).subscribe(
				console.getInMessage());
		bufferedSource.observeOn(SwingScheduler.getInstance()).subscribe(
				frame.getIn());

		frame.setVisible(true);

	}

	// TODO Ca manque d'intéraction ? Ajouter un bouton qui met en pause les
	// données
	// SOURCE --> THROTTLE --> QUEUE --> GRAPH
	public void sourceToThrottleToQueueToGraph() {
		RandomSource source = new RandomSource();
		DemoFrame frame = new DemoFrame("Test");
		GateBlock<Float> gate = new GateBlock<>();

		source.getOut().throttleFirst(50, TimeUnit.MILLISECONDS)
				.subscribe(gate.getIn());
		gate.getOut().subscribe(frame.getIn());
		frame.getOutPlay().subscribe(gate.getInSwitch());

		frame.setVisible(true);
	}

	// TODO On stocke les données en RAM en attendant ? Ecrire et lire dans un
	// channel pour "mettre en cache" les données
	// SOURCE --> THROTTLE --> QUEUE --> GRAPH
	public void sourceToThrottleToFileToGraph() {
		// TODO
	}

	// TODO Utiliser jeromq pour dialoguer à distance
	// SOURCE --> THROTTLE --> TCP --> QUEUE --> GRAPH
	public void sourceToThrottleTo0MQToGraph() {
		RandomSource source = new RandomSource();
		DemoFrame frame = new DemoFrame("Test");
		GateBlock<Float> gate = new GateBlock<>();

		MessageEmitter<Float> emitter = new MessageEmitter<>();
		emitter.setConnectionString("tcp://*:5556");
		MessageReceiver<Float> receiver = new MessageReceiver<>();
		receiver.setConnectionString("tcp://localhost:5556");

		source.getOut().throttleFirst(50, TimeUnit.MILLISECONDS)
				.subscribe(emitter.getIn());
		receiver.getOut().subscribe(gate.getIn());
		gate.getOut().subscribe(frame.getIn());
		frame.getOutPlay().subscribe(gate.getInSwitch());

		frame.setVisible(true);
	}

	// TODO Ou utiliser une WebSocket pour afficher les résultats dans une page
	// Web
	// SOURCE --> THROTTLE --> WEBSOCKET --> HTML
	public void sourceToWebSocketToHTML() {
		RandomSource source = new RandomSource();
		WebSocketServerBlock webSocket = new WebSocketServerBlock();
		webSocket.setAdress(8887);

		source.getOut().subscribe(webSocket.getIn());
	}

	// TODO Ca commence à faire un peu fouilli, on pourrait pas utiliser un DSL
	// ?
	public void dsl() {
		// TODO
	}

	// TODO Mais c'est mieux avec les schemas
	public void schemaToDsl() {
		// TODO
	}

}
