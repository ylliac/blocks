package blocks.demo.test;

import java.util.Arrays;

import blocks.catalog.console.ConsoleBlock;

import rx.Observable;

public class MainBlock {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConsoleBlock console = new ConsoleBlock();		
		Observable.from(Arrays.asList("Coucou", "comment", "ca", "va", "?")).subscribe(console.getInMessage());		
	}
}
