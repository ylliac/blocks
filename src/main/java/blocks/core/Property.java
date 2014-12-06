package blocks.core;

import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class Property<T> {

	public Property(String name) {
		this.name = name;
		this.subject = BehaviorSubject.create();

		subject.doOnCompleted(new Action0() {

			@Override
			public void call() {
				System.out.println("COMPLETED");
			}
		});

		subject.doOnNext(new Action1<T>() {

			@Override
			public void call(T value) {
				System.out.println("VALUE : " + value);
			}
		});

		subject.doOnError(new Action1<Throwable>() {

			@Override
			public void call(Throwable error) {
				System.out.println("ERROR");
			}
		});
	}

	public String getName() {
		return name;
	}

	public Node getParent() {
		return parent;
	}

	public T get() {
		return subject.toBlocking().first();
	}

	public void set(T value) {
		subject.onNext(value);
	}

	public Observable<T> value() {
		return subject;
	}

	public void listen(Observable<T> observable) {
		observable.subscribe(subject);
	}

	public void sendTo(Observer<T> observer) {
		subject.subscribe(observer);
	}

	public Observer<T> endpoint() {
		return subject;
	}

	public void setParent(Node node) {

		Node oldParent = parent;

		parent = node;
		if (parent != null && !parent.contains(this)) {
			parent.putProperty(this);
		}
		if (node == null && oldParent != null) {
			oldParent.removeProperty(this);
		}
	}

	private BehaviorSubject<T> subject;

	private String name;

	private Node parent = null;
}
