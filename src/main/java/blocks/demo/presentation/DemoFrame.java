package blocks.demo.presentation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Random;

import javax.swing.AbstractButton;
import javax.swing.JToggleButton;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.observables.SwingObservable;
import rx.observers.EmptyObserver;
import rx.schedulers.Schedulers;
import rx.schedulers.SwingScheduler;
import rx.subjects.PublishSubject;

public class DemoFrame extends ApplicationFrame {

	public DemoFrame(String title) {
		super(title);

		// CHART
		dataset = new DynamicTimeSeriesCollection(1, 2 * 60, new Second());
		dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));
		dataset.addSeries(gaussianData(), 0, "Gaussian data");
		in = PublishSubject.create();
		in.observeOn(SwingScheduler.getInstance()).subscribe(
				new EmptyObserver<Float>() {
					@Override
					public void onNext(Float newData) {
						float[] array = new float[1];
						array[0] = newData;
						dataset.advanceTime();
						dataset.appendData(array);
					}
				});
		JFreeChart chart = createChart(dataset);
		this.add(new ChartPanel(chart), BorderLayout.CENTER);

		// PLAY / PAUSE BUTTON
		playPauseButton = new JToggleButton("PLAY / PAUSE");
		outPlay = SwingObservable
				.fromButtonAction(playPauseButton)
				.map(new Func1<ActionEvent, Boolean>() {

					@Override
					public Boolean call(ActionEvent actionEvent) {
						AbstractButton abstractButton = (AbstractButton) actionEvent
								.getSource();
						boolean isSelected = abstractButton.getModel()
								.isSelected();
						return isSelected;
					}
				}).subscribeOn(SwingScheduler.getInstance())
				.observeOn(Schedulers.computation());
		this.add(playPauseButton, BorderLayout.SOUTH);

		pack();
		RefineryUtilities.centerFrameOnScreen(this);
	}

	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart(
				"Dynamic series", "hh:mm:ss", "milliVolts", dataset, true,
				true, false);
		final XYPlot plot = result.getXYPlot();
		ValueAxis domain = plot.getDomainAxis();
		domain.setAutoRange(true);
		ValueAxis range = plot.getRangeAxis();
		range.setRange(-100, 100);
		return result;
	}

	private float[] gaussianData() {
		Random random = new Random();
		float[] a = new float[2 * 60];
		for (int i = 0; i < a.length; i++) {
			a[i] = (float) (random.nextGaussian() * 100 / 3);
		}
		return a;
	}

	public Observer<Float> getIn() {
		return in;
	}

	public Observable<Boolean> getOutPlay() {
		return outPlay;
	}

	private PublishSubject<Float> in;

	private Observable<Boolean> outPlay;

	private final DynamicTimeSeriesCollection dataset;
	private JToggleButton playPauseButton;

	/**
	 * UID.
	 */
	private static final long serialVersionUID = 5511255292137552252L;

}
