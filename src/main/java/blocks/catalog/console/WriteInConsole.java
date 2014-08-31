package blocks.catalog.console;

import blocks.util.variant.Variant;

import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.Packet;

@ComponentDescription("Write in console")
@InPort("IN")
public class WriteInConsole extends Component {

	public WriteInConsole() {
	}

	@Override
	protected void execute() throws Exception {

		Packet<?> packet;
		while ((packet = in.receive()) != null) {
			String message = Variant.toString(packet.getContent());
			drop(packet);

			System.out.println(message);
		}
	}

	@Override
	protected void openPorts() {
		in = openInput("IN");
	}

	private InputPort in;
}
