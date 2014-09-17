package blocks.catalog.console;

import rx.Observer;
import blocks.core.Block;

public class ConsoleBlock extends Block {

	@Override
	protected void initialize() {
		inMessage = new Observer<String>() {

			public void onCompleted() {
				System.out.println("-------------------------");
			}

			public void onError(Throwable e) {
				System.out.println("Error : " + e.getMessage());
			}

			public void onNext(String message) {
				System.out.println(message);
			}
		};
	}
	
	public Observer<String> getInMessage() {
		return inMessage;
	}	
	
	private Observer<String> inMessage;

}
