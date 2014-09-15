package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class TestFrameSwing extends JFrame {

	private static final long serialVersionUID = 5580103953774796332L;

	public static void assertEventDispatchThread() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException(
					"Need to run in the event dispatch thread, but was "
							+ Thread.currentThread());
		}
	}

	/**
	 * Create an Subscription that always runs <code>unsubscribe</code> in the
	 * event dispatch thread.
	 * 
	 * @param unsubscribe
	 * @return an Subscription that always runs <code>unsubscribe</code> in the
	 *         event dispatch thread.
	 */
	public static Subscription unsubscribeInEventDispatchThread(
			final Action0 unsubscribe) {
		return Subscriptions.create(new Action0() {
			public void call() {
				if (SwingUtilities.isEventDispatchThread()) {
					unsubscribe.call();
				} else {
					final Worker inner = SwingScheduler.getInstance()
							.createWorker();
					inner.schedule(new Action0() {
						public void call() {
							unsubscribe.call();
							inner.unsubscribe();
						}
					});
				}
			}
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestFrameSwing frame = new TestFrameSwing();
		frame.setSize(250, 250);
		frame.setLocation(300, 200);
		frame.setVisible(true);
	}

	public TestFrameSwing() {
		getContentPane().add(label, BorderLayout.CENTER);
		getContentPane().add(button, BorderLayout.SOUTH);

		buttonObservable = Observable.create(new OnSubscribe<ActionEvent>() {

			public void call(final Subscriber<? super ActionEvent> subscriber) {

				assertEventDispatchThread();

				final ActionListener listener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						subscriber.onNext(e);
						subscriber.onCompleted();
					}
				};
				button.addActionListener(listener);

				subscriber.add(unsubscribeInEventDispatchThread(new Action0() {
					public void call() {
						button.removeActionListener(listener);
					}
				}));
			}

		});

		labelObserver = new Observer<String>() {

			public void onNext(String t) {
				label.setText(t);
			}

			public void onError(Throwable e) {
				label.setText(e.getMessage());
			}

			public void onCompleted() {
				label.setText("FINISHED");
			}
		};

		buttonObservable.map(new Func1<ActionEvent, String>() {

			public String call(ActionEvent t1) {
				return t1.getActionCommand();
			}
		}).subscribeOn(SwingScheduler.getInstance()).subscribe(labelObserver);
	}

	private JLabel label = new JLabel();
	private JButton button = new JButton("Test");

	private Observable<ActionEvent> buttonObservable;
	private Observer<String> labelObserver;

}
