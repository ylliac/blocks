package blocks.demo.presentation;

import blocks.catalog.messaging.MessageReceiver;

public class DemoClientSlow {

	public static void main(String[] args) {
		DemoClientSlow demo = new DemoClientSlow();
		demo.demo();
	}

	// Démo réelle
	public void demo() {
		DemoFrame frame = new DemoFrame("Test");
		MessageReceiver<Float> receiver = new MessageReceiver<>();
		receiver.setConnectionString("tcp://brehat:5557");
		receiver.getOut().subscribe(frame.getIn());
		frame.setVisible(true);
	}

}
