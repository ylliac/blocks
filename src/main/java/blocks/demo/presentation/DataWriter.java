package blocks.demo.presentation;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import rx.Observer;
import rx.exceptions.OnErrorFailedException;
import rx.observers.EmptyObserver;
import blocks.core.BlockSupport;

public class DataWriter extends BlockSupport {

	private static int counter = 1;

	@Override
	protected void initialize() {
		in = new EmptyObserver<List<Float>>() {
			@Override
			public void onNext(List<Float> values) {
				try (FileOutputStream fileOutput = new FileOutputStream(
						"data/samples" + counter++);
						DataOutputStream dataOutput = new DataOutputStream(
								fileOutput);) {
					for (float value : values) {
						dataOutput.writeFloat(value);
					}
				} catch (FileNotFoundException e) {
					throw new OnErrorFailedException(e);
				} catch (IOException e) {
					throw new OnErrorFailedException(e);
				}
			}
		};
	}

	public Observer<List<Float>> getIn() {
		return in;
	}

	private Observer<List<Float>> in;

}
