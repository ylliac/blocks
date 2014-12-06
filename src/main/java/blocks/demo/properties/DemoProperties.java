package blocks.demo.properties;

public class DemoProperties {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DemoProperties demo = new DemoProperties();
		demo.demo();
	}

	public void demo() {
		DemoFrame frame = new DemoFrame();
		SearchBlock searchBlock = new SearchBlock();

		frame.searchProperty().sendTo(searchBlock.getIn());
		frame.executeProperty().sendTo(searchBlock.getIn());
		frame.resultsProperty().listen(searchBlock.getOut());

		frame.setVisible(true);
	}

}
