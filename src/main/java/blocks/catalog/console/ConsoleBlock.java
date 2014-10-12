package blocks.catalog.console;

import rx.Observer;
import blocks.core.BlockSupport;

public class ConsoleBlock extends BlockSupport {

	@Override
	protected void initialize() {
		inMessage = new Observer<Object>() {

			public void onCompleted() {
				System.out.println("-------------------------");
			}

			public void onError(Throwable e) {
				System.out.println("Error : " + e.getMessage());
			}

			public void onNext(Object message) {
				System.out.println(message.toString());
			}
		};
	}

	public Observer<Object> getInMessage() {
		return inMessage;
	}

	private Observer<Object> inMessage;

}
