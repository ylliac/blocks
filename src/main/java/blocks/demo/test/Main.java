package blocks.demo.test;

import blocks.catalog.console.WriteInConsole;

import com.jpmorrsn.fbp.engine.Network;

public class Main extends Network {

	public static void main(String[] args) throws Exception {
		new Main().go();
	}

	@Override
	protected void define() throws Exception {
		component("Console", WriteInConsole.class);
		initialize("Test", "Console.IN");
	}
}
