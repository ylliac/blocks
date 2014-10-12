package blocks.demo.presentation;

import java.awt.BorderLayout;
import java.util.Random;

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

import rx.Observer;
import rx.observers.EmptyObserver;

public class DemoFrame extends ApplicationFrame {

	public DemoFrame(String title) {
		super(title);

		in = new EmptyObserver<Float>() {
			@Override
			public void onNext(Float newData) {
				float[] array = new float[1];
				array[0] = newData;
				dataset.advanceTime();
				dataset.appendData(array);
			}
		};

		dataset = new DynamicTimeSeriesCollection(1, 2 * 60, new Second());
		dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));
		dataset.addSeries(gaussianData(), 0, "Gaussian data");

		JFreeChart chart = createChart(dataset);
		this.add(new ChartPanel(chart), BorderLayout.CENTER);

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

	private Observer<Float> in;

	private final DynamicTimeSeriesCollection dataset;

	/**
	 * UID.
	 */
	private static final long serialVersionUID = 5511255292137552252L;

}
