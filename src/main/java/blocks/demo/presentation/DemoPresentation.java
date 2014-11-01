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

public class DemoPresentation {

	public static void main(String[] args) {
		DemoPresentation demo = new DemoPresentation();
		// demo.sourceToGraph();
		// demo.sourceToThrottleToGraph();
		// demo.sourceToBufferToGraph();
		// demo.sourceToThrottleToQueueToGraph();
		demo.sourceToThrottleTo0MQToGraph();
	}

	// Source simple affichage simple
	// SOURCE --> GRAPH
	public void sourceToGraph() {
		RandomSource source = new RandomSource();
		ConsoleBlock console = new ConsoleBlock();
		DemoFrame frame = new DemoFrame("Test");

		source.getOut().subscribeOn(Schedulers.computation())
				.subscribe(console.getInMessage());
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

	// TODO Ca manque d'int�raction ? Ajouter un bouton qui met en pause les
	// donn�es
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

	// TODO On stocke les donn�es en RAM en attendant ? Ecrire et lire dans un
	// channel pour "mettre en cache" les donn�es
	// SOURCE --> THROTTLE --> QUEUE --> GRAPH
	public void sourceToThrottleToFileToGraph() {
		// TODO
	}

	// TODO Utiliser jeromq pour dialoguer � distance
	public void sourceToThrottleTo0MQToGraph() {
		RandomSource source = new RandomSource();
		DemoFrame frame = new DemoFrame("Test");

		MessageEmitter<Float> emitter = new MessageEmitter<>();
		MessageReceiver<Float> receiver = new MessageReceiver<>();

		source.getOut().throttleFirst(50, TimeUnit.MILLISECONDS)
				.subscribe(emitter.getIn());
		receiver.getOut().subscribe(frame.getIn());

		frame.setVisible(true);
	}

	// TODO Ca commence � faire un peu fouilli, on pourrait pas utiliser un DSL
	// ?
	public void dsl() {
		// TODO
	}

	// TODO Mais c'est mieux avec les schemas
	public void schemaToDsl() {
		// TODO
	}

}
