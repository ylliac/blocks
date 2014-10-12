package blocks.catalog.filewatcher;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

import rx.Observable;
import rx.functions.Func1;
import blocks.core.BlockSupport;

import com.github.davidmoten.rx.FileObservable;

public class FileWatcherBlock extends BlockSupport {

	private static String DATA_FOLDER = "data/";

	@Override
	protected void initialize() {

		out = FileObservable.from(new File(DATA_FOLDER),
				StandardWatchEventKinds.ENTRY_CREATE).map(
				new Func1<WatchEvent<?>, File>() {

					@Override
					public File call(WatchEvent<?> watchEvent) {
						// A new Path was created
						Path newPath = (Path) watchEvent.context();
						// Output
						return new File(DATA_FOLDER + newPath.toString());
					}
				});
	}

	public Observable<File> getOut() {
		return out;
	}

	private Observable<File> out;

}
