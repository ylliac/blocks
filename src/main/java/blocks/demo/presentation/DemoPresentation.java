package blocks.demo.presentation;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import test.SwingScheduler;
import blocks.catalog.console.ConsoleBlock;
import blocks.catalog.filewatcher.FileWatcherBlock;

public class DemoPresentation {

	public static void main(String[] args) {
		DemoPresentation demo = new DemoPresentation();
		// demo.sourceToGraph();
		// demo.sourceToBufferToGraph();
		demo.bufferedSourceToFileToGraph();
	}

	// SOURCE --> GRAPH
	public void sourceToGraph() {
		RandomSource source = new RandomSource();
		ConsoleBlock console = new ConsoleBlock();
		DemoFrame frame = new DemoFrame("Test");

		source.getOut().subscribe(console.getInMessage());
		source.getOut().observeOn(SwingScheduler.getInstance())
				.subscribe(frame.getIn());

		frame.setVisible(true);
	}

	// SOURCE --> BUFFER --> GRAPH
	public void sourceToBufferToGraph() {
		RandomSource source = new RandomSource();
		ConsoleBlock console = new ConsoleBlock();
		DemoFrame frame = new DemoFrame("Test");

		// Buffer every second
		Observable<Float> bufferedSource = source.getOut()
				.buffer(1, TimeUnit.SECONDS)
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

		bufferedSource.subscribe(console.getInMessage());
		bufferedSource.observeOn(SwingScheduler.getInstance()).subscribe(
				frame.getIn());

		frame.setVisible(true);

	}

	// BUFFERED SOURCE --> FILE --> GRAPH
	public void bufferedSourceToFileToGraph() {
		RandomSource source = new RandomSource();
		DemoFrame frame = new DemoFrame("Test");
		DataWriter dataWriter = new DataWriter();
		FileWatcherBlock fileWatcher = new FileWatcherBlock();
		DataReader dataReader = new DataReader();

		source.getBufferedOut().buffer(3).subscribe(dataWriter.getIn());
		dataReader.getOut().observeOn(SwingScheduler.getInstance())
				.subscribe(frame.getIn());
		fileWatcher.getOut().subscribeOn(Schedulers.io())
				.subscribe(dataReader.getIn());

		frame.setVisible(true);

	}
}
