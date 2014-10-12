package blocks.demo.presentation;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.exceptions.OnErrorFailedException;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import blocks.core.BlockSupport;

public class DataReader extends BlockSupport {

	@Override
	protected void initialize() {
		in = PublishSubject.create();

		out = in.map(new Func1<File, List<Float>>() {

			@Override
			public List<Float> call(File file) {
				List<Float> result = new ArrayList<Float>();

				try (FileInputStream fileInput = new FileInputStream(file);
						DataInputStream dataInput = new DataInputStream(
								fileInput);) {
					boolean eof = false;
					while (!eof) {
						try {
							float value = dataInput.readFloat();
							result.add(value);
						} catch (EOFException e) {
							eof = true;
						}
					}
				} catch (FileNotFoundException e) {
					throw new OnErrorFailedException(e);
				} catch (IOException e) {
					throw new OnErrorFailedException(e);
				}

				return result;
			}

		}).flatMap(new Func1<List<Float>, Observable<Float>>() {

			@Override
			public Observable<Float> call(List<Float> collection) {
				return Observable.from(collection);
			}
		});
	}

	public Observer<File> getIn() {
		return in;
	}

	public Observable<Float> getOut() {
		return out;
	}

	private PublishSubject<File> in;

	private Observable<Float> out;

}
