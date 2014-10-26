package blocks.catalog.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;
import rx.observers.EmptyObserver;
import rx.observers.Subscribers;
import rx.subjects.PublishSubject;
import blocks.core.BlockSupport;

//TODO ACY Reste un truc à corriger : quand la source s'arrête, les valeurs en buffer dans la queue ne sont pas emises
//Il faudrait plutôt utiliser un observable qui envoie les données d'une blocking queue en boucle. Quand il n'y a pas de données, il wait() tout seul comme ca.
//L'avantage serait d'exposer des blockingqueue au lieu des subject dans les interfaces graphiques
public class GateBlock extends BlockSupport {

	@Override
	protected void initialize() {

		out = PublishSubject.create();

		in = PublishSubject.create();
		// Always add values in a queue
		in.subscribe(new EmptyObserver<Float>() {
			@Override
			public void onNext(Float value) {
				queue.add(value);
			}
		});
		// On connected, return the last queue value
		final Observable<Float> queueObservable = in
				.map(new Func1<Float, Float>() {

					@Override
					public Float call(Float value) {
						try {
							return queue.take();
						} catch (InterruptedException e) {
							throw OnErrorThrowable.from(e);
						}
					}
				});

		// Connect or not
		inSwitch = PublishSubject.create();
		inSwitch.subscribe(new EmptyObserver<Boolean>() {
			@Override
			public void onNext(Boolean value) {
				if (value) {
					if (subscriber == null) {
						subscriber = Subscribers.from(out);
						queueObservable.subscribe(subscriber);
					}
				} else {
					subscriber.unsubscribe();
					subscriber = null;
				}
			}
		});
	}

	public PublishSubject<Float> getIn() {
		return in;
	}

	public PublishSubject<Float> getOut() {
		return out;
	}

	public PublishSubject<Boolean> getInSwitch() {
		return inSwitch;
	}

	private PublishSubject<Boolean> inSwitch;

	private PublishSubject<Float> in;
	private PublishSubject<Float> out;

	private Subscriber<Float> subscriber = null;

	private BlockingQueue<Float> queue = new LinkedBlockingQueue<>();

}
