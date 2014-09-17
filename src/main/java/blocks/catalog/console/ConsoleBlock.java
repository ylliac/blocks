package blocks.catalog.console;

import rx.Observer;
import blocks.core.Block;

public class ConsoleBlock extends Block {

	@Override
	protected void initialize() {
		inMessage = new Observer<String>() {

			public void onCompleted() {
				System.out.println("");
			}

			public void onError(Throwable e) {
				System.out.println("");
				System.out.println(e.getMessage());
			}

			public void onNext(String t) {
				System.out.print(t);
				System.out.print(" ");
			}
		};
	}
	
	public Observer<String> getInMessage() {
		return inMessage;
	}	
	
	private Observer<String> inMessage;

}
