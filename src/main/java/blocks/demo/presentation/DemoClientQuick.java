package blocks.demo.presentation;

import blocks.catalog.messaging.MessageReceiver;

public class DemoClientQuick {

	public static void main(String[] args) {
		DemoClientQuick demo = new DemoClientQuick();
		demo.demo();
	}

	// Démo réelle
	public void demo() {
		DemoFrame frameQuick = new DemoFrame("Test");
		MessageReceiver<Float> receiver = new MessageReceiver<>();
		receiver.setConnectionString("tcp://brehat:5556");
		receiver.getOut().subscribe(frameQuick.getIn());
		frameQuick.setVisible(true);
	}

}
